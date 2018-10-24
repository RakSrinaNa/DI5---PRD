package fr.mrcraftcod.simulator.settings.settingsources;

import fr.mrcraftcod.simulator.Environment;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-10-24.
 *
 * @author Thomas Couchoud
 * @since 2018-10-24
 */
public abstract class SettingsSource{
	public abstract Environment getEnvironment();
	
	public Path getJSONFile() throws URISyntaxException{
		return Paths.get(SettingsSource.class.getResource("/settings/" + getSettingsFileName()).toURI());
	}
	
	protected abstract String getSettingsFileName();
}
