package fr.mrcraftcod.simulator.metrics;

import fr.mrcraftcod.simulator.Environment;
import java.io.Closeable;
import java.io.IOException;
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
@SuppressWarnings({
		"WeakerAccess",
		"unused"
})
public class MetricEventDispatcher implements Closeable{
	private final List<MetricEventListener> LISTENERS = new ArrayList<>();
	private final Queue<MetricEvent> FUTURES = new PriorityQueue<>();
	private final Environment environment;
	private boolean closed;
	
	public MetricEventDispatcher(final Environment environment){
		this.environment = environment;
		this.closed = false;
	}
	
	public void addListener(final MetricEventListener listener){
		LISTENERS.add(listener);
	}
	
	public void dispatchEvent(final MetricEvent event){
		if(event.getTime() <= environment.getSimulator().getCurrentTime()){
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
	
	public void fire(){
		while(!FUTURES.isEmpty() && FUTURES.peek().getTime() <= environment.getSimulator().getCurrentTime()){
			final var event = FUTURES.poll();
			LISTENERS.parallelStream().forEach(l -> l.onEvent(event));
		}
	}
	
	public void removeListener(final MetricEventListener listener){
		LISTENERS.remove(listener);
	}
	
	@Override
	public void close(){
		if(!isClosed()){
			this.closed = true;
			clear();
			LISTENERS.forEach(l -> {
				try{
					l.close();
				}
				catch(final IOException e){
					e.printStackTrace();
				}
			});
		}
	}
	
	private boolean isClosed(){
		return this.closed;
	}
	
	public void clear(){
		FUTURES.clear();
	}
}
