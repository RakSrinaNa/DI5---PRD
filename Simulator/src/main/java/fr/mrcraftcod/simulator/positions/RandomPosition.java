package fr.mrcraftcod.simulator.positions;

import fr.mrcraftcod.simulator.Environment;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Represent a 2D position generated randomly.
 * <p>
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-10-18.
 *
 * @author Thomas Couchoud
 * @since 1.0.0
 */
public class RandomPosition extends Position{
	/**
	 * Constructor used by the JSON filler.
	 *
	 * @param environment The environment the position is in.
	 *
	 * @since 1.0.0
	 */
	public RandomPosition(@NotNull final Environment environment){
		super(environment);
	}
	
	@Override
	public Position fillFromJson(@NotNull final Environment environment, @NotNull final JSONObject json) throws JSONException{
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
