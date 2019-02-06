package fr.mrcraftcod.simulator;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import fr.mrcraftcod.simulator.jfx.MainApplication;
import fr.mrcraftcod.simulator.jfx.utils.JFXUtils;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.SwingUtilities;
import java.io.IOException;
import java.nio.file.Path;
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
		LOGGER.info("Starting simulator version {}", Main.getSimulatorVersion());
		final var parameters = new CLIParameters();
		try{
			JCommander.newBuilder().addObject(parameters).build().parse(args);
		}
		catch(final ParameterException e){
			LOGGER.error("Failed to parse arguments", e);
			e.usage();
			System.exit(1);
		}
		
		var kontinue = true;
		try{
			System.loadLibrary("jniortools");
		}
		catch(final Throwable e){
			kontinue = false;
			LOGGER.error("Failed to load ORTools library", e);
			SwingUtilities.invokeLater(() -> {
				new JFXPanel(); // this will prepare JavaFX toolkit and environment
				Platform.runLater(() -> {
					JFXUtils.displayExceptionAlert(e, "Simulator error", "Error while starting", "The simulator could not be initialized because ortools was not found. Please add library path with java argument -Djava.library.path=/path/to/folder");
					System.exit(1);
				});
			});
		}
		
		if(kontinue){
			if(!parameters.isCLI()){
				MainApplication.main(args, loadParameters(Paths.get(parameters.getJsonConfigFile().toURI())));
			}
			else{
				for(var i = 0; i < parameters.getReplication(); i++){
					LOGGER.info("Replication {}/{}", i + 1, parameters.getReplication());
					final var simulationParameters = loadParameters(Paths.get(parameters.getJsonConfigFile().toURI()));
					if(Objects.nonNull(simulationParameters)){
						simulationParameters.getEnvironment().getSimulator().setRunning(true);
						simulationParameters.getEnvironment().getSimulator().run();
						simulationParameters.getEnvironment().getSimulator().stop();
					}
				}
			}
		}
	}
	
	private static SimulationParameters loadParameters(final Path path){
		SimulationParameters simulationParameters = null;
		try{
			simulationParameters = SimulationParameters.loadFomFile(path);
			LOGGER.trace("Params: {}", simulationParameters);
		}
		catch(final Exception e){
			LOGGER.error("Failed to load parameters", e);
		}
		return simulationParameters;
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
