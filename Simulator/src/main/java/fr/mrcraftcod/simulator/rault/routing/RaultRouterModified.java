package fr.mrcraftcod.simulator.rault.routing;

import fr.mrcraftcod.simulator.Environment;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Router from Rault's paper.
 * <p>
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-11-07.
 *
 * @author Thomas Couchoud
 */
@SuppressWarnings("unused")
public class RaultRouterModified extends RaultRouter{
	private static final Logger LOGGER = LoggerFactory.getLogger(RaultRouterModified.class);
	
	/**
	 * Constructor.
	 */
	public RaultRouterModified(){
		super();
	}
	
	/**
	 * Constructor used by the JSON filler.
	 *
	 * @param environment The environment the router is in.
	 */
	public RaultRouterModified(@NotNull final Environment environment){
		super(environment);
	}
}
