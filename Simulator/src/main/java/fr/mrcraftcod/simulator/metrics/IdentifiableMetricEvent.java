package fr.mrcraftcod.simulator.metrics;

import fr.mrcraftcod.simulator.Environment;
import fr.mrcraftcod.simulator.utils.Identifiable;

/**
 * A {@link ValueMetricEvent} that targets an identifiable.
 * <p>
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-11-22.
 *
 * @author Thomas Couchoud
 * @since 2018-11-22
 */
public abstract class IdentifiableMetricEvent<T, S extends Identifiable> extends ValueMetricEvent<T>{
	private final S element;
	
	/**
	 * Constructor.
	 *
	 * @param environment The environment.
	 * @param time        The time of the event.
	 * @param element     The identifiable targeted.
	 * @param newValue    The new value.
	 */
	protected IdentifiableMetricEvent(final Environment environment, final double time, final S element, final T newValue){
		super(environment, time, newValue);
		this.element = element;
	}
	
	/**
	 * Constructor.
	 *
	 * @param environment The environment.
	 * @param time        The time of the event.
	 * @param element     The identifiable targeted.
	 * @param newValue    The new value.
	 * @param priority    The priority of the event (lower values will be executed first).
	 */
	protected IdentifiableMetricEvent(final Environment environment, final double time, final S element, final T newValue, final int priority){
		super(environment, time, newValue, priority);
		this.element = element;
	}
	
	/**
	 * Get the targeted element.
	 *
	 * @return The element.
	 */
	public S getElement(){
		return element;
	}
}
