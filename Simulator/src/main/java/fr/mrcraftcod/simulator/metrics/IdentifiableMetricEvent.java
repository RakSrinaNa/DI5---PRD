package fr.mrcraftcod.simulator.metrics;

import fr.mrcraftcod.simulator.Environment;
import fr.mrcraftcod.simulator.utils.Identifiable;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-11-22.
 *
 * @author Thomas Couchoud
 * @since 2018-11-22
 */
public abstract class IdentifiableMetricEvent<T, S extends Identifiable> extends ValueMetricEvent<T>{
	private final S element;
	
	protected IdentifiableMetricEvent(final Environment environment, final double time, final S element, final T newValue){
		super(environment, time, newValue);
		this.element = element;
	}
	
	protected IdentifiableMetricEvent(final Environment environment, final double time, final S element, final T newValue, final int priority){
		super(environment, time, newValue, priority);
		this.element = element;
	}
	
	public S getElement(){
		return element;
	}
}
