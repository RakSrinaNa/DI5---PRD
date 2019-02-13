package fr.mrcraftcod.simulator;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.FileConverter;
import java.io.File;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 01/09/2018.
 *
 * @author Thomas Couchoud
 * @since 2018-09-01
 */
@SuppressWarnings("unused")
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
	
	public File getJsonConfigFile(){
		return jsonConfigFile;
	}
	
	public int getReplication(){
		return replication;
	}
	
	public boolean isCLI(){
		return CLI;
	}
}
