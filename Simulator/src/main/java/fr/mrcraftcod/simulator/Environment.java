package fr.mrcraftcod.simulator;

import fr.mrcraftcod.simulator.utils.Identifiable;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Represents the environment of the simulation.
 * <p>
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-10-18.
 *
 * @author Thomas Couchoud
 *
 */
public class Environment{
	private final List<Identifiable> elements;
	private final Random random;
	private Long seed;
	private int end;
	
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
	
	@Override
	public boolean equals(final Object obj){
		if(obj instanceof Environment){
			final var env = ((Environment) obj);
			return Objects.equals(getSeed(), env.getSeed()) && Objects.equals(getEnd(), env.getEnd()) && elements.equals(env.getElements());
		}
		return false;
	}
	
	/**
	 * Get the end date of the simulation.
	 *
	 * @return The end date.
	 */
	public int getEnd(){
		return this.end;
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
	
	/**
	 * Set the end date of the simulation.
	 *
	 * @param end The end date.
	 */
	public void setEnd(final int end){
		if(end <= 0){
			throw new IllegalArgumentException("End date must be positive");
		}
		this.end = end;
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
	
	/**
	 * Get the elements of the environment.
	 *
	 * @param klass The class of the elements to get.
	 * @param <T>   The type of the elements.
	 *
	 * @return The elements.
	 */
	public <T extends Identifiable> List<? extends T> getElements(final Class<? extends T> klass){
		return this.elements.stream().filter(klass::isInstance).map(klass::cast).collect(Collectors.toList());
	}
}
