package fr.mrcraftcod.simulator.metrics;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-11-22.
 *
 * @author Thomas Couchoud
 * @since 2018-11-22
 */
public class MetricEventDispatcher{
	private static final List<MetricEventListener> LISTENERS = new ArrayList<>();
	
	public static void addListener(final MetricEventListener listener){
		LISTENERS.add(listener);
	}
	
	public static void removeListener(final MetricEventListener listener){
		LISTENERS.remove(listener);
	}
	
	public static void dispatchEvent(final MetricEvent event){
		LISTENERS.parallelStream().forEach(l -> l.onEvent(event));
	}
}