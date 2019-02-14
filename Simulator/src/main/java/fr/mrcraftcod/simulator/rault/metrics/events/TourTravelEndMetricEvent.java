package fr.mrcraftcod.simulator.rault.metrics.events;

import fr.mrcraftcod.simulator.Environment;
import fr.mrcraftcod.simulator.chargers.Charger;
import fr.mrcraftcod.simulator.metrics.IdentifiableMetricEvent;
import fr.mrcraftcod.simulator.rault.routing.ChargingStop;
import org.jetbrains.annotations.NotNull;

/**
 * Fired when a travel during a tour ends.
 *
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-11-22.
 *
 * @author Thomas Couchoud
 * @since 2018-11-22
 */
public class TourTravelEndMetricEvent extends IdentifiableMetricEvent<ChargingStop, Charger>{
	/**
	 * Constructor.
	 *
	 * @param environment  The environment.
	 * @param time         The time of the event.
	 * @param charger      The charger.
	 * @param chargingStop The charging stop.
	 */
	public TourTravelEndMetricEvent(final Environment environment, final double time, @NotNull final Charger charger, @NotNull final ChargingStop chargingStop){
		super(environment, time, charger, chargingStop, 10);
	}
}
