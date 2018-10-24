package fr.mrcraftcod.simulator;

import fr.mrcraftcod.simulator.utils.Identifiable;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import java.util.*;

/**
 * Represents the environment of the simulation.
 * <p>
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-10-18.
 *
 * @author Thomas Couchoud
 * @since 2018-10-18
 */
public class Environment{
	private final List<Identifiable> elements;
	private final Random random;
	private Long seed;
	
	/**
	 * Constructor.
	 */
	public Environment(){
		this.elements = new LinkedList<>();
		this.random = new Random();
	}
	
	/**
	 * Add a new identifiable element.
	 *
	 * @param instance The element to add.
	 *
	 * @return True if the element was added, false otherwise (duplicated).
	 */
	public boolean add(final Identifiable instance){
		return this.elements.add(instance);
	}
	
	@Override
	public String toString(){
		return new ReflectionToStringBuilder(this).toString();
	}
	
	/**
	 * Set the seed for the random generation of this environment.
	 *
	 * @param seed The seed to set.
	 */
	public void setSeed(final Long seed){
		this.seed = seed;
		this.getRandom().setSeed(seed);
	}
	
	/**
	 * Get the random object of this environment.
	 *
	 * @return The random object.
	 */
	public Random getRandom(){
		return this.random;
	}
	
	@Override
	public boolean equals(final Object obj){
		return obj instanceof Environment && Objects.equals(getSeed(), ((Environment) obj).getSeed()) && elements.equals(((Environment) obj).getElements());
	}
	
	/**
	 * Get the seed that have been set for this environment.
	 *
	 * @return The seed.
	 */
	public Long getSeed(){
		return this.seed;
	}
	
	/**
	 * Get the elements of the environment.
	 *
	 * @return The elements.
	 */
	public List<Identifiable> getElements(){
		return this.elements;
	}
}
