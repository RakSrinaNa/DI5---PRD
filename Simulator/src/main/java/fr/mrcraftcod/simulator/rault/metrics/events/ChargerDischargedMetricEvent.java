package fr.mrcraftcod.simulator.rault.metrics.events;

import fr.mrcraftcod.simulator.Environment;
import fr.mrcraftcod.simulator.chargers.Charger;
import fr.mrcraftcod.simulator.metrics.IdentifiableMetricEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Fired when a sensor is charged.
 * <p>
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-11-22.
 *
 * @author Thomas Couchoud
 * @since 2018-11-22
 */
public class ChargerDischargedMetricEvent extends IdentifiableMetricEvent<Double, Charger>{
	/**
	 * Constructor.
	 *
	 * @param environment The environment.
	 * @param time        The time of the event.
	 * @param charger     The charger.
	 * @param newValue    The capacity of the charger.
	 */
	public ChargerDischargedMetricEvent(final Environment environment, final double time, @NotNull final Charger charger, final double newValue){
		super(environment, time, charger, newValue, 10);
	}
}
