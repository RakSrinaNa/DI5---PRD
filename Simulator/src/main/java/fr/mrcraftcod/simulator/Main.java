package fr.mrcraftcod.simulator;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
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
	 * MainApplication function.
	 *
	 * @param args - p: The parameters of the simulation, JSON File.
	 */
	public static void main(final String[] args){
		System.loadLibrary("jniortools");
		LOGGER.info("Starting simulator version {}", getSimulatorVersion());
		
		final var parameters = new CLIParameters();
		try{
			JCommander.newBuilder().addObject(parameters).build().parse(args);
		}
		catch(final ParameterException e){
			LOGGER.error("Failed to parse arguments", e);
			e.usage();
			System.exit(1);
		}
		
		SimulationParameters simulationParameters = null;
		try{
			simulationParameters = SimulationParameters.loadFomFile(Paths.get(parameters.getJsonConfigFile().toURI()));
			LOGGER.trace("Params: {}", simulationParameters);
		}
		catch(final Exception e){
			LOGGER.error("Failed to load parameters", e);
		}
		if(Objects.nonNull(simulationParameters)){
			Simulator.getSimulator(simulationParameters.getEnvironment()).run();
		}
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
