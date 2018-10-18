package fr.mrcraftcod.simulator;

import fr.mrcraftcod.simulator.utils.Identifiable;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-10-18.
 *
 * @author Thomas Couchoud
 * @since 2018-10-18
 */
public class Environment{
	private final List<Identifiable> elements;
	private final Random random;
	
	public Environment(){
		this.elements = new ArrayList<>();
		this.random = new Random();
	}
	
	public void add(final Identifiable instance){
		this.elements.add(instance);
	}
	
	@Override
	public String toString(){
		return new ReflectionToStringBuilder(this).toString();
	}
	
	public void setSeed(final Long seed){
		this.getRandom().setSeed(seed);
	}
	
	public Random getRandom(){
		return this.random;
	}
}
