package fr.mrcraftcod.simulator.settings.settingsources;

import fr.mrcraftcod.simulator.Environment;
import fr.mrcraftcod.simulator.chargers.Charger;
import fr.mrcraftcod.simulator.positions.RandomPosition;
import fr.mrcraftcod.simulator.sensors.Sensor;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-10-24.
 *
 * @author Thomas Couchoud
 * @since 2018-10-24
 */
public class SettingsSource1 extends SettingsSource{
	private final Environment environment;
	
	public SettingsSource1(){
		this.environment = new Environment();
		this.environment.setSeed(42L);
		this.environment.add(new Sensor(23, 40, 3, new RandomPosition(this.environment, 5)));
		for(var i = 0; i < 3; i++){
			this.environment.add(new Charger(450, 500, 40.5, 3));
		}
	}
	
	@Override
	public Environment getEnvironment(){
		return this.environment;
	}
	
	@Override
	protected String getSettingsFileName(){
		return "settings1.json";
	}
}
