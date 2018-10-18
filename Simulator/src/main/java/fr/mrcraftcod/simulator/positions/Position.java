package fr.mrcraftcod.simulator.positions;

import fr.mrcraftcod.simulator.Environment;
import fr.mrcraftcod.simulator.utils.JSONParsable;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-10-04.
 *
 * @author Thomas Couchoud
 * @since 2018-10-04
 */
public class Position implements JSONParsable<Position>{
	private double x;
	private double y;
	
	public Position(final Environment environment){
		this(0, 0);
	}
	
	public Position(final double x, final double y){
		this.x = x;
		this.y = y;
	}
	
	@Override
	public Position fillFromJson(final Environment environment, final JSONObject json) throws JSONException{
		this.x = json.getDouble("x");
		this.y = json.getDouble("y");
		return this;
	}
	
	@Override
	public String toString(){
		return new ReflectionToStringBuilder(this).toString();
	}
	
	public double getX(){
		return x;
	}
	
	public void setX(final double x){
		this.x = x;
	}
	
	public double getY(){
		return y;
	}
	
	public void setY(final double y){
		this.y = y;
	}
}
