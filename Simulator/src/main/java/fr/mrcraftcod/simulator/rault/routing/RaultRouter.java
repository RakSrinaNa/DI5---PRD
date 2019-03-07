package fr.mrcraftcod.simulator.rault.routing;

import fr.mrcraftcod.simulator.Environment;
import fr.mrcraftcod.simulator.chargers.Charger;
import fr.mrcraftcod.simulator.rault.events.TourStartEvent;
import fr.mrcraftcod.simulator.rault.sensors.LrLcSensor;
import fr.mrcraftcod.simulator.rault.utils.TSP;
import fr.mrcraftcod.simulator.rault.utils.TSPMTW;
import fr.mrcraftcod.simulator.rault.utils.TourSolver;
import fr.mrcraftcod.simulator.routing.Router;
import fr.mrcraftcod.simulator.sensors.Sensor;
import fr.mrcraftcod.simulator.utils.Identifiable;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Router from Rault's paper.
 * <p>
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-11-07.
 *
 * @author Thomas Couchoud
 */
@SuppressWarnings("unused")
public class RaultRouter extends Router{
	private static final Logger LOGGER = LoggerFactory.getLogger(RaultRouter.class);
	private static final long TSPMTW_TIMEOUT = 60;
	
	/**
	 * Constructor.
	 */
	public RaultRouter(){
		super();
	}
	
	/**
	 * Constructor used by the JSON filler.
	 *
	 * @param environment The environment the router is in.
	 */
	public RaultRouter(@NotNull final Environment environment){
		super(environment);
	}
	
	@Override
	public boolean route(final Environment environment, final Collection<? extends Sensor> sensors){
		final var chargers = environment.getElements(Charger.class);
		if(chargers.stream().anyMatch(c -> !c.isAvailable())){
			return false;
		}
		else{
			final var executor = Executors.newCachedThreadPool();
			chargers.forEach(c -> c.setAvailable(false));
			final var stopLocations = getStopLocations(environment, chargers, sensors);
			final var chargingLocations = getChargingStops(chargers, sensors, stopLocations);
			final var tours = buildTours(environment.getRandom(), chargers, chargingLocations);
			tours.removeIf(tour -> {
				if(tour.getStops().size() > 0){
					return false;
				}
				tour.getCharger().setAvailable(true);
				return true;
			});
			buildConflictZones(tours);
			var first = true;
			for(final var tour : tours){
				if(first){
					final var maxAttempts = 3;
					final var succeeded = tryRouting(executor, maxAttempts, () -> new TSP(environment, tour), result -> {
						tour.newOrder(result.getLeft());
						tour.setArrivalTimes(result.getRight());
					});
					if(!succeeded){
						LOGGER.error("TSP failed {} times, keeping old order", maxAttempts);
					}
					
					final var firstStop = tour.getStops().get(0);
					firstStop.setChargerArrivalTime(tour.getCharger().getTravelTime(tour.getCharger().getPosition().distanceTo(firstStop.getStopLocation().getPosition())));
					var lastStop = firstStop;
					for(var i = 1; i < tour.getStops().size(); i++){
						final var currentStop = tour.getStops().get(i);
						currentStop.setChargerArrivalTime(tour.getCharger().getTravelTime(currentStop.getStopLocation().getPosition().distanceTo(lastStop.getStopLocation().getPosition())));
						lastStop = currentStop;
					}
					
					first = false;
				}
				else{
					final var maxAttempts = 3;
					final var succeeded = tryRouting(executor, maxAttempts, () -> new TSPMTW(environment, tour), result -> {
						tour.newOrder(result.getLeft());
						tour.setArrivalTimes(result.getRight());
					});
					if(!succeeded){
						LOGGER.error("TSPMTW failed {} times, keeping old order", maxAttempts);
					}
				}
				updateConflictZones(tour);
			}
			tours.stream().flatMap(t -> t.getStops().stream()).map(ChargingStop::getStopLocation).flatMap(s -> s.getSensors().stream()).filter(s -> s instanceof LrLcSensor).forEach(s -> ((LrLcSensor) s).setPlannedForCharging(true));
			tours.stream().map(t -> new TourStartEvent(environment.getSimulator().getCurrentTime(), t)).forEach(e -> environment.getSimulator().getUnreadableQueue().add(e));
			executor.shutdownNow();
			return true;
		}
	}
	
