package fr.mrcraftcod.simulator.rault.routing;

import fr.mrcraftcod.simulator.Environment;
import fr.mrcraftcod.simulator.chargers.Charger;
import fr.mrcraftcod.simulator.sensors.Sensor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

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
	
	@SuppressWarnings("Duplicates")
	@Override
	protected Collection<ChargingStop> getChargingStops(final Collection<? extends Charger> chargers, final Collection<? extends Sensor> sensors, final Collection<StopLocation> stopLocations){
		final var chargingStops = new ArrayList<ChargingStop>();
		
		final var chargingSensors = sensors.stream().map(s -> {
			final var distance = stopLocations.stream().filter(stopLocation -> stopLocation.contains(s)).mapToDouble(stopLocation -> stopLocation.getPosition().distanceTo(s.getPosition())).max().orElse(1);
			var chargeTime = (s.getMaxCapacity() - s.getCurrentCapacity()) / chargers.stream().min(Comparator.comparingDouble(Charger::getTransmissionPower)).map(c -> c.getReceivedPower(distance)).orElse(1D);
			return new ChargingSensor(s, chargeTime);
		}).collect(Collectors.toList());
		
		final var stopLocationsLeft = new LinkedList<>(stopLocations);
		StopLocation stopLocation;
		while((stopLocation = stopLocationsLeft.poll()) != null){
			final var unknownChargingSensors = new ArrayList<ChargingSensor>();
			final var chargeTime = new AtomicReference<>(0D);
			for(final Sensor sensor : stopLocation.getSensors()){
				chargingSensors.stream().filter(c -> c.is(sensor)).findFirst().ifPresent(chargingSensor -> {
					if(stopLocationsLeft.stream().anyMatch(s -> s.contains(sensor))){
						unknownChargingSensors.add(chargingSensor);
					}
					else{
						chargeTime.set(Math.max(chargeTime.get(), chargingSensor.getChargeTime()));
					}
				});
			}
			unknownChargingSensors.forEach(s -> s.setChargeTime(s.getChargeTime() - chargeTime.get()));
			chargingStops.add(new ChargingStop(stopLocation, chargeTime.get()));
		}
		
		return chargingStops;
	}
}
