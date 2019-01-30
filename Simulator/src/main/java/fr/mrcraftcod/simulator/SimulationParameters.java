package fr.mrcraftcod.simulator;

import fr.mrcraftcod.simulator.exceptions.SettingsParserException;
import fr.mrcraftcod.simulator.utils.Identifiable;
import fr.mrcraftcod.simulator.utils.JSONUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

/**
 * Stores the parameters of the simulation.
 * <p>
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-10-18.
 *
 * @author Thomas Couchoud
 */
public class SimulationParameters{
	private final Logger LOGGER = LoggerFactory.getLogger(SimulationParameters.class);
	private final Environment environment;
	
	/**
	 * Constructor.
	 */
	public SimulationParameters(){
		this.environment = new Environment();
	}
	
	/**
	 * Loads a {@link SimulationParameters} object from a JSON file.
	 *
	 * @param path The path of the file.
	 *
	 * @return The parameters.
	 *
	 * @throws SettingsParserException See {@link #fillFromJson(JSONObject)}.
	 * @throws IOException             If the file couldn't be read.
	 */
	public static SimulationParameters loadFomFile(final Path path) throws SettingsParserException, IOException{
		return new SimulationParameters().fillFromJson(new JSONObject(Files.readString(path)));
	}
	
	/**
	 * Fills this object with the values from a JSON object.
	 *
	 * @param json The JSON object.
	 *
	 * @return This object.
	 *
	 * @throws SettingsParserException If the configuration itself is incorrect.
	 */
	private SimulationParameters fillFromJson(final JSONObject json) throws SettingsParserException{
		environment.setSeed(Optional.of(json.optLong("seed")).filter(i -> i > 0).orElse(System.currentTimeMillis()));
		environment.setEnd(json.getInt("end"));
		for(final var elementObj : json.optJSONArray("environment")){
			if(!(elementObj instanceof JSONObject)){
				throw new SettingsParserException("\"environment\" should be a list of object");
			}
			JSONUtils.getObjects(environment, (JSONObject) elementObj).forEach(elementInstance -> {
				if(elementInstance instanceof Identifiable){
					environment.add((Identifiable) elementInstance);
				}
				else{
					LOGGER.warn("Parsed object that isn't identifiable, won't be added to the environment: {}", elementObj);
				}
			});
		}
		return this;
	}
	
	@Override
	public String toString(){
		return new ToStringBuilder(this).append("environment", environment).toString();
	}
	
	/**
	 * Get the environment of these parameters.
	 *
	 * @return The environment.
	 */
	public Environment getEnvironment(){
		return this.environment;
	}
}
