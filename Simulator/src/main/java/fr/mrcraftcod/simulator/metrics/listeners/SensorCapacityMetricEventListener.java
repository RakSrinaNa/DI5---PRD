package fr.mrcraftcod.simulator.metrics.listeners;

import fr.mrcraftcod.simulator.metrics.MetricEvent;
import fr.mrcraftcod.simulator.metrics.MetricEventDispatcher;
import fr.mrcraftcod.simulator.metrics.MetricEventListener;
import fr.mrcraftcod.simulator.metrics.events.SensorCapacityMetricEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Optional;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-11-22.
 *
 * @author Thomas Couchoud
 * @since 2018-11-22
 */
public class SensorCapacityMetricEventListener implements MetricEventListener{
	private static final Logger LOGGER = LoggerFactory.getLogger(SensorCapacityMetricEventListener.class);
	private final HashMap<Integer, PrintWriter> outputFiles;
	
	static
	{
		MetricEventDispatcher.addListener(new SensorCapacityMetricEventListener());
	}
	
	public SensorCapacityMetricEventListener(){outputFiles = new HashMap<>();}
	
	@Override
	public void onEvent(final MetricEvent event){
		if(event instanceof SensorCapacityMetricEvent){
			final var evt = (SensorCapacityMetricEvent) event;
			Optional.ofNullable(outputFiles.computeIfAbsent(evt.getElement().getID(), (id) -> {
				final var path = MetricEvent.getMetricSaveFolder().resolve("sensor").resolve(evt.getElement().getUniqueIdentifier() + ".csv");
				try{
					//noinspection ResultOfMethodCallIgnored
					path.getParent().toFile().mkdirs();
					final var pw = new PrintWriter(new FileOutputStream(path.toFile()));
					pw.println("time,currentCapacity");
					return pw;
				}
				catch(final FileNotFoundException e){
					LOGGER.error("Failed to create output file {}", path, e);
				}
				return null;
			})).ifPresent(pw -> {
				pw.print(event.getTime());
				pw.print(",");
				pw.print(((SensorCapacityMetricEvent) event).getNewValue());
				pw.println();
				pw.flush();
			});
		}
	}
	
	@Override
	public void onEnd(){
		outputFiles.values().forEach(PrintWriter::close);
	}
}
