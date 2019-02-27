package fr.mrcraftcod.simulator.rault.utils;

import com.google.ortools.constraintsolver.FirstSolutionStrategy;
import com.google.ortools.constraintsolver.NodeEvaluator2;
import com.google.ortools.constraintsolver.RoutingModel;
import com.google.ortools.constraintsolver.RoutingSearchParameters;
import fr.mrcraftcod.simulator.Environment;
import fr.mrcraftcod.simulator.rault.routing.ChargerTour;
import fr.mrcraftcod.simulator.rault.utils.callbacks.Callbacks;
import fr.mrcraftcod.simulator.rault.utils.callbacks.DistanceCallback;
import fr.mrcraftcod.simulator.rault.utils.callbacks.TotalTimeCallback;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2019-01-09.
 *
 * @author Thomas Couchoud
 * @since 2019-01-09
 */
public class TSP extends TourSolver{
	private static final Logger LOGGER = LoggerFactory.getLogger(TSP.class);
	
	/**
	 * Constructor.
	 *
	 * @param environment The environment.
	 * @param tour        The tour to route.
	 */
	public TSP(final Environment environment, final ChargerTour tour){
		super(environment, tour);
	}
	
	@SuppressWarnings("Duplicates")
	@Override
	public Optional<Pair<List<Integer>, List<Double>>> solve(){
		//TODO: See https://github.com/google/or-tools/issues/885 should be fixed in ortools 7.0
		@SuppressWarnings("MismatchedQueryAndUpdateOfCollection") final List<NodeEvaluator2> callbacks = new ArrayList<>();
		
		final var routing = new RoutingModel(getTour().getStops().size() + 1, 1, 0);
		
		//Setup distances
		final var distanceCallback = new DistanceCallback(getTour().getCharger().getPosition(), getTour().getStops());
		routing.setArcCostEvaluatorOfAllVehicles(distanceCallback);
		
		//Setup charger
		final var totalTimeCallback = new TotalTimeCallback(getTour().getCharger(), getTour().getStops());
		routing.addDimension(totalTimeCallback, Long.MAX_VALUE, Long.MAX_VALUE, true, "time");
		
		final var search_parameters = RoutingSearchParameters.newBuilder().mergeFrom(RoutingModel.defaultSearchParameters()).setFirstSolutionStrategy(FirstSolutionStrategy.Value.PATH_CHEAPEST_ARC).setTimeLimitMs(getTimeout() * 1000L).build();
		
		callbacks.add(distanceCallback);
		callbacks.add(totalTimeCallback);
		
		LOGGER.info("Starting TSP");
		final var solution = routing.solveWithParameters(search_parameters);
		final var arrivalTimes = new ArrayList<Double>();
		if(solution != null){
			LOGGER.debug("TSP cost for tour of {}: {}", getTour().getCharger().getUniqueIdentifier(), solution.objectiveValue());
			final var newOrder = new ArrayList<Integer>();
			for(var node = routing.start(0); !routing.isEnd(node); node = solution.value(routing.nextVar(node))){
				final var time = routing.cumulVar(node, "time");
				if(node > 0){
					newOrder.add((int) (node - 1));
					arrivalTimes.add(getEnvironment().getSimulator().getCurrentTime() + (solution.min(time) / Callbacks.COST_MULTIPLICAND) - getTour().getStops().get((int) node - 1).getChargingTime());
				}
			}
			return Optional.of(MutablePair.of(newOrder, arrivalTimes));
		}
		else{
			LOGGER.warn("TSP found no solution for {}, keeping old order", getTour());
		}
		return Optional.empty();
	}
	
	@Override
	protected String getSolverName(){
		return "TSP";
	}
	
	@Override
	public int getTimeout(){
		return 30;
	}
}
