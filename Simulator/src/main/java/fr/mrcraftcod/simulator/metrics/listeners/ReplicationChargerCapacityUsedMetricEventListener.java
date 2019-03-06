package fr.mrcraftcod.simulator.metrics.listeners;

import fr.mrcraftcod.simulator.Environment;
import fr.mrcraftcod.simulator.chargers.Charger;
import fr.mrcraftcod.simulator.metrics.MetricEvent;
import fr.mrcraftcod.simulator.metrics.MetricEventListener;
import fr.mrcraftcod.simulator.rault.metrics.events.ChargerDischargedMetricEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-11-22.
 *
 * @author Thomas Couchoud
 * @since 2018-11-22
 */
@SuppressWarnings("unused")
public class ReplicationChargerCapacityUsedMetricEventListener implements MetricEventListener{
	private static final Logger LOGGER = LoggerFactory.getLogger(ReplicationChargerCapacityUsedMetricEventListener.class);
	private final HashMap<Charger, Double> totals;
	private final HashMap<Charger, Double> lastTravelEnd;
	private boolean isClosed = false;
	
	public ReplicationChargerCapacityUsedMetricEventListener(final Environment environment){
		totals = new HashMap<>();
		lastTravelEnd = new HashMap<>();
	}
	
	@Override
	public void onEvent(final MetricEvent event){
		if(event instanceof ChargerDischargedMetricEvent){
			totals.put(((ChargerDischargedMetricEvent) event).getElement(), totals.getOrDefault(((ChargerDischargedMetricEvent) event).getElement(), 0D) + ((ChargerDischargedMetricEvent) event).getNewValue());
		}
	}
	
	@Override
	public void close(){
		if(!isClosed){
			try{
				Files.write(MetricEvent.getAllMetricSaveFolder().resolve("usedCapacityChargers.txt"), (totals.values().stream().mapToDouble(d -> d).sum() + "\n").getBytes(), StandardOpenOption.APPEND, StandardOpenOption.CREATE);
				isClosed = true;
			}
			catch(final IOException e){
				e.printStackTrace();
			}
		}
	}
}
