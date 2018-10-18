package fr.mrcraftcod.simulator.positions;

import fr.mrcraftcod.simulator.Environment;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-10-18.
 *
 * @author Thomas Couchoud
 * @since 2018-10-18
 */
public class RandomPosition extends Position{
	public RandomPosition(final Environment environment){
		super(environment);
	}
	
	@Override
	public Position fillFromJson(final Environment environment, final JSONObject json) throws JSONException{
		final var maxValue = json.optDouble("max", 10);
		setX(environment.getRandom().nextDouble() * maxValue);
		setY(environment.getRandom().nextDouble() * maxValue);
		return this;
	}
	
	@Override
	public String toString(){
		return new ReflectionToStringBuilder(this).toString();
	}
}
