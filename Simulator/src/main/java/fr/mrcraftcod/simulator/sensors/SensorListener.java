package fr.mrcraftcod.simulator.sensors;

import org.jetbrains.annotations.NotNull;

/**
 * Define an interface to listen to events inside a sensor.
 * <p>
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-10-04.
 *
 * @author Thomas Couchoud
 * @since 1.0.0
 */
public interface SensorListener{
	/**
	 * Called when a sensor current capacity changes.
	 *
	 * @param sensor             The sensor concerned.
	 * @param newCurrentCapacity The new capacity value.
	 *
	 * @since 1.0.0
	 */
	void onSensorCurrentCapacityChange(@NotNull final Sensor sensor, double newCurrentCapacity);
}