	/**
	 * Builds stop locations.
	 *
	 * @param environment The environment.
	 * @param chargers    The chargers that will be deployed.
	 * @param sensors     The sensors to cluster.
	 *
	 * @return A collection of stop locations.
	 */
	private Collection<StopLocation> getStopLocations(final Environment environment, final Collection<? extends Charger> chargers, final Collection<? extends Sensor> sensors){
		//TODO RO: Cluster sensors
		final var minRadius = chargers.stream().mapToDouble(Charger::getRadius).min().orElse(1);
		return sensors.stream().map(s -> {
			final var stopLocation = new StopLocation(s.getPosition());
			stopLocation.addSensor(s);
			environment.getElements(Sensor.class).stream().filter(s2 -> !Objects.equals(s, s2)).filter(s2 -> s2.getPosition().distanceTo(stopLocation.getPosition()) < minRadius).forEach(stopLocation::addSensor);
			return stopLocation;
		}).collect(Collectors.toList());
	}
	
	/**
	 * Builds the charging stops.
	 * It determines the area that a charger will have to charge based on the discrete points to charge.
	 *
	 * @param chargers      The chargers to use.
	 * @param sensors       The sensors to recharge.
	 * @param stopLocations The stop locations.
	 *
	 * @return A collection of charging stops.
	 */
	private Collection<ChargingStop> getChargingStops(final Collection<? extends Charger> chargers, final Collection<? extends Sensor> sensors, final Collection<StopLocation> stopLocations){
		final var chargingStops = new ArrayList<ChargingStop>();
		
		final var chargingSensors = sensors.stream().map(s -> {
			final var distance = stopLocations.stream().filter(stopLocation -> stopLocation.contains(s)).mapToDouble(stopLocation -> stopLocation.getPosition().distanceTo(s.getPosition())).max().orElse(1);
			var chargeTime = (s.getMaxCapacity() - s.getCurrentCapacity()) / chargers.stream().min(Comparator.comparingDouble(Charger::getTransmissionPower)).map(c -> c.getReceivedPower(distance)).orElse(1D);
			return new ChargingSensor(s, chargeTime);
		}).collect(Collectors.toList());
		
		final var stopLocationsLeft = new LinkedList<>(stopLocations);
		StopLocation stopLocation;
		while((stopLocation = stopLocationsLeft.poll()) != null){
			final var unknownChargingSensors = new ArrayList<ChargingSensor>();
			final var chargeTime = new AtomicReference<>(0D);
			for(final Sensor sensor : stopLocation.getSensors()){
				chargingSensors.stream().filter(c -> c.is(sensor)).findFirst().ifPresent(chargingSensor -> {
					if(stopLocationsLeft.stream().anyMatch(s -> s.contains(sensor))){
						unknownChargingSensors.add(chargingSensor);
					}
					else{
						chargeTime.set(Math.max(chargeTime.get(), chargingSensor.getChargeTime()));
					}
				});
			}
			unknownChargingSensors.forEach(s -> s.setChargeTime(s.getChargeTime() - chargeTime.get()));
			chargingStops.add(new ChargingStop(stopLocation, chargeTime.get()));
		}
		
		return chargingStops;
	}
	
