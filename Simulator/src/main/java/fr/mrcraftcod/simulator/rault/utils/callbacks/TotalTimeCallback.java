package fr.mrcraftcod.simulator.rault.utils.callbacks;

import com.google.ortools.constraintsolver.NodeEvaluator2;
import fr.mrcraftcod.simulator.chargers.Charger;
import fr.mrcraftcod.simulator.rault.routing.ChargingStop;
import java.util.LinkedList;

/**
 * Node evaluator for time costs.
 * <p>
 * Created by mrcraftcod (MrCraftCod - zerderr@gmail.com) on 2019-02-27.
 *
 * @author Thomas Couchoud
 * @since 2019-02-27
 */
public class TotalTimeCallback extends NodeEvaluator2{
	private final ChargingTimeCallback serviceTimeCallback;
	private final TravelTimeCallback travelTimeCallback;
	
	/**
	 * Constructor.
	 *
	 * @param charger The charger.
	 * @param stops   The stops to go through.
	 */
	public TotalTimeCallback(final Charger charger, final LinkedList<ChargingStop> stops){
		this.serviceTimeCallback = new ChargingTimeCallback(stops);
		this.travelTimeCallback = new TravelTimeCallback(charger, stops);
	}
	
	@Override
	public long run(final int firstIndex, final int secondIndex){
		final var sT = this.serviceTimeCallback.run(firstIndex, secondIndex);
		final var tT = this.travelTimeCallback.run(firstIndex, secondIndex);
		return sT + tT;
	}
}
