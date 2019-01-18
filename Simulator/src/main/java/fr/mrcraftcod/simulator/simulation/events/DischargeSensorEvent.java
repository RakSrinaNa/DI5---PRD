package fr.mrcraftcod.simulator.simulation.events;

import fr.mrcraftcod.simulator.Environment;
import fr.mrcraftcod.simulator.metrics.MetricEventDispatcher;
import fr.mrcraftcod.simulator.metrics.events.DepletedSensorsMetricEvent;
import fr.mrcraftcod.simulator.metrics.events.SensorCapacityMetricEvent;
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
		MetricEventDispatcher.dispatchEvent(new DepletedSensorsMetricEvent(getTime(), environment.getElements(Sensor.class).stream().map(s -> {
			s.removeCapacity(s.getDischargeSpeed());
			MetricEventDispatcher.dispatchEvent(new SensorCapacityMetricEvent(getTime(), s, s.getCurrentCapacity()));
			return s.getCurrentCapacity();
		}).filter(v -> v <= 0).count()));
		Simulator.getUnreadableQueue().add(new DischargeSensorEvent(getTime() + 1));
	}
}