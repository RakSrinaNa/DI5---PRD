package fr.mrcraftcod.simulator.rault.routing;

import fr.mrcraftcod.simulator.chargers.Charger;
import org.apache.commons.lang3.builder.ToStringBuilder;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Represents the tour of a charger.
 * <p>
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-11-07.
 *
 * @author Thomas Couchoud
 */
public class ChargerTour{
	private final Charger charger;
	private LinkedList<ChargingStop> stops;
	private double accumulatedTime;
	private List<ChargerTour> parent;
	
	/**
	 * Constructor.
	 *
	 * @param charger The constructor assigned for the tour.
	 */
	public ChargerTour(final Charger charger){
		this.charger = charger;
		this.stops = new LinkedList<>();
		this.accumulatedTime = 0;
	}
	
	/**
	 * Add a stop to the tour.
	 *
	 * @param chargingStop The stop to add.
	 */
	public void addStop(final ChargingStop chargingStop){
		this.stops.add(chargingStop);
		this.accumulatedTime += chargingStop.getChargingTime();
	}
	
	/**
	 * Calculate the distance from the head to another stop.
	 *
	 * @param chargingStop The stop to go to.
	 *
	 * @return The distance, if no head is there, {@link Double#MAX_VALUE} is returned.
	 */
	double distanceTo(final ChargingStop chargingStop){
		return Optional.ofNullable(getStops().peek()).map(c1 -> c1.getStopLocation().getPosition().distanceTo(chargingStop.getStopLocation().getPosition())).orElse(Double.MAX_VALUE);
	}
	
	/**
	 * Change the order of the stops.
	 *
	 * @param indices The indices of the stops.
	 *
	 * @throws IllegalArgumentException If the indices doesn't have the same size as the number of stops.
	 */
	public void newOrder(final List<Integer> indices) throws IllegalArgumentException{
		if(Objects.equals(indices.size(), this.getStops().size())){
			
			final var newStops = new LinkedList<ChargingStop>();
			for(final var index : indices){
				newStops.add(stops.get(index));
			}
			this.stops = newStops;
		}
		else{
			throw new IllegalArgumentException("New order doesn't have the same size");
		}
	}
	
	public void setParent(final List<ChargerTour> tours){
		this.parent = tours;
	}
	
	public List<ChargerTour> getParent(){
		return parent;
	}
	
	public void setArrivalTimes(final List<Double> arrivalTimes){
		if(Objects.equals(arrivalTimes.size(), this.getStops().size())){
			for(var i = 0; i < arrivalTimes.size(); i++){
				getStops().get(i).setChargerArrivalTime(arrivalTimes.get(i));
			}
		}
		else{
			throw new IllegalArgumentException("New arrival times doesn't have the same size");
		}
	}
	
	/**
	 * Get the stops of the tour.
	 *
	 * @return The stops.
	 */
	public LinkedList<ChargingStop> getStops(){
		return this.stops;
	}
	
	@Override
	public String toString(){
		return new ToStringBuilder(this).append("charger", charger).append("stops_count", stops.size()).append("accumulatedTime", accumulatedTime).toString();
	}
	
	/**
	 * Get the accumulated charge time.
	 *
	 * @return The accumulated time.
	 */
	double getAccumulatedTime(){
		return accumulatedTime;
	}
	
	/**
	 * Get the charger.
	 *
	 * @return The charger.
	 */
	public Charger getCharger(){
		return charger;
	}
	
	@Override
	public int hashCode(){
		return Objects.hash(charger);
	}
	
	@Override
	public boolean equals(final Object o){
		if(this == o){
			return true;
		}
		return o instanceof ChargerTour && Objects.equals(charger, ((ChargerTour) o).getCharger());
	}
}
