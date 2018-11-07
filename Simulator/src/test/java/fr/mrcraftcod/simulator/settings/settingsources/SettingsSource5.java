package fr.mrcraftcod.simulator.settings.settingsources;

import fr.mrcraftcod.simulator.Environment;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-10-24.
 *
 * @author Thomas Couchoud
 * @since 2018-10-24
 */
public class SettingsSource5 extends SettingsSource{
	private final Environment environment;
	
	public SettingsSource5(){
		this.environment = null;
	}
	
	@Override
	public Environment getEnvironment(){
		return this.environment;
	}
	
	@Override
	protected String getSettingsFileName(){
		return "settings5fail.json";
	}
}
