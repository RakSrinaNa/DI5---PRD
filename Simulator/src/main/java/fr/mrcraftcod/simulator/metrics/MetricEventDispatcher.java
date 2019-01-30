package fr.mrcraftcod.simulator.metrics;

import fr.mrcraftcod.simulator.simulation.Simulator;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-11-22.
 *
 * @author Thomas Couchoud
 * @since 2018-11-22
 */
public class MetricEventDispatcher{
	private static final List<MetricEventListener> LISTENERS = new ArrayList<>();
	private static final Queue<MetricEvent> FUTURES = new PriorityQueue<>();
	
	public static void addListener(final MetricEventListener listener){
		LISTENERS.add(listener);
	}
	
	public static void dispatchEvent(final MetricEvent event){
		if(event.getTime() <= Simulator.getCurrentTime()){
			if(FUTURES.isEmpty()){
				LISTENERS.parallelStream().forEach(l -> l.onEvent(event));
			}
			else{
				FUTURES.offer(event);
				fire();
			}
		}
		else{
			FUTURES.offer(event);
		}
	}
	
	public static void removeListener(final MetricEventListener listener){
		LISTENERS.remove(listener);
	}
	
	public static void fire(){
		while(!FUTURES.isEmpty() && FUTURES.peek().getTime() <= Simulator.getCurrentTime()){
			final var event = FUTURES.poll();
			LISTENERS.parallelStream().forEach(l -> l.onEvent(event));
		}
	}
	
	public static void clear(){
		FUTURES.clear();
	}
}
