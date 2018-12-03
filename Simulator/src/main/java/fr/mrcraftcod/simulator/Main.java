package fr.mrcraftcod.simulator;

import fr.mrcraftcod.simulator.metrics.MetricEventDispatcher;
import fr.mrcraftcod.simulator.metrics.listeners.SensorCapacityMetricEventListener;
import fr.mrcraftcod.simulator.simulation.Simulator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Properties;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-10-04.
 *
 * @author Thomas Couchoud
 */
public class Main{
	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
	
	/**
	 * Main function.
	 *
	 * @param args - p: The parameters of the simulation, JSON File.
	 */
	public static void main(final String[] args){
		System.loadLibrary("jniortools");
		LOGGER.info("Starting simulator version {}", getSimulatorVersion());
		
		MetricEventDispatcher.addListener(new SensorCapacityMetricEventListener());
		
		SimulationParameters parameters = null;
		try{
			parameters = SimulationParameters.loadFomFile(Paths.get("./test.json"));
			LOGGER.trace("Params: {}", parameters);
		}
		catch(final Exception e){
			LOGGER.error("Failed to load parameters", e);
		}
		if(Objects.nonNull(parameters)){
			Simulator.getSimulator(parameters.getEnvironment()).run();
		}
	}
	
	/**
	 * Get the simulator version from the version.properties file that have been modified by Maven.
	 *
	 * @return The version or "Unknown" if we couldn't fetch it.
	 */
	private static String getSimulatorVersion(){
		final var properties = new Properties();
		try{
			properties.load(Main.class.getResource("/version.properties").openStream());
		}
		catch(final IOException e){
			LOGGER.warn("Error reading version file", e);
		}
		return properties.getProperty("simulator.version", "Unknown");
	}
}
