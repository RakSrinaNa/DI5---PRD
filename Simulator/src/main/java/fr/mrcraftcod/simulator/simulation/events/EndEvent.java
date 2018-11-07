package fr.mrcraftcod.simulator.simulation.events;

import fr.mrcraftcod.simulator.Environment;
import fr.mrcraftcod.simulator.simulation.SimulationEvent;
import fr.mrcraftcod.simulator.simulation.Simulator;

/**
 * Event of the end of the simulation.
 * <p>
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-11-07.
 *
 * @author Thomas Couchoud
 *
 */
public class EndEvent extends SimulationEvent{
	/**
	 * Constructor.
	 *
	 * @param time The time of the event.
	 */
	public EndEvent(final double time){
		super(time);
	}
	
	@Override
	public void accept(final Environment environment){
		Simulator.getSimulator(environment).stop();
	}
}
