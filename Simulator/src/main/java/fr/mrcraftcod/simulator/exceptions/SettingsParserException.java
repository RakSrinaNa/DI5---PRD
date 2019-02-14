package fr.mrcraftcod.simulator.exceptions;

import fr.mrcraftcod.simulator.utils.JSONParsable;
import org.json.JSONObject;

/**
 * Exception generated when the parsing of the settings goes wrong.
 * <p>
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-10-24.
 *
 * @author Thomas Couchoud
 */
@SuppressWarnings("WeakerAccess")
public class SettingsParserException extends RuntimeException{
	private static final long serialVersionUID = -9087790543702914359L;
	private final JSONObject json;
	private final Class<? extends JSONParsable> klass;
	
	/**
	 * Constructor.
	 *
	 * @param parsableClazz The class we tried to parse into.
	 * @param elementObj    The JSON we tried to parse.
	 * @param e             The exception thrown.
	 */
	public SettingsParserException(final Class<? extends JSONParsable> parsableClazz, final JSONObject elementObj, final Exception e){
		super(String.format("Failed to parse JSON as object of class %s from %s", parsableClazz, elementObj), e);
		this.json = elementObj;
		this.klass = parsableClazz;
	}
	
	/**
	 * Constructor.
	 *
	 * @param message The message of the exception.
	 */
	public SettingsParserException(final String message){
		super(message);
		this.json = null;
		this.klass = null;
	}
	
	/**
	 * Get the source JSON.
	 *
	 * @return The JSON.
	 */
	public JSONObject getJson(){
		return json;
	}
	
	/**
	 * Get the class we tried to parse.
	 *
	 * @return The class.
	 */
	public Class<? extends JSONParsable> getKlass(){
		return klass;
	}
}
