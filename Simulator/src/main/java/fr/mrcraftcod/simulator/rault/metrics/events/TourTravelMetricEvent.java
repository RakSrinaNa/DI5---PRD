package fr.mrcraftcod.simulator.rault.metrics.events;

import fr.mrcraftcod.simulator.Environment;
import fr.mrcraftcod.simulator.chargers.Charger;
import fr.mrcraftcod.simulator.metrics.IdentifiableMetricEvent;
import fr.mrcraftcod.simulator.positions.Position;
import fr.mrcraftcod.simulator.rault.routing.ChargingStop;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

/**
 * Fired when a charger starts traveling during a tour.
 * <p>
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-11-22.
 *
 * @author Thomas Couchoud
 * @since 2018-11-22
 */
public class TourTravelMetricEvent extends IdentifiableMetricEvent<Pair<Position, ChargingStop>, Charger>{
	/**
	 * Constructor.
	 *
	 * @param environment              The environment.
	 * @param time                     The time of the event.
	 * @param charger                  The charger.
	 * @param positionChargingStopPair The current position of the charger and its destination.
	 */
	public TourTravelMetricEvent(final Environment environment, final double time, @NotNull final Charger charger, @NotNull final Pair<Position, ChargingStop> positionChargingStopPair){
		super(environment, time, charger, positionChargingStopPair);
	}
}
