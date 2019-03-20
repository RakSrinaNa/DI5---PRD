package fr.mrcraftcod.simulator.metrics;

/**
 * A metric event with a value that will be retrieved when it is fired.
 * <p>
 * Created by mrcraftcod (MrCraftCod - zerderr@gmail.com) on 2019-03-20.
 *
 * @author Thomas Couchoud
 * @since 2019-03-20
 */
public interface FutureValueMetricEvent<T>{
	/**
	 * Generate the value.
	 */
	void generateValue();
}
