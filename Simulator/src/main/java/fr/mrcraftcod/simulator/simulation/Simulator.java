package fr.mrcraftcod.simulator.simulation;

import fr.mrcraftcod.simulator.Environment;
import fr.mrcraftcod.simulator.metrics.MetricEventDispatcher;
import fr.mrcraftcod.simulator.simulation.events.EndEvent;
import fr.mrcraftcod.simulator.simulation.events.StartEvent;
import fr.mrcraftcod.simulator.utils.UnreadableQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Simulator.
 * <p>
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-11-07.
 *
 * @author Thomas Couchoud
 */
public class Simulator implements Runnable{
	private static final Logger LOGGER = LoggerFactory.getLogger(Simulator.class);
	private static final PriorityQueue<SimulationEvent> events = new PriorityQueue<>();
	private static final UnreadableQueue<SimulationEvent> unreadableQueue = new UnreadableQueue<>(events);
	private final Environment environment;
	private static Simulator INSTANCE = null;
	private static double currentTime = 0;
	private long delay = 0;
	private boolean running;
	
	/**
	 * Constructor.
	 *
	 * @param environment The environment.
	 */
	private Simulator(final Environment environment){
		this.environment = environment;
		this.running = true;
	}
	
	/**
	 * Get the simulator. If none exists, a new one is created.
	 *
	 * @param environment The environment.
	 *
	 * @return The simulator.
	 */
	public static Simulator getSimulator(final Environment environment){
		return Objects.isNull(INSTANCE) ? (INSTANCE = new Simulator(environment)) : INSTANCE;
	}
	
	public static void removeAllEventsOfClass(final Class<? extends SimulationEvent> eventClass){
		events.removeIf(elem -> Objects.equals(elem.getClass(), eventClass));
	}
	
	/**
	 * Stop the simulation by clearing the queue.
	 */
	public void stop(){
		LOGGER.info("Stopping, clearing queue");
		this.getEvents().clear();
	}
	
	@Override
	public void run(){
		LOGGER.info("Starting simulator");
		events.add(new StartEvent(0));
		events.add(new EndEvent(environment.getEnd()));
		SimulationEvent event;
		while((event = getEvents().poll()) != null){
			while(!running){
				try{
					Thread.sleep(1000);
				}
				catch(InterruptedException ignored){
				}
			}
			LOGGER.info("Executing event {} at time {}", event.getClass().getSimpleName(), event.getTime());
			currentTime = event.getTime();
			try{
				event.accept(this.getEnvironment());
			}
			catch(final Exception e){
				LOGGER.error("Error in event {}", event, e);
			}
			MetricEventDispatcher.fire();
			if(delay > 0){
				try{
					Thread.sleep(delay);
				}
				catch(final InterruptedException ignored){
				}
			}
		}
		LOGGER.info("Simulation ended");
	}
	
	/**
	 * Get the queue of events.
	 *
	 * @return The events.
	 */
	private Queue<SimulationEvent> getEvents(){
		return events;
	}
	
	public void setRunning(boolean status){
		this.running = status;
	}
	
	/**
	 * Get the environment.
	 *
	 * @return The environment.
	 */
	private Environment getEnvironment(){
		return this.environment;
	}
	
	/**
	 * Get the current time of the simulation.
	 *
	 * @return The current time.
	 */
	public static double getCurrentTime(){
		return currentTime;
	}
	
	/**
	 * Get a queue of events to add new ones.
	 *
	 * @return An unreadable queue of the events.
	 */
	public static UnreadableQueue<SimulationEvent> getUnreadableQueue(){
		return unreadableQueue;
	}
	
	public void setDelay(final long delay){
		this.delay = delay;
	}
}
