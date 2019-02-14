package fr.mrcraftcod.simulator.rault.events;

import fr.mrcraftcod.simulator.Environment;
import fr.mrcraftcod.simulator.rault.metrics.events.TourStartMetricEvent;
import fr.mrcraftcod.simulator.rault.routing.ChargerTour;
import fr.mrcraftcod.simulator.simulation.SimulationEvent;

/**
 * Event when a tour starts.
 * <p>
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-11-07.
 *
 * @author Thomas Couchoud
 */
public class TourStartEvent extends SimulationEvent{
	private final ChargerTour tour;
	
	/**
	 * Constructor.
	 *
	 * @param time The time of the event.
	 * @param tour The tour.
	 */
	public TourStartEvent(final double time, final ChargerTour tour){
		super(time, 500);
		this.tour = tour;
	}
	
	@Override
	public void accept(final Environment environment){
		if(tour.getStops().isEmpty()){
			environment.getSimulator().getUnreadableQueue().add(new TourEndEvent(getTime(), tour));
		}
		else{
			environment.getSimulator().getMetricEventDispatcher().dispatchEvent(new TourStartMetricEvent(environment, getTime(), getTour().getCharger(), getTour()));
			environment.getSimulator().getUnreadableQueue().add(new TourTravelEvent(getTime(), tour));
		}
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
