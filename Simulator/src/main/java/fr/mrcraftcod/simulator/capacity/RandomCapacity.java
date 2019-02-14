package fr.mrcraftcod.simulator.capacity;

import fr.mrcraftcod.simulator.Environment;
import fr.mrcraftcod.simulator.utils.JSONParsable;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Represent capacity generated randomly.
 * <p>
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-10-18.
 *
 * @author Thomas Couchoud
 */
public class RandomCapacity extends Capacity implements JSONParsable<Capacity>{
	/**
	 * Constructor used by the JSON filler.
	 *
	 * @param environment The environment the capacity is in.
	 */
	public RandomCapacity(@NotNull final Environment environment){
		super(environment);
	}
	
	/**
	 * Constructor used by the JSON filler.
	 *
	 * @param environment The environment the position is in.
	 * @param maxValue    The max value of the generated capacity.
	 */
	public RandomCapacity(@NotNull final Environment environment, final int maxValue){
		super(genRandom(environment, maxValue));
	}
	
	/**
	 * Generates a random coordinate between 0 and +max.
	 *
	 * @param environment The environment.
	 * @param maxValue    The max value.
	 *
	 * @return The random capacity.
	 */
	private static double genRandom(final Environment environment, final int maxValue){
		return environment.getRandom().nextDouble() * maxValue;
	}
	
	@Override
	public Capacity fillFromJson(@NotNull final Environment environment, @NotNull final JSONObject json) throws JSONException{
		final var maxValue = json.optInt("max", 10);
		setCapacity(genRandom(environment, maxValue));
		return this;
	}
}
