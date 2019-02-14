package fr.mrcraftcod.simulator.positions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class PositionTest{
	
	@Test
	void distanceTo(){
		final var position1 = new Position(0, 0);
		final var position2 = new Position(2, 0);
		final var position3 = new Position(1, 1);
		assertEquals(2.0, position1.distanceTo(position2), 0.01);
		assertEquals(Math.sqrt(2), position1.distanceTo(position3), 0.01);
	}
	
	@Test
	void equals1(){
		final var position1 = new Position(0, 0);
		final var position2 = new Position(0, 0);
		final var position3 = new Position(1, 0);
		final var position4 = new Position(1, 1);
		final var position5 = new Position(0, 1);
		assertEquals(position1, position1);
		assertEquals(position1, position2);
		assertNotEquals(position1, position3);
		assertNotEquals(position1, position4);
		assertNotEquals(position1, position5);
		assertNotEquals(position1, new Object());
	}
	
	@ParameterizedTest
	@ValueSource(ints = {
			0,
			-1,
			-7,
			-10,
			1,
			7,
			10
	})
	void getX(final int x){
		final var position1 = new Position(x, 0);
		final var position2 = new Position(0, 0);
		position2.setX(x);
		assertEquals(x, position1.getX());
		assertEquals(x, position2.getX());
	}
	
	@ParameterizedTest
	@ValueSource(ints = {
			0,
			-1,
			-7,
			-10,
			1,
			7,
			10
	})
	void getY(final int y){
		final var position1 = new Position(0, y);
		final var position2 = new Position(0, 0);
		position2.setY(y);
		assertEquals(y, position1.getY());
		assertEquals(y, position2.getY());
	}
}