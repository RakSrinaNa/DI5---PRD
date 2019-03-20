package fr.mrcraftcod.simulator.rault.events;

import fr.mrcraftcod.simulator.Environment;
import fr.mrcraftcod.simulator.chargers.Charger;
import fr.mrcraftcod.simulator.metrics.events.FutureSensorCapacityMetricEvent;
import fr.mrcraftcod.simulator.rault.metrics.events.ChargerDischargedMetricEvent;
import fr.mrcraftcod.simulator.rault.metrics.events.SensorChargedMetricEvent;
import fr.mrcraftcod.simulator.rault.metrics.events.TourChargeEndMetricEvent;
import fr.mrcraftcod.simulator.rault.metrics.events.TourChargeMetricEvent;
import fr.mrcraftcod.simulator.rault.routing.ChargerTour;
import fr.mrcraftcod.simulator.rault.routing.ChargingStop;
import fr.mrcraftcod.simulator.rault.routing.RaultRouterModified;
import fr.mrcraftcod.simulator.rault.sensors.LrLcSensor;
import fr.mrcraftcod.simulator.routing.Router;
import fr.mrcraftcod.simulator.sensors.Sensor;
import fr.mrcraftcod.simulator.simulation.SimulationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Event when a charger is charging.
 * <p>
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-11-07.
 *
 * @author Thomas Couchoud
 */
@SuppressWarnings("WeakerAccess")
public class TourChargeEvent extends SimulationEvent{
	private static final Logger LOGGER = LoggerFactory.getLogger(TourChargeEvent.class);
	private final ChargerTour tour;
	
	/**
	 * Constructor.
	 *
	 * @param time The time of the event.
	 * @param tour The tour.
	 */
	TourChargeEvent(final double time, final ChargerTour tour){
		super(time);
		this.tour = tour;
	}
	
	@Override
	public void accept(final Environment environment){
		final var chargeMultipleSteps = environment.getElements(Router.class).stream().allMatch(e -> e instanceof RaultRouterModified);
		Optional.ofNullable(tour.getStops().peek()).ifPresentOrElse(chargingStop -> {
			final var conflict = chargingStop.getConflictZones().stream().filter(c -> tour.getCharger().getRadius() + c.getCharger().getRadius() >= tour.getCharger().getPosition().distanceTo(c.getCharger().getPosition())).map(ChargingStop::getCharger).anyMatch(Charger::isCharging);
			if(conflict){
				LOGGER.trace("Charger {} in conflict, waiting", getTour().getCharger());
				environment.getSimulator().getUnreadableQueue().add(new TourChargeEvent(getTime() + 1, tour));
			}
			else{
				tour.getStops().remove(chargingStop);
				LOGGER.trace("Charger {} charging {}", tour.getCharger().getUniqueIdentifier(), chargingStop.getStopLocation().getPosition());
				getTour().getCharger().setCharging(true);
				final var chargeTimeMax = new AtomicReference<>(0D);
				final var toAssign = new ArrayList<Sensor>();
				chargingStop.getStopLocation().getSensors().forEach(s -> {
					if(chargeMultipleSteps && tour.getParent().stream().filter(ct -> !Objects.equals(ct, getTour())).flatMap(ct -> ct.getStops().stream()).anyMatch(ct -> ct.contains(s))){
						toAssign.add(s);
					}
					else{
						final var distance = chargingStop.getStopLocation().getPosition().distanceTo(s.getPosition());
						final var toCharge = s.getMaxCapacity() - s.getCurrentCapacity();
						final var chargeTime = toCharge / tour.getCharger().getReceivedPower(distance);
						chargeTimeMax.set(Math.max(chargeTimeMax.get(), chargeTime));
						s.addCapacity(toCharge);
						if(s instanceof LrLcSensor){
							((LrLcSensor) s).setPlannedForCharging(false);
						}
						environment.getSimulator().getMetricEventDispatcher().dispatchEvent(new SensorChargedMetricEvent(environment, getTime() + chargeTime, s, toCharge));
						environment.getSimulator().getMetricEventDispatcher().dispatchEvent(new FutureSensorCapacityMetricEvent(environment, getTime() + chargeTime, s, s::getCurrentCapacity));
					}
				});
				for(final var s : toAssign){
					final var distance = chargingStop.getStopLocation().getPosition().distanceTo(s.getPosition());
					final var chargeTime = chargeTimeMax.get();
					final var toCharge = chargeTime * tour.getCharger().getReceivedPower(distance);
					s.addCapacity(toCharge);
					environment.getSimulator().getMetricEventDispatcher().dispatchEvent(new SensorChargedMetricEvent(environment, getTime() + chargeTime, s, toCharge));
					environment.getSimulator().getMetricEventDispatcher().dispatchEvent(new FutureSensorCapacityMetricEvent(environment, getTime() + chargeTime, s, s::getCurrentCapacity));
				}
				final var powerUsed = tour.getCharger().getCapacityUsed(chargeTimeMax.get());
				tour.getCharger().removeCapacity(powerUsed);
				LOGGER.trace("Charger {} charged sensors, will wait for charge time to end and leave at {}", tour.getCharger().getUniqueIdentifier(), getTime() + chargeTimeMax.get());
				environment.getSimulator().getMetricEventDispatcher().dispatchEvent(new TourChargeMetricEvent(environment, getTime(), getTour().getCharger(), chargingStop));
				environment.getSimulator().getMetricEventDispatcher().dispatchEvent(new ChargerDischargedMetricEvent(environment, getTime() + chargeTimeMax.get(), getTour().getCharger(), powerUsed));
				environment.getSimulator().getMetricEventDispatcher().dispatchEvent(new TourChargeEndMetricEvent(environment, getTime() + chargeTimeMax.get(), getTour().getCharger(), chargingStop));
				environment.getSimulator().getUnreadableQueue().add(new TourTravelEvent(getTime() + chargeTimeMax.get(), tour));
			}
		}, () -> environment.getSimulator().getUnreadableQueue().add(new TourTravelEvent(getTime(), tour)));
	}
	
	/**
	 * Get the tour.
	 *
	 * @return The tour.
	 */
	private ChargerTour getTour(){
		return this.tour;
	}
}
