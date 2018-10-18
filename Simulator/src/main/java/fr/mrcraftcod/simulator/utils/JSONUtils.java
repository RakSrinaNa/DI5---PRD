package fr.mrcraftcod.simulator.utils;

import fr.mrcraftcod.simulator.Environment;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-10-18.
 *
 * @author Thomas Couchoud
 * @since 2018-10-18
 */
public class JSONUtils{
	private static final Logger LOGGER = LoggerFactory.getLogger(JSONUtils.class);
	
	public static List<JSONParsable> getObjects(final Environment environment, final JSONObject elementObj) throws ClassNotFoundException{
		final var elementKlass = Class.forName(Optional.of(elementObj.optString("class")).filter(s -> !s.isBlank()).orElseThrow(() -> new IllegalStateException("No class name provided")));
		if(!getAllExtendedOrImplementedTypesRecursively(elementKlass).contains(JSONParsable.class)){
			throw new IllegalStateException("Element class isn't parsable from JSON");
		}
		final var parsableKlass = (Class<JSONParsable>) elementKlass;
		final var parameters = Optional.ofNullable(elementObj.optJSONObject("parameters")).orElse(new JSONObject());
		return IntStream.range(0, Optional.of(elementObj.optInt("count")).filter(i -> i > 0).orElse(1)).mapToObj(i -> {
			try{
				return (JSONParsable) parsableKlass.getConstructor(Environment.class).newInstance(environment).fillFromJson(environment, parameters);
			}
			catch(final Exception e){
				LOGGER.error("Failed to parse JSON as object of class {} from {}", parsableKlass, elementObj, e);
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
