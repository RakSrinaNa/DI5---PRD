package fr.mrcraftcod.simulator;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.FileConverter;
import java.io.File;

/**
 * The parameters of the program.
 * <p>
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 01/09/2018.
 *
 * @author Thomas Couchoud
 * @since 2018-09-01
 */
@SuppressWarnings("WeakerAccess")
public class CLIParameters{
	@Parameter(names = {
			"-c",
			"--config"
	}, description = "Path to the json configuration", converter = FileConverter.class, required = true)
	private File jsonConfigFile;
	
	@SuppressWarnings("FieldMayBeFinal")
	@Parameter(names = {"--cli"}, description = "Run the simulator without ui")
	private boolean CLI = false;
	
	@SuppressWarnings("FieldMayBeFinal")
	@Parameter(names = {"--replication"}, description = "The number of replications (only in CLI mode)")
	private int replication = 1;
	
	@SuppressWarnings("FieldMayBeFinal")
	@Parameter(names = {"--run-name"}, description = "The name of the run")
	private String runName = "" + System.currentTimeMillis();
	
	/**
	 * Get the json file for the simulation configuration.
	 *
	 * @return The json configuration file.
	 */
	public File getJsonConfigFile(){
		return jsonConfigFile;
	}
	
	/**
	 * Get the number of replications.
	 *
	 * @return The number of replications.
	 */
	public int getReplication(){
		return replication;
	}
	
	/**
	 * Get if the program should be run in the console.
	 *
	 * @return True if it is to run in the console, false otherwise.
	 */
	public boolean isCLI(){
		return CLI;
	}
	
	/**
	 * Get the name of the run for the metric files.
	 *
	 * @return The name of the run.
	 */
	public String getRunName(){
		return runName;
	}
}
