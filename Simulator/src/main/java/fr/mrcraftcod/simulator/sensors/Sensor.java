package fr.mrcraftcod.simulator.sensors;

import fr.mrcraftcod.simulator.Environment;
import fr.mrcraftcod.simulator.capacity.AbstractCapacity;
import fr.mrcraftcod.simulator.positions.Position;
import fr.mrcraftcod.simulator.utils.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Class representing a sensor.
 * If a custom sensor needs to be done, extend this class and change its behaviour.
 * Don't forget to override {@link #fillFromJson(Environment, JSONObject)} to get custom fields and call the super method.
 * <p>
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-18-04.
 *
 * @author Thomas Couchoud
 */
@SuppressWarnings("WeakerAccess")
public class Sensor implements Identifiable, JSONParsable<Sensor>, Positionable, Comparable<Sensor>, Rechargeable{
	private final static Logger LOGGER = LoggerFactory.getLogger(Sensor.class);
	private final int ID;
	private final List<SensorListener> listeners;
	private final Environment environment;
	private static int NEXT_ID = 0;
	private double maxCapacity;
	private double currentCapacity;
	private double powerActivation;
	private Position position;
	private double dischargeSpeed;
	
	/**
	 * Constructor used by the JSON filler.
	 *
	 * @param environment The environment the sensor is in.
	 */
	public Sensor(@SuppressWarnings("unused") final Environment environment){
		this(environment, 0, 0, 0, new Position(0, 0), 1);
	}
	
	/**
	 * Constructor.
	 *
	 * @param environment     The environment.
	 * @param currentCapacity The initial capacity of the sensor.
	 * @param maxCapacity     The maximum capacity of the sensor.
	 * @param powerActivation The minimum amount of power needed in order to be recharged.
	 * @param position        The positions of the sensor.
	 * @param dischargeSpeed  The speed the charger is loosing energy.
	 */
	public Sensor(final Environment environment, final double currentCapacity, final double maxCapacity, final double powerActivation, final Position position, final double dischargeSpeed){
		this.ID = ++NEXT_ID;
		this.environment = environment;
		this.listeners = new ArrayList<>();
		setMaxCapacity(maxCapacity);
		setCurrentCapacity(currentCapacity);
		setPosition(position);
		setPowerActivation(powerActivation);
		setDischargeSpeed(dischargeSpeed);
		LOGGER.debug("New sensor created: {}", getUniqueIdentifier());
	}
	
	@Override
	public int compareTo(@NotNull final Sensor s){
		return Integer.compare(ID, s.ID);
	}
	
	@Override
	public String toString(){
		return new ToStringBuilder(this).append("ID", getUniqueIdentifier()).append("currentCapacity", currentCapacity).toString();
	}
	
	@Override
	public Sensor fillFromJson(@NotNull final Environment environment, @NotNull final JSONObject json){
		setPowerActivation(json.getDouble("powerActivation"));
		setPosition(JSONUtils.getObjects(environment, json.getJSONObject("position"), Position.class).stream().findFirst().orElseThrow(() -> new IllegalArgumentException("Position should define a class with parameters")));
		setMaxCapacity(json.getDouble("maxCapacity"));
		setCurrentCapacity(JSONUtils.getObjects(environment, json.getJSONObject("currentCapacity"), AbstractCapacity.class).stream().findFirst().orElseThrow(() -> new IllegalArgumentException("CurrentCapacity should define a class with parameters")).getCapacity());
		setDischargeSpeed(json.optDouble("dischargeSpeed", getDischargeSpeed()));
		return this;
	}
	
	/**
	 * Get the discharge speed.
	 *
	 * @return The discharge speed.
	 */
	public double getDischargeSpeed(){
		return dischargeSpeed;
	}
	
	/**
	 * Set the discharge speed.
	 *
	 * @param dischargeSpeed The discharge speed.
	 */
	private void setDischargeSpeed(final double dischargeSpeed){
		if(dischargeSpeed <= 0){
			throw new IllegalArgumentException("Discharge speed must be positive");
		}
		this.dischargeSpeed = dischargeSpeed;
	}
	
	@Override
	public boolean haveSameValues(final Identifiable identifiable){
		if(this == identifiable){
			return true;
		}
		if(!this.getClass().isInstance(identifiable)){
			return false;
		}
		final var sensor = (Sensor) identifiable;
		return getMaxCapacity() == sensor.getMaxCapacity() && getCurrentCapacity() == sensor.getCurrentCapacity() && getPowerActivation() == sensor.getPowerActivation() && Objects.equals(getPosition(), sensor.getPosition());
	}
	
	@Override
	public int getID(){
		return this.ID;
	}
	
	@Override
	public double getCurrentCapacity(){
		return currentCapacity;
	}
	
	@Override
	public double getMaxCapacity(){
		return maxCapacity;
	}
	
	/**
	 * Set the maximum capacity of the sensor.
	 *
	 * @param maxCapacity The capacity to set.
	 */
	private void setMaxCapacity(final double maxCapacity){
		if(maxCapacity < 0){
			throw new IllegalArgumentException("Maximum capacity must be positive or 0");
		}
		this.maxCapacity = maxCapacity;
	}
	
	/**
	 * Get the power activation.
	 *
	 * @return The power activation.
	 */
	public double getPowerActivation(){
		return powerActivation;
	}
	
	/**
	 * Get the position.
	 *
	 * @return The position.
	 */
	@Override
	public Position getPosition(){
		return position;
	}
	
	/**
	 * Set the position.
	 *
	 * @param position The position.
	 */
	private void setPosition(@NotNull final Position position){
		this.position = position;
	}
	
	/**
	 * Set power activation value.
	 *
	 * @param powerActivation The power activation.
	 */
	private void setPowerActivation(final double powerActivation){
		this.powerActivation = powerActivation;
	}
	
	@Override
	public void setCurrentCapacity(final double currentCapacity){
		if(currentCapacity > getMaxCapacity()){
			throw new IllegalArgumentException("Current capacity is greater than the max capacity");
		}
		if(currentCapacity < 0){
			throw new IllegalArgumentException("AbstractCapacity must be positive or 0");
		}
		LOGGER.trace("Set sensor {} current capacity from {} to {}", this.getUniqueIdentifier(), this.currentCapacity, currentCapacity);
		listeners.forEach(l -> l.onSensorCurrentCapacityChange(environment, this, this.currentCapacity, currentCapacity));
		this.currentCapacity = currentCapacity;
	}
	
	/**
	 * Add a sensor listener.
	 *
	 * @param listener The listener to add.
	 */
	public void addSensorListener(final SensorListener listener){
		listeners.add(listener);
	}
	
	/**
	 * Remove a sensor listener.
	 *
	 * @param listener The listener to remove.
	 */
	public void removeSensorListener(final SensorListener listener){
		listeners.remove(listener);
	}
}
