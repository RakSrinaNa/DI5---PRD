package fr.mrcraftcod.simulator.positions;

import fr.mrcraftcod.simulator.Environment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomPositionTest{
	private Environment environment;
	
	@BeforeEach
	void setUp(){
		this.environment = new Environment(null, "junit-test");
	}
	
	@RepeatedTest(50)
	void construct(){
		final var position = new RandomPosition(environment, -10, -5, 5, 10);
		assertTrue(position.getX() >= -10);
		assertTrue(position.getX() <= -5);
		assertTrue(position.getY() >= 5);
		assertTrue(position.getY() <= 10);
	}
}