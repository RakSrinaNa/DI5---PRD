package fr.mrcraftcod.simulator.metrics.events;

import fr.mrcraftcod.simulator.Environment;
import fr.mrcraftcod.simulator.metrics.MetricEvent;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-11-22.
 *
 * @author Thomas Couchoud
 * @since 2018-11-22
 */
public class SensorsCapacityMetricEvent extends MetricEvent{
	public SensorsCapacityMetricEvent(final Environment environment, final double time){
		super(environment, time);
	}
}
