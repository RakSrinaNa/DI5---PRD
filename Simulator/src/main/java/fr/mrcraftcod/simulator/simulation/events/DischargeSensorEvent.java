package fr.mrcraftcod.simulator.simulation.events;

import fr.mrcraftcod.simulator.Environment;
import fr.mrcraftcod.simulator.metrics.events.SensorCapacityMetricEvent;
import fr.mrcraftcod.simulator.metrics.events.SensorsCapacityMetricEvent;
import fr.mrcraftcod.simulator.sensors.Sensor;
import fr.mrcraftcod.simulator.simulation.SimulationEvent;

/**
 * Event to discharge sensors.
 * <p>
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-11-07.
 *
 * @author Thomas Couchoud
 */
class DischargeSensorEvent extends SimulationEvent{
	/**
	 * Constructor.
	 *
	 * @param time The time of the event.
	 */
	DischargeSensorEvent(final double time){
		super(time);
	}
	
	@Override
	public void accept(final Environment environment){
		environment.getElements(Sensor.class).forEach(s -> {
			s.removeCapacity(s.getDischargeSpeed());
			environment.getSimulator().getMetricEventDispatcher().dispatchEvent(new SensorCapacityMetricEvent(environment, getTime(), s, s::getCurrentCapacity));
		});
		environment.getSimulator().getMetricEventDispatcher().dispatchEvent(new SensorsCapacityMetricEvent(environment, getTime()));
		environment.getSimulator().getUnreadableQueue().add(new DischargeSensorEvent(getTime() + 1));
	}
}
