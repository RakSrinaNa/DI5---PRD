package fr.mrcraftcod.simulator.metrics.listeners;

import fr.mrcraftcod.simulator.Environment;
import fr.mrcraftcod.simulator.chargers.Charger;
import fr.mrcraftcod.simulator.metrics.MetricEvent;
import fr.mrcraftcod.simulator.metrics.MetricEventListener;
import fr.mrcraftcod.simulator.rault.metrics.events.TourChargeMetricEvent;
import fr.mrcraftcod.simulator.rault.metrics.events.TourTravelEndMetricEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;

/**
 * Saves the inactive times of the chargers in a file.
 *
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-11-22.
 *
 * @author Thomas Couchoud
 * @since 2018-11-22
 */
@SuppressWarnings("unused")
public class ReplicationTotalChargerInactiveTimeMetricEventListener implements MetricEventListener{
	private static final Logger LOGGER = LoggerFactory.getLogger(ReplicationTotalChargerInactiveTimeMetricEventListener.class);
	private final HashMap<Charger, Double> totals;
	private final HashMap<Charger, Double> lastTravelEnd;
	private final Environment environment;
	private boolean isClosed = false;
	
	/**
	 * Constructor.
	 *
	 * @param environment The environment.
	 */
	public ReplicationTotalChargerInactiveTimeMetricEventListener(final Environment environment){
		this.environment = environment;
		totals = new HashMap<>();
		lastTravelEnd = new HashMap<>();
	}
	
	@Override
	public void onEvent(final MetricEvent event){
		if(event instanceof TourTravelEndMetricEvent){
			lastTravelEnd.put(((TourTravelEndMetricEvent) event).getElement(), event.getTime());
		}
		else if(event instanceof TourChargeMetricEvent){
			totals.put(((TourChargeMetricEvent) event).getElement(), totals.getOrDefault(((TourChargeMetricEvent) event).getElement(), 0D) + event.getTime() - lastTravelEnd.getOrDefault(((TourChargeMetricEvent) event).getElement(), event.getTime()));
		}
	}
	
	@Override
	public void close(){
		if(!isClosed){
			try{
				Files.write(MetricEvent.getAllMetricSaveFolder(environment).resolve("inactiveChargeChargers.txt"), (totals.values().stream().mapToDouble(d -> d).sum() + "\n").getBytes(), StandardOpenOption.APPEND, StandardOpenOption.CREATE);
				isClosed = true;
			}
			catch(final IOException e){
				e.printStackTrace();
			}
		}
	}
}
