package fr.mrcraftcod.simulator.metrics;

import fr.mrcraftcod.simulator.utils.Identifiable;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-11-22.
 *
 * @author Thomas Couchoud
 * @since 2018-11-22
 */
public abstract class IdentifiableMetricEvent<T, S extends Identifiable> extends MetricEvent<T>{
	private final S element;
	
	protected IdentifiableMetricEvent(final double time, final S element, final T newValue){
		super(time, newValue);
		this.element = element;
	}
	
	public Identifiable getElement(){
		return element;
	}
}
