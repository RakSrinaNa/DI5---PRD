package fr.mrcraftcod.simulator.rault.metrics.events;

import fr.mrcraftcod.simulator.Environment;
import fr.mrcraftcod.simulator.chargers.Charger;
import fr.mrcraftcod.simulator.metrics.IdentifiableMetricEvent;
import fr.mrcraftcod.simulator.positions.Position;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-11-22.
 *
 * @author Thomas Couchoud
 * @since 2018-11-22
 */
public class TourTravelBaseMetricEvent extends IdentifiableMetricEvent<Pair<Position, Position>, Charger>{
	public TourTravelBaseMetricEvent(final Environment environment, final double time, @NotNull final Charger charger, @NotNull final Pair<Position, Position> chargingStop){
		super(environment, time, charger, chargingStop);
	}
}
