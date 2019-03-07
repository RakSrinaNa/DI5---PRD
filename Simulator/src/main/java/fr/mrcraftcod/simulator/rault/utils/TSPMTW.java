package fr.mrcraftcod.simulator.rault.utils;

import com.google.ortools.constraintsolver.Assignment;
import com.google.ortools.constraintsolver.FirstSolutionStrategy;
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
public class TSPMTW extends TourSolver{
	private static final Logger LOGGER = LoggerFactory.getLogger(TSPMTW.class);
	private static final long MAX_DIMENSION_RANGE = 24 * 3600;
	
	/**
	 * Constructor.
	 *
	 * @param environment The environment.
	 * @param tour        The tour to route.
	 */
	public TSPMTW(final Environment environment, final ChargerTour tour){
		super(environment, tour);
	}
	
	@SuppressWarnings("Duplicates")
	@Override
	public Optional<Pair<List<Integer>, List<Double>>> solve(){
		LOGGER.debug("Creating model with {} stops", getTour().getStops().size());
		
		final var routing = new RoutingModel(getTour().getStops().size() + 1, 1, 0);
		
		//Setup distances
		final var distanceCallback = new DistanceCallback(getTour().getCharger().getPosition(), getTour().getStops());
		routing.setArcCostEvaluatorOfAllVehicles(distanceCallback);
		
		//Setup charger
		final var totalTimeCallback = new TotalTimeCallback(getTour().getCharger(), getTour().getStops());
		routing.addDimension(totalTimeCallback, MAX_DIMENSION_RANGE, MAX_DIMENSION_RANGE, true, "time");
		routing.AddVariableMinimizedByFinalizer(routing.cumulVar(routing.end(0), "time"));
		
		//Setup sensors
		final var timeDimension = routing.getMutableDimension("time");
		timeDimension.setGlobalSpanCostCoefficient(1);
		for(var i = 0; i < this.getTour().getStops().size(); i++){
			final var stopIndex = routing.nodeToIndex(i);
			final var timeWindowCumulVar = timeDimension.cumulVar(stopIndex);
			timeWindowCumulVar.setRange(0, MAX_DIMENSION_RANGE);
			for(final var occupation : getTour().getStops().get(i).getForbiddenTimes()){
				timeWindowCumulVar.removeInterval((long) occupation.getLeft().doubleValue(), (long) occupation.getRight().doubleValue());
				LOGGER.info("Node {} forbidden from {}, to {}", stopIndex, occupation.getLeft(), occupation.getRight());
			}
		}
		
		// Solving
		final var parameters = RoutingSearchParameters.newBuilder().mergeFrom(RoutingModel.defaultSearchParameters()).setFirstSolutionStrategy(FirstSolutionStrategy.Value.PATH_CHEAPEST_ARC).setTimeLimitMs(getTimeout() * 1000L).setLnsTimeLimitMs(getTimeout() * 1000L).build();
		
		LOGGER.info("Starting TSPMTW");
		Assignment solution = null;
		try{
			solution = routing.solveWithParameters(parameters);
		}
		catch(final Throwable e){
			LOGGER.error("Error running TSPMTW", e);
		}
		LOGGER.info("TSPMTW done");
		
		if(solution != null){
			LOGGER.debug("TSPMTW cost for tour of {}: {}", getTour().getCharger().getUniqueIdentifier(), solution.objectiveValue());
			
			final var newOrder = new ArrayList<Integer>();
			final var arrivalTimes = new ArrayList<Double>();
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
			LOGGER.warn("TSPMTW found no solution for {}, keeping old order", getTour());
		}
		return Optional.empty();
	}
	
	@Override
	protected String getSolverName(){
		return "TSPMTW";
	}
	
	@Override
	public int getTimeout(){
		return 30;
	}
}