	/**
	 * Build the tours for the chargers.
	 *
	 * @param random        The random object to use.
	 * @param chargers      The chargers to use.
	 * @param chargingStops The stops to go through.
	 *
	 * @return A collection of tours.
	 */
	private Collection<ChargerTour> buildTours(final Random random, final Collection<? extends Charger> chargers, final Collection<ChargingStop> chargingStops){
		final var remainingStops = new ArrayList<>(chargingStops);
		final var tours = chargers.stream().map(ChargerTour::new).collect(Collectors.toList());
		tours.forEach(t -> {
			t.setParent(tours);
			if(!remainingStops.isEmpty()){
				final var rnd = random.nextInt(remainingStops.size());
				final var stop = remainingStops.get(rnd);
				stop.setCharger(t.getCharger());
				t.addStop(stop);
				remainingStops.remove(rnd);
			}
		});
		
		while(!remainingStops.isEmpty()){
			tours.stream().min(Comparator.comparingDouble(ChargerTour::getAccumulatedTime)).ifPresent(tour -> remainingStops.stream().min(Comparator.comparingDouble(tour::distanceTo)).ifPresent(closest -> {
				closest.setCharger(tour.getCharger());
				tour.addStop(closest);
				remainingStops.remove(closest);
			}));
		}
		return tours;
	}
	
	/**
	 * Create the conflict zones for each tour.
	 *
	 * @param tours The tours to consider.
	 */
	private void buildConflictZones(final Collection<ChargerTour> tours){
		tours.forEach(tour -> tours.parallelStream().filter(t -> !Objects.equals(t, tour)).forEach(tour2 -> tour2.getStops().forEach(t2 -> tour.getStops().forEach(t -> {
			if(tour.getCharger().getRadius() + tour2.getCharger().getRadius() >= t.getStopLocation().getPosition().distanceTo(t2.getStopLocation().getPosition())){
				t.addConflictZone(t2);
				t2.addConflictZone(t);
			}
		}))));
	}
	
	/**
	 * Perform a routing in a new thread with a timeout.
	 *
	 * @param executor       The executor to run in.
	 * @param maxAttempts    The number of attempts.
	 * @param solverSupplier How to build a new solver.
	 * @param resultConsumer What to do with the result.
	 *
	 * @return True if the solver was run successfully, false otherwise.
	 */
	private boolean tryRouting(final ExecutorService executor, @SuppressWarnings("SameParameterValue") final int maxAttempts, final Supplier<TourSolver> solverSupplier, final Consumer<Pair<List<Integer>, List<Double>>> resultConsumer){
		var attemptCount = 0;
		Future<Optional<Pair<List<Integer>, List<Double>>>> tspmtwFuture;
		do{
			attemptCount++;
			final var tourSolver = solverSupplier.get();
			tspmtwFuture = executor.submit(tourSolver);
			try{
				final var resultOptional = tspmtwFuture.get(tourSolver.getTimeout() + 5, TimeUnit.SECONDS);
				resultOptional.ifPresent(resultConsumer);
			}
			catch(final TimeoutException e){
				tspmtwFuture.cancel(true);
				LOGGER.error("Error while running TSPMTW, did not complete in the given time of {} seconds", tourSolver.getTimeout());
			}
			catch(final InterruptedException | ExecutionException e){
				LOGGER.error("Error while running TSPMTW", e);
			}
		}
		while(tspmtwFuture.isCancelled() && attemptCount < maxAttempts);
		return !tspmtwFuture.isCancelled();
	}
	
	/**
	 * Update the conflict zones for each tour.
	 *
	 * @param tour The tour to update.
	 */
	private void updateConflictZones(final ChargerTour tour){
		if(tour.getStops().isEmpty()){
			return;
		}
		final var firstStop = tour.getStops().get(0);
		firstStop.getConflictZones().forEach(cz -> cz.addForbiddenTime(firstStop.getChargerArrivalTime(), firstStop.getChargerArrivalTime() + firstStop.getChargingTime()));
		for(var i = 1; i < tour.getStops().size(); i++){
			final var currentStop = tour.getStops().get(i);
			currentStop.getConflictZones().forEach(cz -> cz.addForbiddenTime(currentStop.getChargerArrivalTime(), currentStop.getChargerArrivalTime() + currentStop.getChargingTime()));
		}
	}
	
	@Override
	public boolean haveSameValues(final Identifiable identifiable){
		return this.getClass().isInstance(identifiable);
	}
}
