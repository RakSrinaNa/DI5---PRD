package fr.mrcraftcod.simulator.rault.sensors;

import fr.mrcraftcod.simulator.Environment;
import fr.mrcraftcod.simulator.rault.events.LcRequestEvent;
import fr.mrcraftcod.simulator.rault.events.LrRequestEvent;
import fr.mrcraftcod.simulator.sensors.Sensor;
import fr.mrcraftcod.simulator.sensors.SensorListener;
import org.jetbrains.annotations.NotNull;

/**
 * Listener of LrLcSensors to send Lc and Lr events.
 * <p>
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-11-07.
 *
 * @author Thomas Couchoud
 */
public class LrLcSensorListener implements SensorListener{
	private final LrLcSensor sensor;
	private boolean hasRequestedLc;
	private boolean hasRequestedLr;
	
	/**
	 * Constructor.
	 *
	 * @param sensor The sensor being watched.
	 */
	LrLcSensorListener(final LrLcSensor sensor){
		this.sensor = sensor;
		this.hasRequestedLc = false;
		this.hasRequestedLr = false;
	}
	
	@Override
	public void onSensorCurrentCapacityChange(final Environment environment, @NotNull final Sensor sensor, final double oldCurrentCapacity, final double newCurrentCapacity){
		if(!hasRequestedLr){
			if(newCurrentCapacity <= this.sensor.getLr()){
				environment.getSimulator().getUnreadableQueue().add(new LrRequestEvent(environment.getSimulator().getCurrentTime(), this.sensor));
				hasRequestedLr = true;
			}
		}
		if(!hasRequestedLc && !this.sensor.isPlannedForCharging()){
			if(newCurrentCapacity <= this.sensor.getLc()){
				environment.getSimulator().getUnreadableQueue().add(new LcRequestEvent(environment.getSimulator().getCurrentTime(), sensor));
				hasRequestedLc = true;
			}
		}
		if(hasRequestedLr && newCurrentCapacity >= this.sensor.getLr()){
			this.hasRequestedLr = false;
			this.hasRequestedLc = false;
		}
	}
}
