package fr.mrcraftcod.simulator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-10-04.
 *
 * @author Thomas Couchoud
 * @since 2018-10-04
 */
public class Main{
	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
	
	/**
	 * Main function.
	 *
	 * @param args - p: The parameters of the simulation, JSON File.
	 */
	public static void main(final String[] args){
		LOGGER.info("Starting simulator version {}", getSimulatorVersion());
		try{
			final var parameters = SimulationParameters.loadFomFile(Paths.get("./test.json"));
			LOGGER.info("Params: {}", parameters);
		}
		catch(final Exception e){
			LOGGER.error("Failed to load parameters", e);
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
