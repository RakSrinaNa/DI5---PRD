package fr.mrcraftcod.simulator.rault.routing;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.tuple.Pair;
import java.util.LinkedList;
import java.util.List;

/**
 * Represent a point to stop to charge sensors.
 * <p>
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-11-07.
 *
 * @author Thomas Couchoud
 */
public class ChargingStop{
	private final StopLocation stopLocation;
	private final double chargingTime;
	private final List<Pair<Double, Double>> forbiddenTimes;
	private final List<ChargingStop> conflictZones;
	private double chargerArrivalTime = 0;
	
	/**
	 * Constructor.
	 *
	 * @param stopLocation The stop location.
	 * @param chargingTime The time to charge the location.
	 */
	public ChargingStop(final StopLocation stopLocation, final double chargingTime){
		this.stopLocation = stopLocation;
		this.chargingTime = chargingTime;
		this.forbiddenTimes = new LinkedList<>();
		this.conflictZones = new LinkedList<>();
	}
	
	@Override
	public String toString(){
		return new ReflectionToStringBuilder(this).toString();
	}
	
	public void addForbiddenTime(final double start, final double end){
		this.forbiddenTimes.add(Pair.of(start, end));
	}
	
	public double getChargerArrivalTime(){
		return this.chargerArrivalTime;
	}
	
	public void setChargerArrivalTime(final double time){
		this.chargerArrivalTime = time;
	}
	
	public List<ChargingStop> getConflictZones(){
		return this.conflictZones;
	}
	
	public List<Pair<Double, Double>> getForbiddenTimes(){
		return forbiddenTimes;
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
