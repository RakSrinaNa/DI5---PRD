package fr.mrcraftcod.simulator.rault.routing;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * Represent a point to stop to charge sensors.
 * <p>
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-11-07.
 *
 * @author Thomas Couchoud
 * @since 2018-11-07
 */
public class ChargingStop{
	private final StopLocation stopLocation;
	private final double chargingTime;
	
	/**
	 * Constructor.
	 *
	 * @param stopLocation The stop location.
	 * @param chargingTime The time to charge the location.
	 */
	ChargingStop(final StopLocation stopLocation, final double chargingTime){
		this.stopLocation = stopLocation;
		this.chargingTime = chargingTime;
	}
	
	@Override
	public String toString(){
		return new ReflectionToStringBuilder(this).toString();
	}
	
	/**
	 * Get the charging time.
	 *
	 * @return The charging time.
	 */
	public double getChargingTime(){
		return chargingTime;
	}
	
	/**
	 * Get the stop location.
	 *
	 * @return The stop location.
	 */
	public StopLocation getStopLocation(){
		return stopLocation;
	}
}
