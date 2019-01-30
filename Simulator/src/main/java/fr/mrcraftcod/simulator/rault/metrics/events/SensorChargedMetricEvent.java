package fr.mrcraftcod.simulator.rault.metrics.events;

import fr.mrcraftcod.simulator.metrics.IdentifiableMetricEvent;
import fr.mrcraftcod.simulator.sensors.Sensor;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-11-22.
 *
 * @author Thomas Couchoud
 * @since 2018-11-22
 */
public class SensorChargedMetricEvent extends IdentifiableMetricEvent<Double, Sensor>{
	public SensorChargedMetricEvent(final double time, @NotNull final Sensor sensor, final double newValue){
		super(time, sensor, newValue, 10);
	}
}
