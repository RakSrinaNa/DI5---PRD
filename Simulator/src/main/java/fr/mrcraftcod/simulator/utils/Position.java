package fr.mrcraftcod.simulator.utils;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-10-04.
 *
 * @author Thomas Couchoud
 * @since 2018-10-04
 */
public class Position{
	public Position(final double x, final double y){
		this.x = x;
		this.y = y;
	}
	
	private double x;
	private double y;
	
	public double getX(){
		return x;
	}
	
	public double getY(){
		return y;
	}
	
	public void setX(final double x){
		this.x = x;
	}
	
	public void setY(final double y){
		this.y = y;
	}
	
	@Override
	public String toString(){
		return "Position{" + "x=" + x + ", y=" + y + '}';
	}
}
