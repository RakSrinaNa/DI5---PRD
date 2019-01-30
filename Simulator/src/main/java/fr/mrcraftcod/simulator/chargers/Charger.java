package fr.mrcraftcod.simulator.chargers;

import fr.mrcraftcod.simulator.Environment;
import fr.mrcraftcod.simulator.positions.Position;
import fr.mrcraftcod.simulator.utils.Identifiable;
import fr.mrcraftcod.simulator.utils.JSONParsable;
import fr.mrcraftcod.simulator.utils.Positionable;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a charger.
 * If a custom sensor needs to be done, extend this class and change its behaviour.
 * Don't forget to override {@link #fillFromJson(Environment, JSONObject)} to get custom fields and call the super method.
 * <p>
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-18-04.
 *
 * @author Thomas Couchoud
 */
public class Charger implements JSONParsable<Charger>, Identifiable, Positionable{
	private static final Logger LOGGER = LoggerFactory.getLogger(Charger.class);
	private final List<ChargerListener> listeners;
	private final int ID;
	private static int NEXT_ID = 0;
	private double currentCapacity;
	private double maxCapacity;
	private double radius;
	private double transmissionPower;
	private double speed;
	private boolean available;
	private Position position;
	
	/**
	 * Constructor used by the JSON filler.
	 *
	 * @param environment The environment the charger is in.
	 */
	public Charger(@SuppressWarnings("unused") @NotNull final Environment environment){
		this(0, 0, 1, 1, 1);
	}
	
	/**
	 * Constructor.
	 *
	 * @param currentCapacity   The initial capacity.
	 * @param maxCapacity       The maximum capacity of the charger.
	 * @param radius            The radius the charger can charge.
	 * @param transmissionPower The transmission power of the charger.
	 * @param speed             The speed of the charger.
	 */
	public Charger(final double currentCapacity, final double maxCapacity, final double radius, final double transmissionPower, final double speed){
		this.ID = ++NEXT_ID;
		this.listeners = new ArrayList<>();
		setMaxCapacity(maxCapacity);
		setCurrentCapacity(currentCapacity);
		setRadius(radius);
		setTransmissionPower(transmissionPower);
		setSpeed(speed);
		setAvailable(true);
		setPosition(new Position(0, 0));
		LOGGER.debug("New charger created: {}", getUniqueIdentifier());
	}
	
	/**
	 * Get the maximum capacity.
	 *
	 * @return The maximum capacity.
	 */
	public double getMaxCapacity(){
		return maxCapacity;
	}
	
	/**
	 * Get the received power at a given distance.
	 *
	 * @param distance The distance.
	 *
	 * @return The received power.
	 */
	public double getReceivedPower(final double distance){
		return getTransmissionPower() * (-0.0958 * Math.pow(distance, 2) - 0.0377 * distance + 1);
	}
	
	/**
	 * Get the travel time of the charger.
	 *
	 * @param distance The distance to travel.
	 *
	 * @return The time to travel the distance.
	 */
	public double getTravelTime(final double distance){
		return distance / getSpeed();
	}
	
	/**
	 * Get the charger's speed.
	 *
	 * @return The speed.
	 */
	public double getSpeed(){
		return this.speed;
	}
	
	/**
	 * Set the speed.
	 *
	 * @param speed The speed.
	 */
	private void setSpeed(final double speed){
		if(speed <= 0){
			throw new IllegalArgumentException("Speed must be positive");
		}
		this.speed = speed;
	}
	
	/**
	 * Get the energy consumed while traveling.
	 *
	 * @param travelTime The time traveling?
	 *
	 * @return The energy consumed.
	 */
	public double getTravelConsumption(final double travelTime){
		return 7.4 * travelTime + 0.29;
	}
	
	@Override
	public Charger fillFromJson(@NotNull final Environment environment, @NotNull final JSONObject json) throws IllegalArgumentException{
		setRadius(json.getDouble("radius"));
		setTransmissionPower(json.getDouble("transmissionPower"));
		setMaxCapacity(json.getDouble("maxCapacity"));
		setCurrentCapacity(json.getDouble("currentCapacity"));
		setSpeed(json.getDouble("speed"));
		return this;
	}
	
