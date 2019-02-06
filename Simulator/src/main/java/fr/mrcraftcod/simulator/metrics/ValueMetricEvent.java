package fr.mrcraftcod.simulator.metrics;

import fr.mrcraftcod.simulator.Environment;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-11-22.
 *
 * @author Thomas Couchoud
 * @since 2018-11-22
 */
public abstract class ValueMetricEvent<T> extends MetricEvent{
	private final T newValue;
	
	protected ValueMetricEvent(final Environment environment, final double time, final T newValue){
		super(environment, time);
		this.newValue = newValue;
	}
	
	protected ValueMetricEvent(final Environment environment, final double time, final T newValue, final int priority){
		super(environment, time, priority);
		this.newValue = newValue;
	}
	
	public T getNewValue(){
		return newValue;
	}
}
