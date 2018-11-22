package fr.mrcraftcod.simulator.positions;

import fr.mrcraftcod.simulator.Environment;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Represent a 2D position generated randomly.
 * <p>
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-10-18.
 *
 * @author Thomas Couchoud
 */
public class RandomPosition extends Position{
	/**
	 * Constructor used by the JSON filler.
	 *
	 * @param environment The environment the position is in.
	 */
	public RandomPosition(@NotNull final Environment environment){
		super(environment);
	}
	
	/**
	 * Constructor used by the JSON filler.
	 *
	 * @param environment The environment the position is in.
	 * @param maxValue    The max value of the generated coordinates.
	 */
	public RandomPosition(@NotNull final Environment environment, final int maxValue){
		super(genRandom(environment, maxValue), genRandom(environment, maxValue));
	}
	
	/**
	 * Generates a random coordinate between -max and +max.
	 *
	 * @param environment The environment.
	 * @param maxValue    The max value.
	 *
	 * @return The random coordinate.
	 */
	private static int genRandom(final Environment environment, final int maxValue){
		return environment.getRandom().nextInt(maxValue * 2) - maxValue;
	}
	
	@Override
	public Position fillFromJson(@NotNull final Environment environment, @NotNull final JSONObject json) throws JSONException{
		final var maxValue = json.optInt("max", 10);
		setX(genRandom(environment, maxValue));
		setY(genRandom(environment, maxValue));
		return this;
	}
}
