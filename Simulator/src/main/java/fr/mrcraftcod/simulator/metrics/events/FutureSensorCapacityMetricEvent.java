package fr.mrcraftcod.simulator.metrics.events;

import fr.mrcraftcod.simulator.Environment;
import fr.mrcraftcod.simulator.metrics.FutureValueMetricEvent;
import fr.mrcraftcod.simulator.sensors.Sensor;
import org.jetbrains.annotations.NotNull;
import java.util.function.Supplier;

/**
 * Event when the capacity of a sensor changes.
 * <p>
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-11-22.
 *
 * @author Thomas Couchoud
 * @since 2018-11-22
 */
public class FutureSensorCapacityMetricEvent extends SensorCapacityMetricEvent implements FutureValueMetricEvent<Double>{
	private final Supplier<Double> valueSupplier;
	
	/**
	 * Constructor.
	 *
	 * @param environment The environment.
	 * @param time        The time of the event.
	 * @param sensor      The sensor concerned.
	 * @param newCapacity The new capacity of the sensor.
	 */
	public FutureSensorCapacityMetricEvent(final Environment environment, final double time, @NotNull final Sensor sensor, final Supplier<Double> newCapacity){
		super(environment, time, sensor, -1D);
		this.valueSupplier = newCapacity;
	}
	
	@Override
	public void generateValue(){
		this.setValue(valueSupplier.get());
	}
}
