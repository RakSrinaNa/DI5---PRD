package fr.mrcraftcod.simulator.rault.events;

import fr.mrcraftcod.simulator.Environment;
import fr.mrcraftcod.simulator.positions.Position;
import fr.mrcraftcod.simulator.rault.metrics.events.TourTravelBaseMetricEvent;
import fr.mrcraftcod.simulator.rault.metrics.events.TourTravelEndMetricEvent;
import fr.mrcraftcod.simulator.rault.metrics.events.TourTravelMetricEvent;
import fr.mrcraftcod.simulator.rault.routing.ChargerTour;
import fr.mrcraftcod.simulator.simulation.SimulationEvent;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Optional;

/**
 * Event when a charger is traveling during its tour.
 * <p>
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-11-07.
 *
 * @author Thomas Couchoud
 */
class TourTravelEvent extends SimulationEvent{
	private static final Logger LOGGER = LoggerFactory.getLogger(TourTravelEvent.class);
	private final ChargerTour tour;
	
	/**
	 * Constructor.
	 *
	 * @param time The time of the event.
	 * @param tour The tour.
	 */
	TourTravelEvent(final double time, final ChargerTour tour){
		super(time);
		this.tour = tour;
	}
	
	@Override
	public void accept(final Environment environment){
		getTour().getCharger().setCharging(false);
		Optional.ofNullable(tour.getStops().peek()).ifPresentOrElse(nextStop -> {
			final var pos = nextStop.getStopLocation().getPosition();
			final var distance = tour.getCharger().getPosition().distanceTo(pos);
			
			final var travelTime = tour.getCharger().getTravelTime(distance);
			LOGGER.debug("Charger {} will travel to {} (distance: {}, travel time: {})", tour.getCharger().getUniqueIdentifier(), pos, distance, travelTime);
			tour.getCharger().removeCapacity(tour.getCharger().getTravelConsumption(travelTime));
			final var lastPos = tour.getCharger().getPosition();
			tour.getCharger().setPosition(pos);
			environment.getSimulator().getMetricEventDispatcher().dispatchEvent(new TourTravelMetricEvent(environment, getTime(), getTour().getCharger(), new ImmutablePair<>(lastPos, nextStop)));
			environment.getSimulator().getMetricEventDispatcher().dispatchEvent(new TourTravelEndMetricEvent(environment, getTime() + travelTime, getTour().getCharger(), nextStop));
			environment.getSimulator().getUnreadableQueue().add(new TourChargeEvent(getTime() + travelTime, tour));
		}, () -> {
			final var lastPos = tour.getCharger().getPosition();
			tour.getCharger().setPosition(new Position(0, 0));
			environment.getSimulator().getMetricEventDispatcher().dispatchEvent(new TourTravelBaseMetricEvent(environment, getTime(), getTour().getCharger(), new ImmutablePair<>(lastPos, new Position(0, 0))));
			environment.getSimulator().getUnreadableQueue().add(new TourEndEvent(getTime() + tour.getCharger().getTravelTime(lastPos.distanceTo(new Position(0, 0))), tour));
		});
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
