package fr.mrcraftcod.simulator.rault.metrics.events;

import fr.mrcraftcod.simulator.Environment;
import fr.mrcraftcod.simulator.metrics.IdentifiableMetricEvent;
import fr.mrcraftcod.simulator.sensors.Sensor;
import org.jetbrains.annotations.NotNull;

/**
 * Fired when a sensor is charged.
 * <p>
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-11-22.
 *
 * @author Thomas Couchoud
 * @since 2018-11-22
 */
public class SensorChargedMetricEvent extends IdentifiableMetricEvent<Double, Sensor>{
	/**
	 * Constructor.
	 *
	 * @param environment The environment.
	 * @param time        The time of the event.
	 * @param sensor      The sensor.
	 * @param newValue    The capacity of the sensor.
	 */
	public SensorChargedMetricEvent(final Environment environment, final double time, @NotNull final Sensor sensor, final double newValue){
		super(environment, time, sensor, newValue, 10);
	}
}
