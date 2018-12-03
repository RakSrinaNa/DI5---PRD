package fr.mrcraftcod.simulator.metrics;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-11-22.
 *
 * @author Thomas Couchoud
 * @since 2018-11-22
 */
public interface MetricEventListener{
	void onEvent(final MetricEvent event);
	
	void onEnd();
}
