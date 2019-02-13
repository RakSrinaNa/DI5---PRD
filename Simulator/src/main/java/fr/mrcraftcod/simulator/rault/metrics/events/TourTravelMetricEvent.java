package fr.mrcraftcod.simulator.rault.metrics.events;

import fr.mrcraftcod.simulator.Environment;
import fr.mrcraftcod.simulator.chargers.Charger;
import fr.mrcraftcod.simulator.metrics.IdentifiableMetricEvent;
import fr.mrcraftcod.simulator.positions.Position;
import fr.mrcraftcod.simulator.rault.routing.ChargingStop;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-11-22.
 *
 * @author Thomas Couchoud
 * @since 2018-11-22
 */
public class TourTravelMetricEvent extends IdentifiableMetricEvent<Pair<Position, ChargingStop>, Charger>{
	public TourTravelMetricEvent(final Environment environment, final double time, @NotNull final Charger charger, @NotNull final Pair<Position, ChargingStop> chargingStop){
		super(environment, time, charger, chargingStop);
	}
}
