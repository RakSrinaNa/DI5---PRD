package fr.mrcraftcod.simulator.rault.utils;

import com.google.ortools.constraintsolver.FirstSolutionStrategy;
import com.google.ortools.constraintsolver.NodeEvaluator2;
import com.google.ortools.constraintsolver.RoutingModel;
import com.google.ortools.constraintsolver.RoutingSearchParameters;
import fr.mrcraftcod.simulator.chargers.Charger;
import fr.mrcraftcod.simulator.positions.Position;
import fr.mrcraftcod.simulator.rault.routing.ChargerTour;
import fr.mrcraftcod.simulator.rault.routing.ChargingStop;
import fr.mrcraftcod.simulator.rault.routing.StopLocation;
import fr.mrcraftcod.simulator.rault.utils.callbacks.ChargingTimeCallback;
import fr.mrcraftcod.simulator.rault.utils.callbacks.DistanceCallback;
import fr.mrcraftcod.simulator.rault.utils.callbacks.TravelTimeCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2019-01-09.
 *
 * @author Thomas Couchoud
 * @since 2019-01-09
 */
public class TSPMTW{
	private static final Logger LOGGER = LoggerFactory.getLogger(TSPMTW.class);
	private final ChargerTour tour;
	
	/**
	 * Node evaluator for time costs.
	 */
	class TotalTimeCallback extends NodeEvaluator2{
		private final ChargingTimeCallback serviceTimeCallback;
		private final TravelTimeCallback travelTimeCallback;
		
		/**
		 * Constructor.
		 *
		 * @param charger The charger.
		 * @param stops   The stops to go through.
		 */
		TotalTimeCallback(final Charger charger, final LinkedList<ChargingStop> stops){
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
	
	public TSPMTW(final ChargerTour tour){
		this.tour = tour;
	}
	
	public static void main(final String[] args){
		System.loadLibrary("jniortools");
		
		final var charger = new Charger(Double.MAX_VALUE, Double.MAX_VALUE, 1, 1, 1);
		charger.setPosition(new Position(-2, 1));
		final var tour = new ChargerTour(charger);
		tour.addStop(new ChargingStop(new StopLocation(new Position(-1, -1)), 10));
		tour.addStop(new ChargingStop(new StopLocation(new Position(-1, 1)), 10));
		tour.addStop(new ChargingStop(new StopLocation(new Position(1, -1)), 10));
		tour.addStop(new ChargingStop(new StopLocation(new Position(1, 1)), 10));
		new TSPMTW(tour).solve();
	}
	
	public void solve(){
		LOGGER.debug("Creating model with {} stops", tour.getStops().size());
		
		final var routing = new RoutingModel(tour.getStops().size() + 1, 1, 0);
		
		//Setup distances
		final var distanceCallback = new DistanceCallback(tour.getCharger().getPosition(), tour.getStops());
		routing.setArcCostEvaluatorOfAllVehicles(distanceCallback);
		
		//Setup charger
		final var totalTimeCallback = new TotalTimeCallback(tour.getCharger(), tour.getStops());
		routing.addDimension(totalTimeCallback, Long.MAX_VALUE, Long.MAX_VALUE, true, "time");
		routing.AddVariableMinimizedByFinalizer(routing.cumulVar(routing.end(0), "time"));
		
		//Setup sensors
		final var timeDimension = routing.getDimensionOrDie("time");
		timeDimension.setGlobalSpanCostCoefficient(100000);
		for(var i = 0; i < this.tour.getStops().size(); i++){
			final var stopIndex = routing.NodeToIndex(i);
			final var timeWindowCumulVar = timeDimension.cumulVar(stopIndex);
			timeWindowCumulVar.setRange(0, Long.MAX_VALUE);
			for(final var occupation : tour.getStops().get(i).getForbiddenTimes()){
				timeWindowCumulVar.removeInterval((long) occupation.getLeft().doubleValue(), (long) occupation.getRight().doubleValue());
				LOGGER.warn("Node {} forbidden from {}, to {}", stopIndex, occupation.getLeft(), occupation.getRight());
			}
		}
		
		// Solving
		final var parameters = RoutingSearchParameters.newBuilder().mergeFrom(RoutingModel.defaultSearchParameters()).setFirstSolutionStrategy(FirstSolutionStrategy.Value.PATH_CHEAPEST_ARC).build();
		
		LOGGER.info("Starting TSPMTW");
		final var solution = routing.solveWithParameters(parameters);
		
		if(solution != null){
			LOGGER.debug("TSPMTW cost for tour of {}: {}", tour.getCharger().getUniqueIdentifier(), solution.objectiveValue());
			
			final var newOrder = new ArrayList<Integer>();
			for(var node = routing.start(0); !routing.isEnd(node); node = solution.value(routing.nextVar(node))){
				if(node > 0){
					newOrder.add((int) (node - 1));
				}
			}
			tour.newOrder(newOrder);
		}
		else{
			LOGGER.warn("TSPMTW found no solution for {}, keeping old order", tour);
		}
	}
}
