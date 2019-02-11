package fr.mrcraftcod.simulator.rault.routing;

import fr.mrcraftcod.simulator.chargers.Charger;
import fr.mrcraftcod.simulator.sensors.Sensor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.tuple.Pair;
import java.util.*;

/**
 * Represent a point to stop to charge sensors.
 * <p>
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-11-07.
 *
 * @author Thomas Couchoud
 */
public class ChargingStop{
	private final int ID;
	private static int NEXT_ID = 0;
	private final StopLocation stopLocation;
	private final double chargingTime;
	private final List<Pair<Double, Double>> forbiddenTimes;
	private final Set<ChargingStop> conflictZones;
	private double chargerArrivalTime = 0;
	private Charger charger;
	
	/**
	 * Constructor.
	 *
	 * @param stopLocation The stop location.
	 * @param chargingTime The time to charge the location.
	 */
	public ChargingStop(final StopLocation stopLocation, final double chargingTime){
		this.ID = NEXT_ID++;
		this.stopLocation = stopLocation;
		this.chargingTime = chargingTime;
		this.forbiddenTimes = new LinkedList<>();
		this.conflictZones = new HashSet<>();
	}
	
	@Override
	public String toString(){
		return new ToStringBuilder(this).append("stopLocation", stopLocation).append("chargingTime", chargingTime).append("forbiddenTimes_count", forbiddenTimes.size()).append("conflictZones_count", conflictZones.size()).append("chargerArrivalTime", chargerArrivalTime).toString();
	}
	
	public void addForbiddenTime(final double start, final double end){
		this.forbiddenTimes.add(Pair.of(start, end));
	}
	
	public void addConflictZone(final ChargingStop zone){
		this.conflictZones.add(zone);
	}
	
	@Override
	public int hashCode(){
		return Objects.hash(ID);
	}
	
	@Override
	public boolean equals(final Object o){
		return this == o || Objects.nonNull(o) && o instanceof ChargingStop && ID == ((ChargingStop) o).ID;
	}
	
	public boolean contains(final Sensor s){
		return getStopLocation().getSensors().contains(s);
	}
	
	public double getChargerArrivalTime(){
		return this.chargerArrivalTime;
	}
	
	public int getID(){
		return ID;
	}
	
	public void setChargerArrivalTime(final double time){
		this.chargerArrivalTime = time;
	}
	
	public Charger getCharger(){
		return this.charger;
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
	
	public void setCharger(final Charger charger){
		this.charger = charger;
	}
	
	public Set<ChargingStop> getConflictZones(){
		return this.conflictZones;
	}
}
