package fr.mrcraftcod.simulator.exceptions;

import fr.mrcraftcod.simulator.Environment;
import fr.mrcraftcod.simulator.sensors.Sensor;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SettingsParserExceptionTest{
	private Sensor parsable;
	private JSONObject json;
	
	@BeforeEach
	void setUp(){
		final var environment = new Environment(null);
		this.parsable = new Sensor(environment);
		this.json = new JSONObject("{\"a\": 5}");
	}
	
	@Test
	void construct1(){
		final var origin = new Exception();
		final var exception = new SettingsParserException(parsable.getClass(), json, origin);
		assertEquals(json, exception.getJson());
		assertEquals(parsable.getClass(), exception.getKlass());
		assertEquals(origin, exception.getCause());
		assertNotNull(exception.getMessage());
	}
	
	@Test
	void construct2(){
		final var exception = new SettingsParserException("Test");
		assertEquals("Test", exception.getMessage());
		assertNull(exception.getJson());
		assertNull(exception.getKlass());
		assertNull(exception.getCause());
	}
}