package fr.mrcraftcod.simulator.metrics.listeners;

import fr.mrcraftcod.simulator.Environment;
import fr.mrcraftcod.simulator.metrics.MetricEvent;
import fr.mrcraftcod.simulator.metrics.MetricEventListener;
import fr.mrcraftcod.simulator.metrics.events.SensorsCapacityMetricEvent;
import fr.mrcraftcod.simulator.rault.events.TourChargeEvent;
import fr.mrcraftcod.simulator.sensors.Sensor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-11-22.
 *
 * @author Thomas Couchoud
 * @since 2018-11-22
 */
@SuppressWarnings("unused")
public class TempDepletionMetricEventListener implements MetricEventListener{
	private static final Logger LOGGER = LoggerFactory.getLogger(TempDepletionMetricEventListener.class);
	private final HashMap<Sensor, Double> totals;
	private double lastTime = 0D;
	private boolean isClosed = false;
	
	public TempDepletionMetricEventListener(final Environment environment){
		totals = new HashMap<>();
	}
	
	@Override
	public void onEvent(final MetricEvent event){
		if(event instanceof SensorsCapacityMetricEvent){
			event.getEnvironment().getElements(Sensor.class).forEach(s -> totals.put(s, totals.getOrDefault(s, 0D) + (event.getTime() - lastTime) * (s.getCurrentCapacity() > 0 ? 0 : 1)));
			lastTime = event.getTime();
		}
	}
	
	@Override
	public void close(){
		if(!isClosed){
			try{
				//noinspection SpellCheckingInspection
				Files.write(Paths.get("deplet" + (TourChargeEvent.CHARGE_MULTIPLE_STEPS ? "1" : "0")  + ".txt"), (totals.values().stream().mapToDouble(d -> d).sum() + "\n").getBytes(), StandardOpenOption.APPEND, StandardOpenOption.CREATE);
				isClosed = true;
			}
			catch(final IOException e){
				e.printStackTrace();
			}
		}
	}
}
