package fr.mrcraftcod.simulator.utils;

import fr.mrcraftcod.simulator.Environment;
import org.json.JSONObject;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-10-18.
 *
 * @author Thomas Couchoud
 * @since 2018-10-18
 */
public interface JSONParsable<T>{
	T fillFromJson(Environment environment, JSONObject json) throws Exception;
}
