package fr.mrcraftcod.simulator.utils;

import fr.mrcraftcod.simulator.positions.Position;

/**
 * An element that have a position.
 * <p>
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2019-01-17.
 *
 * @author Thomas Couchoud
 * @since 2019-01-17
 */
public interface Positionable extends Identifiable{
	/**
	 * Get the position of the element.
	 *
	 * @return The position.
	 */
	Position getPosition();
}
