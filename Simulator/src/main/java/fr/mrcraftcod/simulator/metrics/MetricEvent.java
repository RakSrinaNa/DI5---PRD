package fr.mrcraftcod.simulator.metrics;

import fr.mrcraftcod.simulator.Environment;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-11-22.
 *
 * @author Thomas Couchoud
 * @since 2018-11-22
 */
public abstract class MetricEvent implements Comparable<MetricEvent>{
	private static final Path METRIC_SAVE_FOLDER = Paths.get(new File(".").toURI()).resolve("metrics").resolve("" + System.currentTimeMillis());
	
	private final Environment environment;
	private final double time;
	private final int priority;
	
	protected MetricEvent(final Environment environment, final double time){
		this(environment, time, Integer.MAX_VALUE);
	}
	
	protected MetricEvent(final Environment environment, final double time, final int priority){
		this.environment = environment;
		this.time = time;
		this.priority = priority;
	}
	
	@Override
	public int compareTo(@NotNull final MetricEvent o){
		final var timeDiff = Double.compare(getTime(), o.getTime());
		return timeDiff == 0D ? Integer.compare(getPriority(), o.getPriority()) : timeDiff;
	}
	
	private int getPriority(){
		return this.priority;
	}
	
	public static Path getMetricSaveFolder(){
		return METRIC_SAVE_FOLDER;
	}
	
	public double getTime(){
		return time;
	}
	
	public Environment getEnvironment(){
		return environment;
	}
}
