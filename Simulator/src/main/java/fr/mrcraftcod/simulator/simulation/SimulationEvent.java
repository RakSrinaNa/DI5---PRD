package fr.mrcraftcod.simulator.simulation;

import fr.mrcraftcod.simulator.Environment;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jetbrains.annotations.NotNull;
import java.util.function.Consumer;

/**
 * Represents an event of the simulation.
 * <p>
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-11-07.
 *
 * @author Thomas Couchoud
 */
public abstract class SimulationEvent implements Comparable<SimulationEvent>, Consumer<Environment>{
	private final double time;
	private final int priority;
	
	/**
	 * Constructor.
	 *
	 * @param time The time of the event.
	 */
	protected SimulationEvent(final double time){
		this(time, Integer.MAX_VALUE);
	}
	
	/**
	 * Constructor.
	 *
	 * @param time     The time of the event.
	 * @param priority The priority of the event (most negative have the most priority).
	 */
	protected SimulationEvent(final double time, final int priority){
		this.time = time;
		this.priority = priority;
	}
	
	@Override
	public int compareTo(@NotNull final SimulationEvent o){
		final var timeDiff = Double.compare(getTime(), o.getTime());
		return timeDiff == 0D ? Integer.compare(getPriority(), o.getPriority()) : timeDiff;
	}
	
	/**
	 * Get the time of the event.
	 *
	 * @return The time of the event.
	 */
	public double getTime(){
		return time;
	}
	
	/**
	 * Get the priority.
	 *
	 * @return The priority.
	 */
	private int getPriority(){
		return this.priority;
	}
	
	@Override
	public String toString(){
		return new ToStringBuilder(this).append("time", time).append("priority", priority).toString();
	}
}
