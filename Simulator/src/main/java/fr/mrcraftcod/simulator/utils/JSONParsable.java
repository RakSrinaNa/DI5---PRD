package fr.mrcraftcod.simulator.utils;

import fr.mrcraftcod.simulator.Environment;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

/**
 * Represents an element that can have its fields filled from a JSON object.
 * <p>
 * Any class implementing this interface should have a constructor taking a {@link Environment} as parameter.
 * <p>
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-10-18.
 *
 * @author Thomas Couchoud
 * @since 1.0.0
 */
public interface JSONParsable<T>{
	/**
	 * Parse the JSON object into the current object.
	 *
	 * @param environment The environment the object belongs to.
	 * @param json        The JSON to parse.
	 *
	 * @return This
	 *
	 * @throws IllegalArgumentException If something went wrong when parsing.
	 * @since 1.0.0
	 */
	T fillFromJson(@NotNull Environment environment, @NotNull JSONObject json) throws IllegalArgumentException;
}
