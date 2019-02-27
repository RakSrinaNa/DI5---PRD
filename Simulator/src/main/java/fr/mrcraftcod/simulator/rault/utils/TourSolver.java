package fr.mrcraftcod.simulator.rault.utils;

import fr.mrcraftcod.simulator.Environment;
import fr.mrcraftcod.simulator.rault.routing.ChargerTour;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;

/**
 * Represents a solver that can be run with a timeout.
 * <p>
 * Created by mrcraftcod (MrCraftCod - zerderr@gmail.com) on 2019-02-27.
 *
 * @author Thomas Couchoud
 * @since 2019-02-27
 */
@SuppressWarnings("WeakerAccess")
public abstract class TourSolver implements Callable<Optional<Pair<List<Integer>, List<Double>>>>{
	private static final Logger LOGGER = LoggerFactory.getLogger(TourSolver.class);
	
	private final Environment environment;
	private final ChargerTour tour;
	
	/**
	 * Constructor.
	 *
	 * @param environment The environment.
	 * @param tour        The tour to route.
	 */
	protected TourSolver(final Environment environment, final ChargerTour tour){
		super();
		this.environment = environment;
		this.tour = tour;
	}
	
	@Override
	public Optional<Pair<List<Integer>, List<Double>>> call(){
		final var startTime = System.currentTimeMillis();
		final var result = solve();
		LOGGER.debug("{} executed in {}", getSolverName(), Duration.ofMillis(System.currentTimeMillis() - startTime));
		return result;
	}
	
	/**
	 * Starts the solver.
	 *
	 * @return The result being a pair containing a list representing in which order to run the jobs and the times for each job.
	 */
	public abstract Optional<Pair<List<Integer>, List<Double>>> solve();
	
	/**
	 * Get the name of the solver.
	 *
	 * @return The name.
	 */
	protected abstract String getSolverName();
	
	/**
	 * Get the environment.
	 *
	 * @return Th environment.
	 */
	public Environment getEnvironment(){
		return environment;
	}
	
	/**
	 * Get the timeout before the solver should be stopped.
	 *
	 * @return The timeout in seconds.
	 */
	@SuppressWarnings("SameReturnValue")
	public abstract int getTimeout();
	
	/**
	 * Get the tour.
	 *
	 * @return The tour.
	 */
	public ChargerTour getTour(){
		return tour;
	}
}
