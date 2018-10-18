package fr.mrcraftcod.simulator.positions;

import fr.mrcraftcod.simulator.Environment;
import fr.mrcraftcod.simulator.utils.JSONParsable;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Represents a 2D position.
 * <p>
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-10-04.
 *
 * @author Thomas Couchoud
 * @since 1.0.0
 */
public class Position implements JSONParsable<Position>{
	private double x;
	private double y;
	
	/**
	 * Constructor used by the JSON filler.
	 *
	 * @param environment The environment the position is in.
	 *
	 * @since 1.0.0
	 */
	public Position(@NotNull final Environment environment){
		this(0, 0);
	}
	
	/**
	 * Constructor.
	 *
	 * @param x The X coordinate.
	 * @param y The Y coordinate.
	 *
	 * @since 1.0.0
	 */
	public Position(final double x, final double y){
		setX(x);
		setY(y);
	}
	
	@Override
	public Position fillFromJson(@NotNull final Environment environment, @NotNull final JSONObject json) throws JSONException{
		setX(json.getDouble("x"));
		setY(json.getDouble("y"));
		return this;
	}
	
	@Override
	public String toString(){
		return new ReflectionToStringBuilder(this).toString();
	}
	
	/**
	 * Get the X coordinate.
	 *
	 * @return The X coordinate.
	 *
	 * @since 1.0.0
	 */
	public double getX(){
		return x;
	}
	
	/**
	 * Set the X coordinate.
	 *
	 * @param x The coordinate to set.
	 *
	 * @since 1.0.0
	 */
	void setX(final double x){
		this.x = x;
	}
	
	/**
	 * Get the Y coordinate.
	 *
	 * @return The Y coordinate.
	 *
	 * @since 1.0.0
	 */
	public double getY(){
		return y;
	}
	
	/**
	 * Set the Y coordinate.
	 *
	 * @param y The coordinate to set.
	 *
	 * @since 1.0.0
	 */
	void setY(final double y){
		this.y = y;
	}
}
