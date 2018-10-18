package fr.mrcraftcod.simulator.utils;

/**
 * Represent an element in the environment that is uniquely identifiable.
 * <p>
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-10-04.
 *
 * @author Thomas Couchoud
 * @since 1.0.0
 */
public interface Identifiable{
	/**
	 * Get a unique ID among all Identifiable objects.
	 *
	 * @return The unique ID.
	 *
	 * @since 1.0.0
	 */
	default String getUniqueIdentifier(){
		return String.format("%s[%d]", getClass().getName(), getID());
	}
	
	/**
	 * Get an ID that should be unique among the instances of a same class.
	 *
	 * @return The ID.
	 *
	 * @since 1.0.0
	 */
	int getID();
}
