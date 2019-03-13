package fr.mrcraftcod.simulator.rault.utils.callbacks;

import fr.mrcraftcod.simulator.chargers.Charger;
import fr.mrcraftcod.simulator.rault.routing.ChargingStop;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.stream.Collectors;

/**
 * Represents the travel time between nodes.
 * <p>
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2019-01-09.
 *
 * @author Thomas Couchoud
 * @since 2019-01-09
 */
@SuppressWarnings("WeakerAccess")
public class TravelTimeCallback extends Callbacks{
	private final long[][] time;
	
	/**
	 * Constructor.
	 *
	 * @param charger The charger.
	 * @param stops   The stops to go through.
	 */
	public TravelTimeCallback(final Charger charger, final LinkedList<ChargingStop> stops){
		final var positions = stops.stream().map(s -> s.getStopLocation().getPosition()).collect(Collectors.toCollection(ArrayList::new));
		positions.add(0, charger.getPosition());
		final var size = positions.size();
		this.time = new long[size][size];
		for(var i = 0; i < size; i++){
			this.time[i][i] = 0;
			for(var j = i + 1; j < size; j++){
				final var travelTime = (long) (COST_MULTIPLICAND * charger.getTravelTime(positions.get(i).distanceTo(positions.get(j))));
				this.time[i][j] = travelTime;
				this.time[j][i] = travelTime;
			}
		}
	}
	
	@Override
	public long run(final int firstIndex, final int secondIndex){
		return time[firstIndex][secondIndex];
	}
}
