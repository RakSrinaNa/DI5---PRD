package fr.mrcraftcod.simulator.metrics.events;

import fr.mrcraftcod.simulator.Environment;
import fr.mrcraftcod.simulator.metrics.IdentifiableMetricEvent;
import fr.mrcraftcod.simulator.sensors.Sensor;
import org.jetbrains.annotations.NotNull;

/**
 * Event when the capacity of a sensor changes.
 * <p>
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-11-22.
 *
 * @author Thomas Couchoud
 * @since 2018-11-22
 */
public class SensorCapacityMetricEvent extends IdentifiableMetricEvent<Double, Sensor>{
	/**
	 * Constructor.
	 *
	 * @param environment The environment.
	 * @param time        The time of the event.
	 * @param sensor      The sensor concerned.
	 * @param newCapacity The new capacity of the sensor.
	 */
	public SensorCapacityMetricEvent(final Environment environment, final double time, @NotNull final Sensor sensor, final Double newCapacity){
		super(environment, time, sensor, newCapacity);
	}
}
