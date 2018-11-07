package fr.mrcraftcod.simulator.routing;

import fr.mrcraftcod.simulator.Environment;
import fr.mrcraftcod.simulator.sensors.Sensor;
import fr.mrcraftcod.simulator.utils.Identifiable;
import fr.mrcraftcod.simulator.utils.JSONParsable;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Collection;

/**
 * Represents a router.
 * <p>
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-11-07.
 *
 * @author Thomas Couchoud
 * @since 2018-11-07
 */
public abstract class Router implements Identifiable, JSONParsable<Router>{
	private static final Logger LOGGER = LoggerFactory.getLogger(Router.class);
	private final int ID;
	private static int NEXT_ID = 0;
	
	/**
	 * Constructor used by the JSON filler.
	 *
	 * @param environment The environment the router is in.
	 *
	 *
	 */
	public Router(@NotNull final Environment environment){
		this();
	}
	
	/**
	 * Constructor.
	 */
	protected Router(){
		this.ID = ++NEXT_ID;
		LOGGER.debug("New router created: {}", getUniqueIdentifier());
	}
	
	/**
	 * Handle sensors to recharge.
	 *
	 * @param environment The environment.
	 * @param sensors     The sensors to recharge.
	 */
	public abstract void route(final Environment environment, final Collection<? extends Sensor> sensors);
	
	@Override
	public Router fillFromJson(@NotNull final Environment environment, @NotNull final JSONObject json) throws IllegalArgumentException{
		return this;
	}
	
	@Override
	public int getID(){
		return this.ID;
	}
}
