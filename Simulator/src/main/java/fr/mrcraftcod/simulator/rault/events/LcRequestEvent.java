package fr.mrcraftcod.simulator.rault.events;

import fr.mrcraftcod.simulator.Environment;
import fr.mrcraftcod.simulator.metrics.MetricEventDispatcher;
import fr.mrcraftcod.simulator.rault.metrics.events.LcRequestMetricEvent;
import fr.mrcraftcod.simulator.routing.Router;
import fr.mrcraftcod.simulator.sensors.Sensor;
import fr.mrcraftcod.simulator.simulation.SimulationEvent;
import fr.mrcraftcod.simulator.simulation.Simulator;

/**
 * The event of a Lc request from a sensor.
 * <p>
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-11-07.
 *
 * @author Thomas Couchoud
 */
public class LcRequestEvent extends SimulationEvent{
	private final Sensor sensor;
	
	/**
	 * Constructor.
	 *
	 * @param time The time of the event.
	 */
	public LcRequestEvent(final double time, final Sensor sensor){
		super(time);
		this.sensor = sensor;
	}
	
	@Override
	public void accept(final Environment environment)
	{
		MetricEventDispatcher.dispatchEvent(new LcRequestMetricEvent(getTime(), getSensor()));
		environment.getElements(Router.class).stream().findFirst().map(r -> r.route(environment, LrRequestEvent.getRequestingSensors())).ifPresent(result -> {
			if(result)
			{
				LrRequestEvent.getRequestingSensors().clear();
				Simulator.removeAllEventsOfClass(LrRequestEvent.class);
				Simulator.removeAllEventsOfClass(LcRequestEvent.class);
			}
			else
			{
				Simulator.getUnreadableQueue().add(new LcRequestEvent(Simulator.getCurrentTime() + 1, sensor));
			}
		});
	}
	
	public Sensor getSensor(){
		return sensor;
	}
}
