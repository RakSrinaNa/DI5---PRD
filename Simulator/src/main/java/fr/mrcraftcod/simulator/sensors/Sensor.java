package fr.mrcraftcod.simulator.sensors;

import fr.mrcraftcod.simulator.Environment;
import fr.mrcraftcod.simulator.positions.Position;
import fr.mrcraftcod.simulator.utils.Identifiable;
import fr.mrcraftcod.simulator.utils.JSONParsable;
import fr.mrcraftcod.simulator.utils.JSONUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a sensor.
 * If a custom sensor needs to be done, extend this class and change its behaviour.
 * Don't forget to override {@link #fillFromJson(Environment, JSONObject)} to get custom fields and call the super method.
 * <p>
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-18-04.
 *
 * @author Thomas Couchoud
 * @since 2018-18-04
 */
public class Sensor implements Identifiable, JSONParsable<Sensor>{
	private final static Logger LOGGER = LoggerFactory.getLogger(Sensor.class);
	private static int NEXT_ID = 0;
	private final int ID;
	private final List<SensorListener> listeners;
	private double maxCapacity;
	private double currentCapacity;
	private double powerActivation;
	private Position position;
	
	/**
	 * Constructor used to use the JSON filler.
	 */
	public Sensor(final Environment environment){
		this(0, new Position(0, 0), 0, 0);
	}
	
	/**
	 * Constructor.
	 *
	 * @param powerActivation The minimum amount of power needed in order to be recharged.
	 * @param position        The positions of the sensor.
	 * @param maxCapacity     The maximum capacity of the sensor.
	 * @param currentCapacity The initial capacity of the sensor.
	 *
	 * @throws IllegalArgumentException If the current capacity is greater than the maximum capacity.
	 */
	public Sensor(final double powerActivation, final Position position, final double maxCapacity, final double currentCapacity){
		if(currentCapacity > maxCapacity){
			throw new IllegalArgumentException("Current capacity is greater than the max capacity");
		}
		
		this.ID = ++NEXT_ID;
		this.listeners = new ArrayList<>();
		this.maxCapacity = maxCapacity;
		this.position = position;
		this.currentCapacity = currentCapacity;
		this.powerActivation = powerActivation;
		LOGGER.info("New sensor created: {}", getUniqueIdentifier());
	}
	
	@Override
	public String getUniqueIdentifier(){
		return String.format("SENS[%d]", this.ID);
	}
	
	@Override
	public Sensor fillFromJson(final Environment environment, final JSONObject json) throws Exception{
		this.powerActivation = json.getDouble("powerActivation");
		this.position = JSONUtils.getObjects(environment, json.getJSONObject("position")).stream().filter(c -> c instanceof Position).map(c -> (Position) c).findFirst().orElseThrow(() -> new IllegalArgumentException("Position should define a class with parameters"));
		this.maxCapacity = json.getDouble("maxCapacity");
		this.currentCapacity = json.getDouble("currentCapacity");
		return this;
	}
	
	public Position getPosition(){
		return position;
	}
	
	@Override
	public String toString(){
		return new ReflectionToStringBuilder(this).toString();
	}
	
	public Sensor(final double powerActivation, final Position position, final double maxCapacity){
		this(powerActivation, position, maxCapacity, 0);
	}
	
	public double getMaxCapacity(){
		return maxCapacity;
	}
	
	public double getCurrentCapacity(){
		return currentCapacity;
	}
	
	public double getPowerActivation(){
		return powerActivation;
	}
	
	public void addSensorListener(final SensorListener listener){
		listeners.add(listener);
	}
	
	public void removeSensorListener(final SensorListener listener){
		listeners.remove(listener);
	}
	
	public void setCurrentCapacity(final double currentCapacity){
		if(currentCapacity >= 0){
			this.currentCapacity = currentCapacity;
			LOGGER.debug("Set sensor current capacity of {} to {}", this.getUniqueIdentifier(), currentCapacity);
			listeners.forEach(l -> l.onCurrentCapacityChange(currentCapacity));
		}
		else{
			LOGGER.warn("Trying to set current capacity of {} to {}", this.getUniqueIdentifier(), currentCapacity);
		}
	}
}
