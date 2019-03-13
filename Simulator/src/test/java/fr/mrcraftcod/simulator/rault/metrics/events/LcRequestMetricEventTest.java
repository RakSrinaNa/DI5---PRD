package fr.mrcraftcod.simulator.rault.metrics.events;

import fr.mrcraftcod.simulator.Environment;
import fr.mrcraftcod.simulator.sensors.Sensor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class LcRequestMetricEventTest{
	private Environment environment;
	private Sensor sensor;
	
	@BeforeEach
	void setUp(){
		this.environment = new Environment(null);
		this.sensor = new Sensor(environment);
	}
	
	@ParameterizedTest
	@ValueSource(ints = {
			0,
			1,
			10,
			50,
			17
	})
	void construct(final int time){
		final var event = new LcRequestMetricEvent(environment, time, sensor);
		assertEquals(environment, event.getEnvironment());
		assertEquals(time, event.getTime());
		assertEquals(sensor, event.getElement());
		assertNull(event.getNewValue());
	}
}