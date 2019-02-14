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
	 * @param minX        The min value of the generated X coordinate.
	 * @param maxX        The max value of the generated X coordinate.
	 * @param minY        The min value of the generated Y coordinate.
	 * @param maxY        The max value of the generated Y coordinate.
	 */
	public RandomPosition(@NotNull final Environment environment, final double minX, final double maxX, final double minY, final double maxY){
		super(genRandom(environment, minX, maxX), genRandom(environment, minY, maxY));
	}
	
	/**
	 * Generates a random coordinate between min and max.
	 *
	 * @param environment The environment.
	 * @param minValue    The min value.
	 * @param maxValue    The max value.
	 *
	 * @return The random coordinate.
	 */
	private static double genRandom(final Environment environment, final double minValue, final double maxValue){
		return environment.getRandom().nextDouble() * (maxValue - minValue) + minValue;
	}
	
	@Override
	public Position fillFromJson(@NotNull final Environment environment, @NotNull final JSONObject json) throws JSONException{
		final var minX = json.optDouble("minX", -10);
		final var maxX = json.optDouble("maxX", 10);
		final var minY = json.optDouble("minY", -10);
		final var maxY = json.optDouble("maxY", 10);
		setX(genRandom(environment, minX, maxX));
		setY(genRandom(environment, minY, maxY));
		return this;
	}
}
