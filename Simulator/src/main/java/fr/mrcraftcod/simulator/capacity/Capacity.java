package fr.mrcraftcod.simulator.capacity;

import fr.mrcraftcod.simulator.Environment;
import fr.mrcraftcod.simulator.utils.JSONParsable;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Represent capacity.
 * <p>
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-10-18.
 *
 * @author Thomas Couchoud
 */
public class Capacity extends AbstractCapacity implements JSONParsable<AbstractCapacity>{
	/**
	 * Constructor used by the JSON filler.
	 *
	 * @param environment The environment the capacity is in.
	 */
	public Capacity(@NotNull final Environment environment){
		super(environment);
	}
	
	/**
	 * Constructor used by the JSON filler.
	 *
	 * @param environment The environment the position is in.
	 * @param value       The value of the capacity.
	 */
	public Capacity(@SuppressWarnings("unused") @NotNull final Environment environment, final int value){
		super(value);
	}
	
	@Override
	public AbstractCapacity fillFromJson(@NotNull final Environment environment, @NotNull final JSONObject json) throws JSONException{
		setCapacity(json.getInt("value"));
		return this;
	}
}
