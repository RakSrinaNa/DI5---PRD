package fr.mrcraftcod.simulator.rault.routing;

import fr.mrcraftcod.simulator.Environment;
import fr.mrcraftcod.simulator.chargers.Charger;
import fr.mrcraftcod.simulator.rault.events.ChargerTourStartEvent;
import fr.mrcraftcod.simulator.rault.events.LcRequestEvent;
import fr.mrcraftcod.simulator.rault.utils.TSP;
import fr.mrcraftcod.simulator.rault.utils.TSPMTW;
import fr.mrcraftcod.simulator.routing.Router;
import fr.mrcraftcod.simulator.sensors.Sensor;
import fr.mrcraftcod.simulator.simulation.Simulator;
import fr.mrcraftcod.simulator.utils.Identifiable;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * Router from Rault's paper.
 * <p>
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-11-07.
 *
 * @author Thomas Couchoud
 */
public class RaultRouter extends Router{
	private static final Logger LOGGER = LoggerFactory.getLogger(RaultRouter.class);
	
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
	
	/**
	 * Builds stop locations.
	 *
	 * @param sensors The sensors to cluster.
	 *
	 * @return A collection of stop locations.
	 */
	private Collection<StopLocation> getStopLocations(final Collection<? extends Sensor> sensors){
		//TODO RO
		return sensors.stream().map(s -> {
			final var stopLocation = new StopLocation(s.getPosition());
			stopLocation.addSensor(s);
			return stopLocation;
		}).collect(Collectors.toList());
	}
	
	/**
	 * Builds the charging stops.
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
			final var rnd = random.nextInt(chargingStops.size());
			t.addStop(remainingStops.get(rnd));
			remainingStops.remove(rnd);
		});
		
		while(!remainingStops.isEmpty()){
			tours.stream().min(Comparator.comparingDouble(ChargerTour::getAccumulatedTime)).ifPresent(tour -> remainingStops.stream().min(Comparator.comparingDouble(tour::distanceTo)).ifPresent(closest -> {
				tour.addStop(closest);
				remainingStops.remove(closest);
			}));
		}
		return tours;
	}
	
	@Override
	public void route(final Environment environment, final Collection<? extends Sensor> sensors){
		final var chargers = environment.getElements(Charger.class).stream().filter(Charger::isAvailable).collect(Collectors.toList());
		if(chargers.size() < 1){
			Simulator.getUnreadableQueue().add(new LcRequestEvent(Simulator.getCurrentTime() + 100));
		}
		else{
			chargers.forEach(c -> c.setAvailable(false));
			final var stopLocations = getStopLocations(sensors);
			final var chargingLocations = getChargingStops(chargers, sensors, stopLocations);
			final var tours = buildTours(environment.getRandom(), chargers, chargingLocations);
			var first = true;
			for(final var tour : tours){
				if(first){
					new TSP(tour).solve();
					first = false;
				}
				else{
					new TSPMTW(tour).solve();
				}
			}
			tours.stream().map(t -> new ChargerTourStartEvent(Simulator.getCurrentTime(), t)).forEach(e -> Simulator.getUnreadableQueue().add(e));
		}
	}
	
	@Override
	public boolean haveSameValues(final Identifiable identifiable){
		return this.getClass().isInstance(identifiable);
	}
}
