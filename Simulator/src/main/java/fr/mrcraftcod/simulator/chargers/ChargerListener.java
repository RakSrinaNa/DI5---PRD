package fr.mrcraftcod.simulator.chargers;

import org.jetbrains.annotations.NotNull;

/**
 * Define an interface to listen to events inside a charger.
 * <p>
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-10-18.
 *
 * @author Thomas Couchoud
 */
public interface ChargerListener{
	/**
	 * Called when a charger current capacity changes.
	 *
	 * @param charger            The charger concerned.
	 * @param newCurrentCapacity The new capacity value
	 */
	void onChargerCurrentCapacityChange(@NotNull final Charger charger, double newCurrentCapacity);
}
