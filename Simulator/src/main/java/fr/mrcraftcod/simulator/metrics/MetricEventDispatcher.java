package fr.mrcraftcod.simulator.metrics;

import fr.mrcraftcod.simulator.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;

/**
 * Dispatches {@link MetricEvent}s to the {@link MetricEventListener}s.
 * <p>
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-11-22.
 *
 * @author Thomas Couchoud
 * @since 2018-11-22
 */
@SuppressWarnings("WeakerAccess")
public class MetricEventDispatcher implements Closeable{
	private static final Logger LOGGER = LoggerFactory.getLogger(MetricEventDispatcher.class);
	private final List<MetricEventListener> listeners = new ArrayList<>();
	private final Queue<MetricEvent> futures = new PriorityQueue<>();
	private final Environment environment;
	private boolean closed;
	
	/**
	 * Constructor.
	 *
	 * @param environment The environment.
	 */
	public MetricEventDispatcher(final Environment environment){
		this.environment = environment;
		this.closed = false;
		try{
			Files.createDirectories(MetricEvent.getAllMetricSaveFolder());
			if(Objects.nonNull(environment.getConfigurationPath())){
				Files.copy(environment.getConfigurationPath(), MetricEvent.getAllMetricSaveFolder().resolve("config.json"), StandardCopyOption.REPLACE_EXISTING);
			}
		}
		catch(final IOException e){
			LOGGER.error("Failed to create directory {}", MetricEvent.getAllMetricSaveFolder(), e);
		}
	}
	
	/**
	 * Add a listener.
	 *
	 * @param listener The listener to add.
	 */
	public void addListener(final MetricEventListener listener){
		listeners.add(listener);
	}
	
	/**
	 * Dispatch an event to the listeners.
	 *
	 * @param event The event to dispatch.
	 */
	public void dispatchEvent(final MetricEvent event){
		if(event.getTime() <= environment.getSimulator().getCurrentTime()){
			if(futures.isEmpty()){
				if(event instanceof FutureValueMetricEvent){
					((FutureValueMetricEvent) event).generateValue();
				}
				listeners.parallelStream().forEach(l -> l.onEvent(event));
			}
			else{
				futures.offer(event);
				fire();
			}
		}
		else{
			futures.offer(event);
		}
	}
	
	/**
	 * Fire a dispatch to retry events that were ahead of time.
	 */
	public void fire(){
		while(!futures.isEmpty() && futures.peek().getTime() <= environment.getSimulator().getCurrentTime()){
			final var event = futures.poll();
			if(event instanceof FutureValueMetricEvent){
				((FutureValueMetricEvent) event).generateValue();
			}
			listeners.parallelStream().forEach(l -> l.onEvent(event));
		}
	}
	
	/**
	 * Remove a listener.
	 *
	 * @param listener The listener to remove.
	 */
	public void removeListener(final MetricEventListener listener){
		listeners.remove(listener);
	}
	
	@Override
	public void close(){
		if(!isClosed()){
			this.closed = true;
			clear();
			listeners.forEach(l -> {
				try{
					l.close();
				}
				catch(final IOException e){
					e.printStackTrace();
				}
			});
		}
	}
	
	/**
	 * Tell if the dispatcher have been closed.
	 *
	 * @return The closed status of the dispatcher. True is closed, false otherwise.
	 */
	private boolean isClosed(){
		return this.closed;
	}
	
	/**
	 * Clear the pending events.
	 */
	public void clear(){
		futures.clear();
	}
}
