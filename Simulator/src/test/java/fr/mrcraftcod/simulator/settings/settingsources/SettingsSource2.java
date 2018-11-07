package fr.mrcraftcod.simulator.settings.settingsources;

import fr.mrcraftcod.simulator.Environment;
import fr.mrcraftcod.simulator.chargers.Charger;
import fr.mrcraftcod.simulator.positions.Position;
import fr.mrcraftcod.simulator.sensors.Sensor;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-10-24.
 *
 * @author Thomas Couchoud
 * @since 2018-10-24
 */
public class SettingsSource2 extends SettingsSource{
	private final Environment environment;
	
	public SettingsSource2(){
		this.environment = new Environment();
		this.environment.setSeed(42L);
		this.environment.setEnd(10);
		for(var i = 0; i < 2; i++){
			this.environment.add(new Sensor(29, 45, 10, new Position(10, 20), 1));
		}
		this.environment.add(new Charger(20, 40, 25, 1, 34));
	}
	
	@Override
	public Environment getEnvironment(){
		return this.environment;
	}
	
	@Override
	protected String getSettingsFileName(){
		return "settings2.json";
	}
}
