package fr.mrcraftcod.simulator.rault.events;

import fr.mrcraftcod.simulator.Environment;
import fr.mrcraftcod.simulator.rault.routing.ChargerTour;
import fr.mrcraftcod.simulator.simulation.SimulationEvent;
import fr.mrcraftcod.simulator.simulation.Simulator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Optional;

/**
 * Event when a charger is charging.
 * <p>
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-11-07.
 *
 * @author Thomas Couchoud
 *
 */
class TourChargeEvent extends SimulationEvent{
	private static final Logger LOGGER = LoggerFactory.getLogger(TourChargeEvent.class);
	private final ChargerTour tour;
	
	/**
	 * Constructor.
	 *
	 * @param time The time of the event.
	 * @param tour The tour.
	 */
	TourChargeEvent(final double time, final ChargerTour tour){
		super(time);
		this.tour = tour;
	}
	
	@Override
	public void accept(final Environment environment){
		Optional.ofNullable(tour.getStops().poll()).ifPresentOrElse(chargingStop -> {
			LOGGER.debug("Charger {} charging {}", tour.getCharger(), chargingStop);
			Simulator.getUnreadableQueue().add(new TourTravelEvent(getTime() + chargingStop.getChargingTime(), tour)); //TODO: Discharge charger
		}, () -> Simulator.getUnreadableQueue().add(new TourTravelEvent(getTime(), tour)));
	}
}
