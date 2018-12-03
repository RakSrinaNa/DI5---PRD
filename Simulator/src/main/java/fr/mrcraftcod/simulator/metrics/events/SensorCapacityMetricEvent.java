package fr.mrcraftcod.simulator.metrics.events;

import fr.mrcraftcod.simulator.metrics.MetricEvent;
import fr.mrcraftcod.simulator.sensors.Sensor;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-11-22.
 *
 * @author Thomas Couchoud
 * @since 2018-11-22
 */
public class SensorCapacityMetricEvent extends MetricEvent<Double>{
	public SensorCapacityMetricEvent(final double time, @NotNull final Sensor sensor, final double newValue){
		super(time, sensor, newValue);}
}
