package fr.mrcraftcod.simulator.rault.routing;

import fr.mrcraftcod.simulator.chargers.Charger;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import java.util.LinkedList;
import java.util.List;
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
	
	/**
	 * Constructor.
	 *
	 * @param charger The constructor assigned for the tour.
	 */
	ChargerTour(final Charger charger){
		this.charger = charger;
		this.stops = new LinkedList<>();
		this.accumulatedTime = 0;
	}
	
	/**
	 * Add a stop to the tour.
	 *
	 * @param chargingStop The stop to add.
	 */
	void addStop(final ChargingStop chargingStop){
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
	 */
	void newOrder(final List<Integer> indices){
		final var newStops = new LinkedList<ChargingStop>();
		for(final var index : indices){
			newStops.add(stops.get(index));
		}
		this.stops = newStops;
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
		return new ReflectionToStringBuilder(this).toString();
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
}
