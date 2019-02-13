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
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-11-22.
 *
 * @author Thomas Couchoud
 * @since 2018-11-22
 */
public class DepletionMetricEventListener implements MetricEventListener{
	private static final Logger LOGGER = LoggerFactory.getLogger(DepletionMetricEventListener.class);
	private final PrintWriter outputFile;
	private final HashMap<Sensor, Double> totals;
	private double lastTime = 0D;
	
	public DepletionMetricEventListener(final Environment environment) throws FileNotFoundException{
		totals = new HashMap<>();
		final var path = MetricEvent.getMetricSaveFolder(environment).resolve("sensor").resolve("depletion.csv");
		path.getParent().toFile().mkdirs();
		outputFile = new PrintWriter(new FileOutputStream(path.toFile()));
		outputFile.print("time");
		outputFile.print(CSV_SEPARATOR);
		outputFile.println(environment.getElements(Sensor.class).stream().sorted().map(Identifiable::getUniqueIdentifier).collect(Collectors.joining(CSV_SEPARATOR)));
		outputFile.flush();
	}
	
	@Override
	public void onEvent(final MetricEvent event){
		if(event instanceof SensorsCapacityMetricEvent){
			final var evt = (SensorsCapacityMetricEvent) event;
			outputFile.print(evt.getTime());
			outputFile.print(CSV_SEPARATOR);
			outputFile.println(event.getEnvironment().getElements(Sensor.class).stream().sorted().map(s -> s.getCurrentCapacity() > 0 ? "0" : "1").collect(Collectors.joining(CSV_SEPARATOR)));
			outputFile.flush();
			event.getEnvironment().getElements(Sensor.class).forEach(s -> totals.put(s, totals.getOrDefault(s, 0D) + (event.getTime() - lastTime) * (s.getCurrentCapacity() > 0 ? 0 : 1)));
			lastTime = event.getTime();
		}
	}
	
	@Override
	public void close(){
		outputFile.print("total");
		outputFile.print(CSV_SEPARATOR);
		outputFile.println(totals.entrySet().stream().sorted(Comparator.comparing(Map.Entry::getKey)).map(s -> "" + s.getValue()).collect(Collectors.joining(CSV_SEPARATOR)));
		outputFile.close();
	}
}
