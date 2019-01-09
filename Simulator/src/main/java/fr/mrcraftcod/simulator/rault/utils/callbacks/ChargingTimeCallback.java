package fr.mrcraftcod.simulator.rault.utils.callbacks;

import fr.mrcraftcod.simulator.rault.routing.ChargingStop;
import java.util.LinkedList;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2019-01-09.
 *
 * @author Thomas Couchoud
 * @since 2019-01-09
 */
public class ChargingTimeCallback extends Callbacks{
	private final long[] costs;
	
	/**
	 * Constructor.
	 *
	 * @param stops The stops to go through.
	 */
	public ChargingTimeCallback(final LinkedList<ChargingStop> stops){
		this.costs = new long[stops.size() + 1];
		this.costs[0] = 0;
		for(var i = 0; i < stops.size(); i++){
			this.costs[i] = (long) (COST_MULTIPLICAND * stops.get(i).getChargingTime());
		}
	}
	
	@Override
	public long run(final int firstIndex, final int secondIndex){
		return costs[firstIndex];
	}
}
