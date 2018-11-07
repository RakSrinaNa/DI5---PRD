package fr.mrcraftcod.simulator.rault.events;

import fr.mrcraftcod.simulator.Environment;
import fr.mrcraftcod.simulator.routing.Router;
import fr.mrcraftcod.simulator.simulation.SimulationEvent;

/**
 * The event of a Lc request from a sensor.
 * <p>
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-11-07.
 *
 * @author Thomas Couchoud
 *
 */
public class LcRequestEvent extends SimulationEvent{
	/**
	 * Constructor.
	 *
	 * @param time The time of the event.
	 */
	public LcRequestEvent(final double time){
		super(time);
	}
	
	@Override
	public void accept(final Environment environment){
		environment.getElements(Router.class).stream().findFirst().ifPresent(r -> r.route(environment, LrRequestEvent.getRequestingSensors()));
	}
}
