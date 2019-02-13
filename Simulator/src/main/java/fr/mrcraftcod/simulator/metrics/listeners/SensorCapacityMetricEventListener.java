package fr.mrcraftcod.simulator.metrics.listeners;

import fr.mrcraftcod.simulator.Environment;
import fr.mrcraftcod.simulator.metrics.MetricEvent;
import fr.mrcraftcod.simulator.metrics.MetricEventListener;
import fr.mrcraftcod.simulator.metrics.events.SensorsCapacityMetricEvent;
import fr.mrcraftcod.simulator.sensors.Sensor;
import fr.mrcraftcod.simulator.utils.Identifiable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Comparator;
import java.util.stream.Collectors;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-11-22.
 *
 * @author Thomas Couchoud
 * @since 2018-11-22
 */
@SuppressWarnings("unused")
public class SensorCapacityMetricEventListener implements MetricEventListener{
	private static final Logger LOGGER = LoggerFactory.getLogger(SensorCapacityMetricEventListener.class);
	private final PrintWriter outputFile;
	
	public SensorCapacityMetricEventListener(final Environment environment) throws FileNotFoundException{
		final var path = MetricEvent.getMetricSaveFolder(environment).resolve("sensor").resolve("capacity.csv");
		if(!path.getParent().toFile().mkdirs())
			LOGGER.error("Couldn't create folder {}", path.getParent().toFile());
		outputFile = new PrintWriter(new FileOutputStream(path.toFile()));
		outputFile.print("time");
		outputFile.print(CSV_SEPARATOR);
		outputFile.println(environment.getElements(Sensor.class).stream().map(Identifiable::getUniqueIdentifier).sorted().collect(Collectors.joining(CSV_SEPARATOR)));
		outputFile.flush();
	}
	
	@Override
	public void onEvent(final MetricEvent event){
		if(event instanceof SensorsCapacityMetricEvent){
			final var evt = (SensorsCapacityMetricEvent) event;
			outputFile.print(evt.getTime());
			outputFile.print(CSV_SEPARATOR);
			outputFile.println(event.getEnvironment().getElements(Sensor.class).stream().sorted(Comparator.comparing(Identifiable::getUniqueIdentifier)).map(s -> "" + s.getCurrentCapacity()).collect(Collectors.joining(CSV_SEPARATOR)));
			outputFile.flush();
		}
	}
	
	@Override
	public void close(){
		outputFile.close();
	}
}
