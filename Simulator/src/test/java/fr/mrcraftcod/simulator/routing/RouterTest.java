package fr.mrcraftcod.simulator.routing;

import fr.mrcraftcod.simulator.Environment;
import fr.mrcraftcod.simulator.sensors.Sensor;
import fr.mrcraftcod.simulator.utils.Identifiable;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;

class RouterTest{
	static class RouterT extends Router
	{
		public RouterT(){
			super();
		}
		
		public RouterT(@NotNull final Environment environment){
			super(environment);
		}
		
		@Override
		public boolean haveSameValues(final Identifiable identifiable){
			return this.getClass().isInstance(identifiable);
		}
		
		@Override
		public boolean route(final Environment environment, final Collection<? extends Sensor> sensors){
			return Objects.isNull(environment);
		}
	}
	
	@Test
	void construct(){
		final var router = new RouterT();
		assertEquals(1, router.getID());
		assertFalse(router.route(new Environment(null, "junit-test"), List.of()));
		assertTrue(router.route(null, List.of()));
		
		final var router2 = new RouterT(new Environment(null, "junit-test"));
		assertEquals(2, router2.getID());
		assertFalse(router2.route(new Environment(null, "junit-test"), List.of()));
		assertTrue(router2.route(null, List.of()));
	}
}