package fr.mrcraftcod.simulator.rault.utils;

import com.google.ortools.constraintsolver.FirstSolutionStrategy;
import com.google.ortools.constraintsolver.RoutingModel;
import com.google.ortools.constraintsolver.RoutingSearchParameters;
import fr.mrcraftcod.simulator.rault.routing.ChargerTour;
import fr.mrcraftcod.simulator.rault.utils.callbacks.DistanceCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2019-01-09.
 *
 * @author Thomas Couchoud
 * @since 2019-01-09
 */
public class TSP{
	private static final Logger LOGGER = LoggerFactory.getLogger(TSP.class);
	private final ChargerTour tour;
	
	/**
	 * Constructor.
	 *
	 * @param tour The tour to route.
	 */
	public TSP(final ChargerTour tour){this.tour = tour;}
	
	/**
	 * Solves the TSP for this tour.
	 */
	@SuppressWarnings("Duplicates")
	public void solve(){
		final var routing = new RoutingModel(tour.getStops().size() + 1, 1, 0);
		final var distances = new DistanceCallback(tour.getCharger().getPosition(), tour.getStops());
		routing.setArcCostEvaluatorOfAllVehicles(distances);
		
		final var search_parameters = RoutingSearchParameters.newBuilder().mergeFrom(RoutingModel.defaultSearchParameters()).setFirstSolutionStrategy(FirstSolutionStrategy.Value.PATH_CHEAPEST_ARC).build();
		final var solution = routing.solveWithParameters(search_parameters);
		if(solution != null){
			LOGGER.debug("TSP cost for tour of {}: {}", tour.getCharger().getUniqueIdentifier(), solution.objectiveValue());
			final var newOrder = new ArrayList<Integer>();
			for(var node = routing.start(0); !routing.isEnd(node); node = solution.value(routing.nextVar(node))){
				if(node > 0){
					newOrder.add((int) (node - 1));
				}
			}
			tour.newOrder(newOrder);
		}
		else{
			LOGGER.warn("TSP found no solution for {}, keeping old order", tour);
		}
	}
}
