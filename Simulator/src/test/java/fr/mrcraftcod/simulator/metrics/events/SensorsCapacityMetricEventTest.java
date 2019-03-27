package fr.mrcraftcod.simulator.metrics.events;

import fr.mrcraftcod.simulator.Environment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SensorsCapacityMetricEventTest{
	private Environment environment;
	
	@BeforeEach
	void setUp(){
		this.environment = new Environment(null, "junit-test");
	}
	
	@ParameterizedTest
	@ValueSource(ints = {
			0,
			1,
			5,
			7,
			10,
			17
	})
	void construct(final int time){
		final var event = new SensorsCapacityMetricEvent(environment, time);
		assertEquals(environment, event.getEnvironment());
		assertEquals(time, event.getTime());
	}
}