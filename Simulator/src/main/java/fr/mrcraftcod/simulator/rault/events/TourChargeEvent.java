package fr.mrcraftcod.simulator.rault.events;

import fr.mrcraftcod.simulator.Environment;
import fr.mrcraftcod.simulator.chargers.Charger;
import fr.mrcraftcod.simulator.metrics.MetricEventDispatcher;
import fr.mrcraftcod.simulator.rault.metrics.events.SensorChargedMetricEvent;
import fr.mrcraftcod.simulator.rault.metrics.events.TourChargeEndMetricEvent;
import fr.mrcraftcod.simulator.rault.metrics.events.TourChargeMetricEvent;
import fr.mrcraftcod.simulator.rault.routing.ChargerTour;
import fr.mrcraftcod.simulator.rault.routing.ChargingStop;
import fr.mrcraftcod.simulator.rault.sensors.LrLcSensor;
import fr.mrcraftcod.simulator.simulation.SimulationEvent;
import fr.mrcraftcod.simulator.simulation.Simulator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Event when a charger is charging.
 * <p>
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-11-07.
 *
 * @author Thomas Couchoud
 */
class TourChargeEvent extends SimulationEvent{
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
		Optional.ofNullable(tour.getStops().peek()).ifPresentOrElse(chargingStop -> {
			final var conflict = chargingStop.getConflictZones().stream().filter(c -> tour.getCharger().getRadius() + c.getCharger().getRadius() >= tour.getCharger().getPosition().distanceTo(c.getCharger().getPosition())).map(ChargingStop::getCharger).anyMatch(Charger::isCharging);
			if(conflict){
				LOGGER.debug("Charger {} in conflict, waiting", getTour().getCharger());
				Simulator.getUnreadableQueue().add(new TourChargeEvent(getTime() + 1, tour));
			}
			else{
				tour.getStops().remove(chargingStop);
				LOGGER.debug("Charger {} charging {}", tour.getCharger().getUniqueIdentifier(), chargingStop.getStopLocation().getPosition());
				getTour().getCharger().setCharging(true);
				final var chargeTimeMax = new AtomicReference<>(0D);
				chargingStop.getStopLocation().getSensors().forEach(s -> {
					final var distance = chargingStop.getStopLocation().getPosition().distanceTo(s.getPosition());
					final var toCharge = s.getMaxCapacity() - s.getCurrentCapacity();
					final var chargeTime = toCharge / tour.getCharger().getReceivedPower(distance);
					chargeTimeMax.set(Math.max(chargeTimeMax.get(), chargeTime));
					s.addCapacity(toCharge);
					if(s instanceof LrLcSensor){
						((LrLcSensor) s).setPlannedForCharging(false);
					}
					MetricEventDispatcher.dispatchEvent(new SensorChargedMetricEvent(getTime() + chargeTime, s, toCharge));
				});
				tour.getCharger().removeCapacity(chargeTimeMax.get() * tour.getCharger().getTransmissionPower());
				LOGGER.debug("Charger {} charged sensors, will wait for charge time to end and leave at {}", tour.getCharger().getUniqueIdentifier(), getTime() + chargeTimeMax.get());
				MetricEventDispatcher.dispatchEvent(new TourChargeMetricEvent(getTime(), getTour().getCharger(), chargingStop));
				MetricEventDispatcher.dispatchEvent(new TourChargeEndMetricEvent(getTime() + chargeTimeMax.get(), getTour().getCharger(), chargingStop));
				Simulator.getUnreadableQueue().add(new TourTravelEvent(getTime() + chargeTimeMax.get(), tour));
			}
		}, () -> Simulator.getUnreadableQueue().add(new TourTravelEvent(getTime(), tour)));
	}
	
	private ChargerTour getTour(){
		return this.tour;
	}
}
