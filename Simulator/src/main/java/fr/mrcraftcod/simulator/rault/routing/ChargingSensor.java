package fr.mrcraftcod.simulator.rault.routing;

import fr.mrcraftcod.simulator.sensors.Sensor;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import java.util.Objects;

/**
 * Represents a sensor to charge.
 * <p>
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-11-07.
 *
 * @author Thomas Couchoud
 *
 */
class ChargingSensor{
	private final Sensor sensor;
	private double chargeTime;
	
	/**
	 * Constructor.
	 *
	 * @param sensor     The sensor.
	 * @param chargeTime The estimated time to charge the sensor.
	 */
	ChargingSensor(final Sensor sensor, final double chargeTime){
		this.sensor = sensor;
		this.chargeTime = chargeTime;
	}
	
	/**
	 * Test if this sensor to charge is the given sensor.
	 *
	 * @param sensor The sensor to test.
	 *
	 * @return True if it is the same sensor, false otherwise.
	 */
	public boolean is(final Sensor sensor){
		return Objects.equals(this.sensor, sensor);
	}
	
	@Override
	public String toString(){
		return new ReflectionToStringBuilder(this).toString();
	}
	
	/**
	 * Get the charge time.
	 *
	 * @return The charge time.
	 */
	double getChargeTime(){
		return chargeTime;
	}
	
	/**
	 * Set the charge time of the sensor.
	 *
	 * @param chargeTime The charge time.
	 */
	void setChargeTime(final double chargeTime){
		this.chargeTime = Math.max(0, chargeTime);
	}
	
	/**
	 * Get the sensor.
	 *
	 * @return The sensor.
	 */
	public Sensor getSensor(){
		return sensor;
	}
}
