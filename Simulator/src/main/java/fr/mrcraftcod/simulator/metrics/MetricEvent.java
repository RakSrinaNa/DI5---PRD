package fr.mrcraftcod.simulator.metrics;

import fr.mrcraftcod.simulator.Environment;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Represents a metric event.
 * <p>
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-11-22.
 *
 * @author Thomas Couchoud
 * @since 2018-11-22
 */
public abstract class MetricEvent implements Comparable<MetricEvent>{
	private static final Path METRIC_SAVE_FOLDER = Paths.get(new File(".").toURI()).resolve("metrics");
	
	private final Environment environment;
	private final double time;
	private final int priority;
	
	/**
	 * Constructor.
	 *
	 * @param environment The environment.
	 * @param time        The time of the event.
	 */
	protected MetricEvent(final Environment environment, final double time){
		this(environment, time, Integer.MAX_VALUE);
	}
	
	/**
	 * Constructor.
	 *
	 * @param environment The environment.
	 * @param time        The time of the event.
	 * @param priority    The priority of the event (lower values will be executed first).
	 */
	protected MetricEvent(final Environment environment, final double time, final int priority){
		this.environment = environment;
		this.time = time;
		this.priority = priority;
	}
	
	/**
	 * Get the folder to save the metric files in.
	 *
	 * @param environment The environment.
	 *
	 * @return The path to the folder.
	 */
	public static Path getMetricSaveFolder(final Environment environment){
		return getAllMetricSaveFolder(environment).resolve("" + environment.getCreationDate());
	}
	
	/**
	 * Get the folder of the metrics.
	 *
	 * @param environment The environment.
	 *
	 * @return The metric folder.
	 */
	public static Path getAllMetricSaveFolder(final Environment environment){
		return METRIC_SAVE_FOLDER.resolve(environment.getRunName());
	}
	
	@Override
	public int compareTo(@NotNull final MetricEvent o){
		final var timeDiff = Double.compare(getTime(), o.getTime());
		return timeDiff == 0D ? Integer.compare(getPriority(), o.getPriority()) : timeDiff;
	}
	
	/**
	 * Get the time of the event.
	 *
	 * @return The time of the event.
	 */
	public double getTime(){
		return time;
	}
	
	/**
	 * Get the priority.
	 *
	 * @return The priority;
	 */
	private int getPriority(){
		return this.priority;
	}
	
	/**
	 * Get the environment.
	 *
	 * @return The environment.
	 */
	public Environment getEnvironment(){
		return environment;
	}
}
