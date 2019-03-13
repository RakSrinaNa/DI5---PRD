package fr.mrcraftcod.simulator.rault.events;

import fr.mrcraftcod.simulator.Environment;
import fr.mrcraftcod.simulator.rault.metrics.events.LcRequestMetricEvent;
import fr.mrcraftcod.simulator.routing.Router;
import fr.mrcraftcod.simulator.sensors.Sensor;
import fr.mrcraftcod.simulator.simulation.SimulationEvent;

/**
 * The event of a Lc request from a sensor.
 * <p>
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-11-07.
 *
 * @author Thomas Couchoud
 */
@SuppressWarnings("WeakerAccess")
public class LcRequestEvent extends SimulationEvent{
	private final Sensor sensor;
	
	/**
	 * Constructor.
	 *
	 * @param time   The time of the event.
	 * @param sensor The sensor that made the request.
	 */
	public LcRequestEvent(final double time, final Sensor sensor){
		super(time);
		this.sensor = sensor;
	}
	
	@Override
	public void accept(final Environment environment){
		LrRequestEvent.getRequestingSensors().add(getSensor());
		environment.getSimulator().getMetricEventDispatcher().dispatchEvent(new LcRequestMetricEvent(environment, getTime(), getSensor()));
		environment.getElements(Router.class).stream().findFirst().map(r -> r.route(environment, LrRequestEvent.getRequestingSensors())).ifPresent(result -> {
			if(result){
				LrRequestEvent.getRequestingSensors().clear();
				environment.getSimulator().removeAllEventsOfClass(LcRequestEvent.class);
			}
			else{
				environment.getSimulator().removeAllEventsOfClass(LcRequestEvent.class);
				environment.getSimulator().getUnreadableQueue().add(new LcRequestEvent(environment.getSimulator().getCurrentTime() + 1, sensor));
			}
		});
	}
	
	/**
	 * The sensor that made the request.
	 *
	 * @return The sensor.
	 */
	public Sensor getSensor(){
		return sensor;
	}
}
