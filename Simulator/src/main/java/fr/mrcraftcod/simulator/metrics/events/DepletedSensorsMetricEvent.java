package fr.mrcraftcod.simulator.metrics.events;

import fr.mrcraftcod.simulator.metrics.MetricEvent;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-12-19.
 *
 * @author Thomas Couchoud
 * @since 2018-12-19
 */
public class DepletedSensorsMetricEvent extends MetricEvent<Long>{
	public DepletedSensorsMetricEvent(final double time, final long count){
		super(time, count);
	}
}
