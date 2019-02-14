package fr.mrcraftcod.simulator.metrics;

import java.io.Closeable;

/**
 * A listener of a metric event.
 * <p>
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-11-22.
 *
 * @author Thomas Couchoud
 * @since 2018-11-22
 */
public interface MetricEventListener extends Closeable{
	String CSV_SEPARATOR = ",";
	
	/**
	 * Called when an event is dispatched.
	 *
	 * @param event The event dispatched.
	 */
	void onEvent(final MetricEvent event);
}
