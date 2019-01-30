package fr.mrcraftcod.simulator.metrics;

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
public abstract class MetricEvent<T> implements Comparable<MetricEvent>{
	private static final Path METRIC_SAVE_FOLDER = Paths.get(new File(".").toURI()).resolve("metrics").resolve("" + System.currentTimeMillis());
	
	private final T newValue;
	private final double time;
	private final int priority;
	
	protected MetricEvent(final double time, final T newValue){
		this(time, newValue, Integer.MAX_VALUE);
	}
	
	protected MetricEvent(final double time, final T newValue, final int priority){
		this.time = time;
		this.newValue = newValue;
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
	
	public T getNewValue(){
		return newValue;
	}
	
	public static Path getMetricSaveFolder(){
		return METRIC_SAVE_FOLDER;
	}
	
	public double getTime(){
		return time;
	}
}
