package fr.mrcraftcod.simulator.metrics;

import fr.mrcraftcod.simulator.Environment;

/**
 * A metric event with a value attached to it.
 * <p>
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-11-22.
 *
 * @author Thomas Couchoud
 * @since 2018-11-22
 */
@SuppressWarnings("WeakerAccess")
public abstract class ValueMetricEvent<T> extends MetricEvent{
	private T newValue;
	
	/**
	 * Constructor.
	 *
	 * @param environment The environment.
	 * @param time        The time of the event.
	 * @param newValue    The new value.
	 */
	protected ValueMetricEvent(final Environment environment, final double time, final T newValue){
		super(environment, time);
		this.newValue = newValue;
	}
	
	/**
	 * Constructor.
	 *
	 * @param environment The environment.
	 * @param time        The time of the event.
	 * @param newValue    The new value.
	 * @param priority    The priority of the event (lower values will be executed first).
	 */
	protected ValueMetricEvent(final Environment environment, final double time, final T newValue, final int priority){
		super(environment, time, priority);
		this.newValue = newValue;
	}
	
	/**
	 * Set the new value.
	 *
	 * @param value The value to set.
	 */
	protected void setValue(final T value){
		this.newValue = value;
	}
	
	/**
	 * Get the new value.
	 *
	 * @return The new value.
	 */
	public T getNewValue(){
		return newValue;
	}
}
