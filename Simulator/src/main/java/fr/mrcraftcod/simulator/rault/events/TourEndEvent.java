package fr.mrcraftcod.simulator.rault.events;

import fr.mrcraftcod.simulator.Environment;
import fr.mrcraftcod.simulator.metrics.MetricEventDispatcher;
import fr.mrcraftcod.simulator.rault.metrics.events.TourEndMetricEvent;
import fr.mrcraftcod.simulator.rault.routing.ChargerTour;
import fr.mrcraftcod.simulator.simulation.SimulationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Event when a tour ends.
 * <p>
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-11-07.
 *
 * @author Thomas Couchoud
 */
class TourEndEvent extends SimulationEvent{
	private static final Logger LOGGER = LoggerFactory.getLogger(TourEndEvent.class);
	private final ChargerTour tour;
	
	/**
	 * Constructor.
	 *
	 * @param time The time of the event.
	 * @param tour The tour.
	 */
	TourEndEvent(final double time, final ChargerTour tour){
		super(time);
		this.tour = tour;
	}
	
	@Override
	public void accept(final Environment environment){
		LOGGER.debug("Tour for charger {} ended, setting charger as available", tour.getCharger().getUniqueIdentifier());
		MetricEventDispatcher.dispatchEvent(new TourEndMetricEvent(getTime(), getTour().getCharger(), getTour()));
		tour.getCharger().setAvailable(true);
	}
	
	private ChargerTour getTour(){
		return this.tour;
	}
}
