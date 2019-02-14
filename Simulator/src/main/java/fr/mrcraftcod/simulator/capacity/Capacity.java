package fr.mrcraftcod.simulator.capacity;

import fr.mrcraftcod.simulator.Environment;
import fr.mrcraftcod.simulator.utils.JSONParsable;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Objects;

/**
 * Represents a capacity.
 *
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2019-01-17.
 *
 * @author Thomas Couchoud
 * @since 2019-01-17
 */
@SuppressWarnings("WeakerAccess")
public abstract class Capacity implements JSONParsable<Capacity>{
	private double capacity;
	
	/**
	 * Set the capacity.
	 *
	 * @param capacity The capacity.
	 */
	public void setCapacity(final double capacity){
		this.capacity = capacity;
	}
	
	/**
	 * Constructor.
	 *
	 * @param environment The environment the capacity is in.
	 */
	public Capacity(@SuppressWarnings("unused") @NotNull final Environment environment){
		this(0);
	}
	
	/**
	 * Constructor.
	 *
	 * @param capacity The capacity.
	 */
	public Capacity(final double capacity){this.capacity = capacity;}
	
	/**
	 * Get the capacity.
	 *
	 * @return The capacity.
	 */
	public double getCapacity(){
		return capacity;
	}
	
	@Override
	public Capacity fillFromJson(@NotNull final Environment environment, @NotNull final JSONObject json) throws JSONException{
		setCapacity(json.getDouble("value"));
		return this;
	}
	
	@Override
	public boolean equals(final Object o){
		if(this == o){
			return true;
		}
		if(o instanceof Capacity){
			final var capacity = (Capacity) o;
			return capacity.capacity == this.capacity;
		}
		return false;
	}
	
	@Override
	public String toString(){
		return new ToStringBuilder(this).append("capacity", capacity).toString();
	}
	
	@Override
	public int hashCode(){
		return Objects.hash(capacity);
	}
}
