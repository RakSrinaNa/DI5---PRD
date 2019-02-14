package fr.mrcraftcod.simulator.rault.metrics.events;

import fr.mrcraftcod.simulator.Environment;
import fr.mrcraftcod.simulator.chargers.Charger;
import fr.mrcraftcod.simulator.metrics.IdentifiableMetricEvent;
import fr.mrcraftcod.simulator.rault.routing.ChargerTour;
import org.jetbrains.annotations.NotNull;

/**
 * Fired when a tour ends.
 * <p>
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-11-22.
 *
 * @author Thomas Couchoud
 * @since 2018-11-22
 */
public class TourEndMetricEvent extends IdentifiableMetricEvent<ChargerTour, Charger>{
	/**
	 * Constructor.
	 *
	 * @param environment The environment.
	 * @param time        The time of the event.
	 * @param charger     The charger.
	 * @param chargerTour The tour.
	 */
	public TourEndMetricEvent(final Environment environment, final double time, @NotNull final Charger charger, @NotNull final ChargerTour chargerTour){
		super(environment, time, charger, chargerTour);
	}
}
