package fr.mrcraftcod.simulator.sensors;

import fr.mrcraftcod.simulator.utils.Identifiable;
import fr.mrcraftcod.simulator.utils.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-10-04.
 *
 * @author Thomas Couchoud
 * @since 2018-10-04
 */
public class Sensor implements Identifiable{
	private final static Logger LOGGER = LoggerFactory.getLogger(Sensor.class);
	private static int NEXT_ID = 0;
	private final int ID;
	private final List<SensorListener> listeners;
	private final double maxCapacity;
	private double currentCapacity;
	private final double powerActivation;
	private final Position position;
	
	public Sensor(final double powerActivation, final Position position, final double maxCapacity, final double currentCapacity){
		this.ID = ++NEXT_ID;
		this.listeners = new ArrayList<>();
		this.maxCapacity = maxCapacity;
		this.position = position;
		this.currentCapacity = currentCapacity;
		this.powerActivation = powerActivation;
		LOGGER.info("New sensor created: {}");
	}
	
	@Override
	public String getIdentifier(){
		return String.format("SENS[%d]", this.ID);
	}
	
	public Position getPosition(){
		return position;
	}
	
	public void setCurrentCapacity(final double currentCapacity){
		if(currentCapacity >= 0){
			this.currentCapacity = currentCapacity;
			LOGGER.debug("Set sensor current capacity of {} to {}", this.getIdentifier(), currentCapacity);
			listeners.forEach(l -> l.onCurrentCapacityChange(currentCapacity));
		}
		else{
			LOGGER.warn("Trying to set current capacity of {} to {}", this.getIdentifier(), currentCapacity);
		}
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
	
	@Override
	public String toString(){
		return "Sensor{ID=" + ID + ",  maxCapacity=" + maxCapacity + ", currentCapacity=" + currentCapacity + ", powerActivation=" + powerActivation + ", position=" + position+ '}';
	}
}
