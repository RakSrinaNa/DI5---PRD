package fr.mrcraftcod.simulator.chargers;

import fr.mrcraftcod.simulator.Environment;
import fr.mrcraftcod.simulator.utils.Identifiable;
import fr.mrcraftcod.simulator.utils.JSONParsable;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
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
 * @since 1.0.0
 */
public class Charger implements JSONParsable<Charger>, Identifiable{
	private static final Logger LOGGER = LoggerFactory.getLogger(Charger.class);
	private final List<ChargerListener> listeners;
	private final int ID;
	private static int NEXT_ID = 0;
	private double currentCapacity;
	private double maxCapacity;
	private double radius;
	private double transmissionPower;
	
	/**
	 * Constructor used by the JSON filler.
	 *
	 * @param environment The environment the charger is in.
	 *
	 * @since 1.0.0
	 */
	public Charger(@SuppressWarnings("unused") @NotNull final Environment environment){
		this(0, 0, 1, 1);
	}
	
	/**
	 * Constructor.
	 *
	 * @param currentCapacity   The initial capacity.
	 * @param maxCapacity       The maximum capacity of the charger.
	 * @param radius            The radius the charger can charge.
	 * @param transmissionPower The transmission power of the charger.
	 *
	 * @since 1.0.0
	 */
	public Charger(final double currentCapacity, final double maxCapacity, final double radius, final double transmissionPower){
		this.ID = ++NEXT_ID;
		this.listeners = new ArrayList<>();
		setMaxCapacity(maxCapacity);
		setCurrentCapacity(currentCapacity);
		setRadius(radius);
		setTransmissionPower(transmissionPower);
		LOGGER.info("New charger created: {}", getUniqueIdentifier());
	}
	
	/**
	 * Get the maximum capacity.
	 *
	 * @return The maximum capacity.
	 *
	 * @since 1.0.0
	 */
	public double getMaxCapacity(){
		return maxCapacity;
	}
	
	/**
	 * Set the maximum capacity of the charger.
	 *
	 * @param maxCapacity The capacity to set.
	 *
	 * @since 1.0.0
	 */
	private void setMaxCapacity(final double maxCapacity){
		if(maxCapacity < 0){
			throw new IllegalArgumentException("Maximum capacity must be positive or 0");
		}
		this.maxCapacity = maxCapacity;
	}
	
	@Override
	public Charger fillFromJson(@NotNull final Environment environment, @NotNull final JSONObject json) throws IllegalArgumentException{
		setRadius(json.getDouble("radius"));
		setTransmissionPower(json.getDouble("transmissionPower"));
		setMaxCapacity(json.getDouble("maxCapacity"));
		setCurrentCapacity(json.getDouble("currentCapacity"));
		return this;
	}
	
	/**
	 * Add a charger listener.
	 *
	 * @param listener The listener to add.
	 *
	 * @since 1.0.0
	 */
	public void addChargerListener(final ChargerListener listener){
		listeners.add(listener);
	}
	
	/**
	 * Remove a charger listener.
	 *
	 * @param listener The listener to remove.
	 *
	 * @since 1.0.0
	 */
	public void removeChargerListener(final ChargerListener listener){
		listeners.remove(listener);
	}
	
	/**
	 * Get the current capacity.
	 *
	 * @return The current capacity.
	 *
	 * @since 1.0.0
	 */
	public double getCurrentCapacity(){
		return currentCapacity;
	}
	
	@Override
	public String toString(){
		return new ReflectionToStringBuilder(this).toString();
	}
	
	/**
	 * Set the current capacity of the charger.
	 *
	 * @param currentCapacity The capacity to set.
	 *
	 * @since 1.0.0
	 */
	@SuppressWarnings("WeakerAccess")
	public void setCurrentCapacity(final double currentCapacity){
		if(currentCapacity > getMaxCapacity()){
			throw new IllegalArgumentException("Current capacity is greater than the max capacity");
		}
		if(currentCapacity < 0){
			throw new IllegalArgumentException("Current capacity must be positive or 0");
		}
		LOGGER.debug("Set charger {} current capacity from {} to {}", this.getUniqueIdentifier(), this.currentCapacity, currentCapacity);
		this.currentCapacity = currentCapacity;
		this.listeners.forEach(l -> l.onChargerCurrentCapacityChange(this, currentCapacity));
	}
	
	@Override
	public int getID(){
		return this.ID;
	}
	
	/**
	 * Get the charging radius.
	 *
	 * @return The radius.
	 *
	 * @since 1.0.0
	 */
	public double getRadius(){
		return radius;
	}
	
	/**
	 * Set the radius of the charger.
	 *
	 * @param radius The radius to set.
	 *
	 * @since 1.0.0
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
	 *
	 * @since 1.0.0
	 */
	public double getTransmissionPower(){
		return transmissionPower;
	}
	
	/**
	 * Set the transmission power of the charger.
	 *
	 * @param transmissionPower The transmission power to set.
	 *
	 * @since 1.0.0
	 */
	private void setTransmissionPower(final double transmissionPower){
		if(transmissionPower <= 0){
			throw new IllegalArgumentException("Transmission power must be positive");
		}
		this.transmissionPower = transmissionPower;
	}
	
	@Override
	public boolean haveSameValues(final Identifiable identifiable){
		if(this == identifiable){
			return true;
		}
		if(identifiable instanceof Charger){
			final var charger = (Charger) identifiable;
			return getMaxCapacity() == charger.getMaxCapacity() && getCurrentCapacity() == charger.getCurrentCapacity() && getRadius() == charger.getRadius() && getTransmissionPower() == charger.getTransmissionPower();
		}
		return false;
	}
}
