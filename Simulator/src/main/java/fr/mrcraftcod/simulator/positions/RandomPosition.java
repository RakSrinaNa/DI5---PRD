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
	public RandomPosition(@NotNull final Environment environment, final double minX, final double maxX, final double minY, final double maxY){
		super(genRandom(environment, minX, maxX), genRandom(environment, minY, maxY));
	}
	
	/**
	 * Generates a random coordinate between -max and +max.
	 *
	 * @param environment The environment.
	 * @param maxValue    The max value.
	 *
	 * @return The random coordinate.
	 */
	private static double genRandom(final Environment environment, final double minValue, final double maxValue){
		return environment.getRandom().nextDouble() * (maxValue - minValue) + minValue;
	}
	
	@Override
	public Position fillFromJson(@NotNull final Environment environment, @NotNull final JSONObject json) throws JSONException{
		final var minValue = json.optDouble("min", -10);
		final var maxValue = json.optDouble("max", 10);
		setX(genRandom(environment, minValue, maxValue));
		setY(genRandom(environment, minValue, maxValue));
		return this;
	}
}
