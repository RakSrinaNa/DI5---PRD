package fr.mrcraftcod.simulator.settings;

import fr.mrcraftcod.simulator.SimulationParameters;
import fr.mrcraftcod.simulator.exceptions.SettingsParserException;
import fr.mrcraftcod.simulator.settings.settingsources.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-10-24.
 *
 * @author Thomas Couchoud
 * @since 2018-10-24
 */
class SimulationParametersTest{
	private static Stream<? extends Arguments> settingsProvider(){
		return Stream.of(arguments(new SettingsSource1()), arguments(new SettingsSource2()));
	}
	
	private static Stream<? extends Arguments> failingSettingsProvider(){
		return Stream.of(arguments(new SettingsSource3()), arguments(new SettingsSource4()), arguments(new SettingsSource5()));
	}
	
	@ParameterizedTest(name = "Settings {0}")
	@MethodSource("settingsProvider")
	void loadFomFile(final SettingsSource settingsSource) throws Exception{
		final var params = SimulationParameters.loadFomFile(settingsSource.getJSONFile());
		final var trueEnv = settingsSource.getEnvironment();
		final var env = params.getEnvironment();
		assertEquals(trueEnv.getSeed(), env.getSeed(), "Seed isn't the same");
		assertEquals(trueEnv.getElements().size(), env.getElements().size(), "Environment doesn't have the same amount of elements");
		for(var i = 0; i < settingsSource.getEnvironment().getElements().size(); i++){
			assertTrue(trueEnv.getElements().get(i).haveSameValues(env.getElements().get(i)), "Elements are not the same at index " + i);
		}
	}
	
	@ParameterizedTest(name = "Failing settings {0}")
	@MethodSource("failingSettingsProvider")
	void failingLoadFomFile(final SettingsSource settingsSource){
		Assertions.assertThrows(SettingsParserException.class, () -> SimulationParameters.loadFomFile(settingsSource.getJSONFile()), "An exception should have been thrown");
	}
}