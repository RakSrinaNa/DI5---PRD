package fr.mrcraftcod.simulator;

import fr.mrcraftcod.simulator.jfx.MainApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-10-04.
 *
 * @author Thomas Couchoud
 */
public class Main{
	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
	
	/**
	 * MainApplication function.
	 *
	 * @param args - p: The parameters of the simulation, JSON File.
	 */
	public static void main(final String[] args){
		MainApplication.main(args);
	}
	
	/**
	 * Get the simulator version from the version.properties jsonConfigFile that have been modified by Maven.
	 *
	 * @return The version or "Unknown" if we couldn't fetch it.
	 */
	public static String getSimulatorVersion(){
		final var properties = new Properties();
		try{
			properties.load(Main.class.getResource("/version.properties").openStream());
		}
		catch(final IOException e){
			LOGGER.warn("Error reading version jsonConfigFile", e);
		}
		return properties.getProperty("simulator.version", "Unknown");
	}
}
