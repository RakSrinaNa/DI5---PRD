package fr.mrcraftcod.simulator;

import fr.mrcraftcod.simulator.utils.Identifiable;
import fr.mrcraftcod.simulator.utils.JSONUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-10-18.
 *
 * @author Thomas Couchoud
 * @since 2018-10-18
 */
public class SimulationParameters{
	private final Logger LOGGER = LoggerFactory.getLogger(SimulationParameters.class);
	private final Environment environment;
	
	private SimulationParameters(){
		this.environment = new Environment();
	}
	
	static SimulationParameters loadFomFile(final Path path) throws Exception{
		return new SimulationParameters().fillFromJson(new JSONObject(Files.readString(path)));
	}
	
	private SimulationParameters fillFromJson(final JSONObject json) throws Exception{
		environment.setSeed(Optional.of(json.optLong("seed")).filter(i -> i > 0).orElse(System.currentTimeMillis()));
		for(final var elementObj : json.optJSONArray("environment")){
			if(!(elementObj instanceof JSONObject)){
				throw new IllegalArgumentException("\"environment\" should be a list of object");
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
		return new ReflectionToStringBuilder(this).toString();
	}
}
