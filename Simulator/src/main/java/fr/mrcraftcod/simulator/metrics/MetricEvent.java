package fr.mrcraftcod.simulator.metrics;

import fr.mrcraftcod.simulator.utils.Identifiable;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-11-22.
 *
 * @author Thomas Couchoud
 * @since 2018-11-22
 */
public abstract class MetricEvent<T>{
	private static final Path METRIC_SAVE_FOLDER = Paths.get(new File(".").toURI()).resolve("metrics");
	
	private final Identifiable element;
	private final T newValue;
	private final double time;
	
	protected MetricEvent(final double time, final Identifiable element, final T newValue){
		this.time = time;
		this.element = element;
		this.newValue = newValue;
	}
	
	public Identifiable getElement(){
		return element;
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
