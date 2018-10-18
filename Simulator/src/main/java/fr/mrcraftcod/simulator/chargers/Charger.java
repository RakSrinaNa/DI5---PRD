package fr.mrcraftcod.simulator.chargers;

import fr.mrcraftcod.simulator.Environment;
import fr.mrcraftcod.simulator.utils.Identifiable;
import fr.mrcraftcod.simulator.utils.JSONParsable;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-10-18.
 *
 * @author Thomas Couchoud
 * @since 2018-10-18
 */
public class Charger implements JSONParsable<Charger>, Identifiable{
	private static final Logger LOGGER = LoggerFactory.getLogger(Charger.class);
	private final int ID;
	private static int NEXT_ID = 0;
	private double currentCapacity;
	private double maxCapacity;
	private double radius;
	private double transmissionPower;
	
	public Charger(final Environment environment){
		this(0, 0, 0, 0);
	}
	
	public Charger(final double currentCapacity, final double maxCapacity, final double radius, final double transmissionPower){
		this.ID = ++NEXT_ID;
		this.currentCapacity = currentCapacity;
		this.maxCapacity = maxCapacity;
		this.radius = radius;
		this.transmissionPower = transmissionPower;
		LOGGER.info("New charger created: {}", getUniqueIdentifier());
	}
	
	@Override
	public String getUniqueIdentifier(){
		return String.format("CHARG[%d]", getID());
	}
	
	private int getID(){
		return this.ID;
	}
	
	@Override
	public Charger fillFromJson(final Environment environment, final JSONObject json) throws Exception{
		this.radius = json.getDouble("radius");
		this.transmissionPower = json.getDouble("transmissionPower");
		this.maxCapacity = json.getDouble("maxCapacity");
		this.currentCapacity = json.getDouble("currentCapacity");
		return this;
	}
	
	@Override
	public String toString(){
		return new ReflectionToStringBuilder(this).toString();
	}
	
	public double getCurrentCapacity(){
		return currentCapacity;
	}
	
	public double getMaxCapacity(){
		return maxCapacity;
	}
	
	public double getRadius(){
		return radius;
	}
	
	public double getTransmissionPower(){
		return transmissionPower;
	}
}
