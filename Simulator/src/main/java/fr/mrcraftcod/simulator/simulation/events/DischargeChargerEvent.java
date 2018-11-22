package fr.mrcraftcod.simulator.simulation.events;

import fr.mrcraftcod.simulator.Environment;
import fr.mrcraftcod.simulator.sensors.Sensor;
import fr.mrcraftcod.simulator.simulation.SimulationEvent;
import fr.mrcraftcod.simulator.simulation.Simulator;

/**
 * Event to discharge sensors.
 * <p>
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-11-07.
 *
 * @author Thomas Couchoud
 */
class DischargeChargerEvent extends SimulationEvent{
	/**
	 * Constructor.
	 *
	 * @param time The time of the event.
	 */
	DischargeChargerEvent(final double time){
		super(time);
	}
	
	@Override
	public void accept(final Environment environment){
		environment.getElements(Sensor.class).forEach(s -> s.removeCapacity(s.getDischargeSpeed()));
		Simulator.getUnreadableQueue().add(new DischargeChargerEvent(getTime() + 1));
	}
}