	@Override
	public boolean haveSameValues(final Identifiable identifiable){
		if(this == identifiable){
			return true;
		}
		if(!this.getClass().isInstance(identifiable)){
			return false;
		}
		final var charger = (Charger) identifiable;
		return getMaxCapacity() == charger.getMaxCapacity() && getCurrentCapacity() == charger.getCurrentCapacity() && getRadius() == charger.getRadius() && getTransmissionPower() == charger.getTransmissionPower();
	}
	
	/**
	 * Get the current capacity.
	 *
	 * @return The current capacity.
	 */
	public double getCurrentCapacity(){
		return currentCapacity;
	}
	
	/**
	 * Add some capacity to this element.
	 *
	 * @param amount The amount to add.
	 */
	public void addCapacity(final double amount){
		final var newCapacity = getCurrentCapacity() + amount;
		if(newCapacity > maxCapacity){
			setCurrentCapacity(maxCapacity);
		}
		else if(newCapacity < 0){
			setCurrentCapacity(0);
		}
		else{
			setCurrentCapacity(newCapacity);
		}
	}
	
	/**
	 * Set the current capacity of the charger.
	 *
	 * @param currentCapacity The capacity to set.
	 */
	@SuppressWarnings("WeakerAccess")
	public void setCurrentCapacity(final double currentCapacity){
		if(currentCapacity > getMaxCapacity()){
			throw new IllegalArgumentException("Current capacity is greater than the max capacity");
		}
		if(currentCapacity < 0){
			throw new IllegalArgumentException("Capacity must be positive or 0");
		}
		LOGGER.debug("Set charger {} current capacity from {} to {}", this.getUniqueIdentifier(), this.currentCapacity, currentCapacity);
		this.currentCapacity = Math.max(0, currentCapacity);
		this.listeners.forEach(l -> l.onChargerCurrentCapacityChange(this, currentCapacity));
	}
	
	/**
	 * Remove some capacity to this element.
	 *
	 * @param amount The amount to remove.
	 */
	public void removeCapacity(final double amount){
		final var newCapacity = getCurrentCapacity() - amount;
		if(newCapacity > maxCapacity){
			setCurrentCapacity(maxCapacity);
		}
		else if(newCapacity < 0){
			setCurrentCapacity(0);
		}
		else{
			setCurrentCapacity(newCapacity);
		}
	}
	
	/**
	 * Get the charging radius.
	 *
	 * @return The radius.
	 */
	public double getRadius(){
		return radius;
	}
	
	/**
	 * Set the radius of the charger.
	 *
	 * @param radius The radius to set.
	 */
	private void setRadius(final double radius){
		if(radius <= 0){
			throw new IllegalArgumentException("Radius must be positive");
		}
		this.radius = radius;
	}
	
	/**
	 * Get the power transmission.
	 *
	 * @return The power transmission.
	 */
	public double getTransmissionPower(){
		return transmissionPower;
	}
	
	/**
	 * Set the transmission power of the charger.
	 *
	 * @param transmissionPower The transmission power to set.
	 */
	private void setTransmissionPower(final double transmissionPower){
		if(transmissionPower <= 0){
			throw new IllegalArgumentException("Transmission power must be positive");
		}
		this.transmissionPower = transmissionPower;
	}
	
	/**
	 * Add a charger listener.
	 *
	 * @param listener The listener to add.
	 */
	public void addChargerListener(final ChargerListener listener){
		listeners.add(listener);
	}
	
	/**
	 * Remove a charger listener.
	 *
	 * @param listener The listener to remove.
	 */
	public void removeChargerListener(final ChargerListener listener){
		listeners.remove(listener);
	}
	
	@Override
	public String toString(){
		return new ToStringBuilder(this).append("ID", getUniqueIdentifier()).append("available", available).append("position", position).toString();
	}
	
	@Override
	public int getID(){
		return this.ID;
	}
	
	/**
	 * Get the position of the charger.
	 *
	 * @return The position.
	 */
	@Override
	public Position getPosition(){
		return this.position;
	}
	
	/**
	 * Set the position of the charger.
	 *
	 * @param position The charger's position.
	 */
	public void setPosition(final Position position){
		this.position = position;
	}
	
	/**
	 * Set the maximum capacity of the charger.
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
	 * Get availability of the charger.
	 *
	 * @return The availability.
	 */
	public boolean isAvailable(){
		return available;
	}
	
	/**
	 * Set the availability of the charger.
	 *
	 * @param availability The availability.
	 */
	public void setAvailable(final boolean availability){
		this.available = availability;
	}
}
