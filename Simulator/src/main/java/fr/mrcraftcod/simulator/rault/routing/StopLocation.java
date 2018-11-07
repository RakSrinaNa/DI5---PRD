package fr.mrcraftcod.simulator.rault.routing;

import fr.mrcraftcod.simulator.positions.Position;
import fr.mrcraftcod.simulator.sensors.Sensor;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a stop location.
 * <p>
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-11-07.
 *
 * @author Thomas Couchoud
 */
public class StopLocation{
	private final Position position;
	private final Collection<Sensor> sensors;
	
	/**
	 * Constructor.
	 *
	 * @param position The position of the stop location.
	 */
	StopLocation(final Position position){
		this.position = position;
		this.sensors = new ArrayList<>();
	}
	
	/**
	 * Check if this location contains a sensor.
	 *
	 * @param sensor The sensor to check for.
	 *
	 * @return True if the sensor is contained, false otherwise.
	 */
	boolean contains(final Sensor sensor){
		return sensors.contains(sensor);
	}
	
	/**
	 * Add a sensor to the stop location.
	 *
	 * @param sensor The sensor to add.
	 */
	void addSensor(final Sensor sensor){
		this.sensors.add(sensor);
	}
	
	@Override
	public String toString(){
		return new ReflectionToStringBuilder(this).toString();
	}
	
	/**
	 * Get the position of this stop location.
	 *
	 * @return The position.
	 */
	public Position getPosition(){
		return position;
	}
	
	/**
	 * Get the sensors of this stop location.
	 *
	 * @return The sensors.
	 */
	public Collection<? extends Sensor> getSensors(){
		return sensors;
	}
}
