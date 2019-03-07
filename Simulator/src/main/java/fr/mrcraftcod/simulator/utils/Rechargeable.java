package fr.mrcraftcod.simulator.utils;

/**
 * Represents an entity that can be recharged.
 * <p>
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2019-02-14.
 *
 * @author Thomas Couchoud
 * @since 2019-02-14
 */
public interface Rechargeable{
	/**
	 * Remove some capacity to this element.
	 *
	 * @param amount The amount to remove.
	 */
	default void removeCapacity(final double amount){
		final var newCapacity = getCurrentCapacity() - amount;
		if(newCapacity > getMaxCapacity()){
			setCurrentCapacity(getMaxCapacity());
		}
		else if(newCapacity < 0){
			setCurrentCapacity(0);
		}
		else{
			setCurrentCapacity(newCapacity);
		}
	}
	
	/**
	 * Get the current capacity.
	 *
	 * @return The current capacity.
	 */
	double getCurrentCapacity();
	
	/**
	 * Get the maximum capacity of this element.
	 *
	 * @return The max capacity.
	 */
	double getMaxCapacity();
	
	/**
	 * Set the current capacity of this element.
	 *
	 * @param currentCapacity The capacity to set.
	 */
	void setCurrentCapacity(final double currentCapacity);
	
	/**
	 * Add some capacity to this element.
	 *
	 * @param amount The amount to add.
	 */
	default void addCapacity(final double amount){
		final var newCapacity = getCurrentCapacity() + amount;
		if(newCapacity > getMaxCapacity()){
			setCurrentCapacity(getMaxCapacity());
		}
		else if(newCapacity < 0){
			setCurrentCapacity(0);
		}
		else{
			setCurrentCapacity(newCapacity);
		}
	}
}
