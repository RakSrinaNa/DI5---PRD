package fr.mrcraftcod.simulator.utils;

import fr.mrcraftcod.simulator.Environment;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Utilities to parse JSON.
 * <p>
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-10-18.
 *
 * @author Thomas Couchoud
 * @since 1.0.0
 */
public class JSONUtils{
	private static final Logger LOGGER = LoggerFactory.getLogger(JSONUtils.class);
	
	/**
	 * Get objects of a class from the json.
	 * This is done with an object formatted as follow: {@code
	 * "count": Int; Default 1; must be positive non 0,
	 * "class": String,
	 * "parameters": JSONObject; This will be passed to {@link JSONParsable#fillFromJson(Environment, JSONObject)}
	 * }
	 *
	 * @param environment The environment the object is in.
	 * @param elementObj  The JSONObject to parse.
	 * @param filterKlass Keep only the instances of this class.
	 * @param <T>         The type of the elements to get.
	 *
	 * @return A list of objects.
	 *
	 * @throws IllegalArgumentException If the class couldn't be found or isn't an instance of {@link JSONParsable}.
	 * @since 1.0.0
	 */
	public static <T extends JSONParsable> List<T> getObjects(final Environment environment, final JSONObject elementObj, final Class<T> filterKlass){
		return getObjects(environment, elementObj).stream().filter(filterKlass::isInstance).map(filterKlass::cast).collect(Collectors.toList());
	}
	
	/**
	 * Get objects from the json.
	 * This is done with an object formatted as follow:
	 * {@code
	 * "count": Int; Default 1; must be positive non 0,
	 * "class": String,
	 * "parameters": JSONObject; This will be passed to {@link JSONParsable#fillFromJson(Environment, JSONObject)}
	 * }
	 *
	 * @param environment The environment the object is in.
	 * @param elementObj  The JSONObject to parse.
	 *
	 * @return A list of objects.
	 *
	 * @throws IllegalArgumentException If the class couldn't be found or isn't an instance of {@link JSONParsable}.
	 * @since 1.0.0
	 */
	public static List<JSONParsable> getObjects(final Environment environment, final JSONObject elementObj){
		final Class<?> elementKlass;
		try{
			elementKlass = Class.forName(Optional.of(elementObj.optString("class")).filter(s -> !s.isBlank()).orElseThrow(() -> new IllegalStateException("No class name provided")));
		}
		catch(final ClassNotFoundException e){
			LOGGER.error("Class from {} not found", elementObj, e);
			throw new IllegalArgumentException("Malformatted JSON");
		}
		if(!getAllExtendedOrImplementedTypesRecursively(elementKlass).contains(JSONParsable.class)){
			throw new IllegalArgumentException("Element class isn't parsable from JSON");
		}
		@SuppressWarnings("unchecked") final var parsableClazz = (Class<JSONParsable>) elementKlass;
		final var parameters = Optional.ofNullable(elementObj.optJSONObject("parameters")).orElse(new JSONObject());
		return IntStream.range(0, Optional.of(elementObj.optInt("count")).filter(i -> i > 0).orElse(1)).mapToObj(i -> {
			try{
				return (JSONParsable) parsableClazz.getConstructor(Environment.class).newInstance(environment).fillFromJson(environment, parameters);
			}
			catch(final Exception e){
				LOGGER.error("Failed to parse JSON as object of class {} from {}", parsableClazz, elementObj, e);
			}
			return null;
		}).collect(Collectors.toList());
	}
	
	private static Set<Class<?>> getAllExtendedOrImplementedTypesRecursively(Class<?> clazz){
		final List<Class<?>> res = new ArrayList<>();
		
		do{
			res.add(clazz);
			final var interfaces = clazz.getInterfaces();
			if(interfaces.length > 0){
				res.addAll(Arrays.asList(interfaces));
				for(final var interfaze : interfaces){
					res.addAll(getAllExtendedOrImplementedTypesRecursively(interfaze));
				}
			}
			final var superClass = clazz.getSuperclass();
			if(superClass == null){
				break;
			}
			clazz = superClass;
		}
		while(!"java.lang.Object".equals(clazz.getCanonicalName()));
		return new HashSet<>(res);
	}
}
