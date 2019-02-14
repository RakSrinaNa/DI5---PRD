package fr.mrcraftcod.simulator.rault.metrics.events;

import fr.mrcraftcod.simulator.Environment;
import fr.mrcraftcod.simulator.metrics.IdentifiableMetricEvent;
import fr.mrcraftcod.simulator.sensors.Sensor;
import org.jetbrains.annotations.NotNull;

/**
 * Fired when a Lc request have been made.
 *
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-11-22.
 *
 * @author Thomas Couchoud
 * @since 2018-11-22
 */
public class LcRequestMetricEvent extends IdentifiableMetricEvent<Void, Sensor>{
	/**
	 * Constructor.
	 *
	 * @param environment The environment.
	 * @param time        The time of the event.
	 * @param sensor      The sensor making the request.
	 */
	public LcRequestMetricEvent(final Environment environment, final double time, @NotNull final Sensor sensor){
		super(environment, time, sensor, null);
	}
}
