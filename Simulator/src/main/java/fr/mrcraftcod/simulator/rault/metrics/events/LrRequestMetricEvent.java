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
public class LrRequestMetricEvent extends IdentifiableMetricEvent<Void, Sensor>{
	public LrRequestMetricEvent(final double time, @NotNull final Sensor sensor){
		super(time, sensor, null);}
}
