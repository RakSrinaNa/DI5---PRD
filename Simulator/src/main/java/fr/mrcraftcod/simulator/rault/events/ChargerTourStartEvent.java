package fr.mrcraftcod.simulator.rault.events;

import fr.mrcraftcod.simulator.Environment;
import fr.mrcraftcod.simulator.rault.routing.ChargerTour;
import fr.mrcraftcod.simulator.simulation.SimulationEvent;
import fr.mrcraftcod.simulator.simulation.Simulator;

/**
 * Event when a tour starts.
 * <p>
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-11-07.
 *
 * @author Thomas Couchoud
 * @since 2018-11-07
 */
public class ChargerTourStartEvent extends SimulationEvent{
	private final ChargerTour tour;
	
	/**
	 * Constructor.
	 *
	 * @param time The time of the event.
	 * @param tour The tour.
	 */
	public ChargerTourStartEvent(final double time, final ChargerTour tour){
		super(time, 500);
		this.tour = tour;
	}
	
	@Override
	public void accept(final Environment environment){
		if(tour.getStops().isEmpty()){
			Simulator.getUnreadableQueue().add(new TourEndEvent(getTime(), tour));
		}
		else{
			Simulator.getUnreadableQueue().add(new TourTravelEvent(getTime(), tour));
		}
	}
}
