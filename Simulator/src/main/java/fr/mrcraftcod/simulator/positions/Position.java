package fr.mrcraftcod.simulator.positions;

import fr.mrcraftcod.simulator.Environment;
import fr.mrcraftcod.simulator.utils.JSONParsable;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Objects;

/**
 * Represents a 2D position.
 * <p>
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-10-04.
 *
 * @author Thomas Couchoud
 */
public class Position implements JSONParsable<Position>{
	private double x;
	private double y;
	
	/**
	 * Constructor used by the JSON filler.
	 *
	 * @param environment The environment the position is in.
	 */
	public Position(@NotNull final Environment environment){
		this(0, 0);
	}
	
	/**
	 * Constructor.
	 *
	 * @param x The X coordinate.
	 * @param y The Y coordinate.
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
	
	/**
	 * Get the distance to another position.
	 *
	 * @param position The position to get the distance to.
	 *
	 * @return The distance.
	 */
	public double distanceTo(final Position position){
		return Math.sqrt(Math.pow(this.getX() - position.getX(), 2) + Math.pow(this.getY() - position.getY(), 2));
	}
	
	@Override
	public boolean equals(final Object o){
		if(this == o){
			return true;
		}
		if(o instanceof Position){
			final var position = (Position) o;
			return Objects.equals(position.getX(), getX()) && Objects.equals(position.getY(), getY());
		}
		return false;
	}
	
	@Override
	public String toString(){
		return String.format("{x: %f; y: %f}", x, y);
	}
	
	/**
	 * Get the X coordinate.
	 *
	 * @return The X coordinate.
	 */
	public double getX(){
		return x;
	}
	
	/**
	 * Set the X coordinate.
	 *
	 * @param x The coordinate to set.
	 */
	void setX(final double x){
		this.x = x;
	}
	
	@Override
	public int hashCode(){
		return Objects.hash(x, y);
	}
	
	/**
	 * Get the Y coordinate.
	 *
	 * @return The Y coordinate.
	 */
	public double getY(){
		return y;
	}
	
	/**
	 * Set the Y coordinate.
	 *
	 * @param y The coordinate to set.
	 */
	void setY(final double y){
		this.y = y;
	}
}
