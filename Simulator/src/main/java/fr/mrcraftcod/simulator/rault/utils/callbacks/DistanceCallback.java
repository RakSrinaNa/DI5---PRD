package fr.mrcraftcod.simulator.rault.utils.callbacks;

import fr.mrcraftcod.simulator.positions.Position;
import fr.mrcraftcod.simulator.rault.routing.ChargingStop;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.stream.Collectors;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2019-01-09.
 *
 * @author Thomas Couchoud
 * @since 2019-01-09
 */
public class DistanceCallback extends Callbacks{
	private final long[][] dist;
	
	/**
	 * Constructor.
	 *
	 * @param basePosition The position of the base.
	 * @param stops        The stops to go through.
	 */
	public DistanceCallback(final Position basePosition, final LinkedList<ChargingStop> stops){
		final var positions = stops.stream().map(s -> s.getStopLocation().getPosition()).collect(Collectors.toCollection(ArrayList::new));
		positions.add(0, basePosition);
		final var size = positions.size();
		this.dist = new long[size][size];
		for(var i = 0; i < size; i++){
			this.dist[i][i] = 0;
			for(var j = i + 1; j < size; j++){
				final var interDist = (long) (COST_MULTIPLICAND * positions.get(i).distanceTo(positions.get(j)));
				this.dist[i][j] = interDist;
				this.dist[j][i] = interDist;
			}
		}
	}
	
	@Override
	public long run(final int firstIndex, final int secondIndex){
		return dist[firstIndex][secondIndex];
	}
}
