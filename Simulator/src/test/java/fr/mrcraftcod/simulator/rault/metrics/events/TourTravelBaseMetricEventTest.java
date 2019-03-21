package fr.mrcraftcod.simulator.rault.metrics.events;

import fr.mrcraftcod.simulator.Environment;
import fr.mrcraftcod.simulator.chargers.Charger;
import fr.mrcraftcod.simulator.positions.Position;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TourTravelBaseMetricEventTest{
	private Environment environment;
	private Charger charger;
	
	static class DataProvider implements ArgumentsProvider{
		@Override
		public Stream<? extends Arguments> provideArguments(final ExtensionContext context){
			return Stream.of(new Object[]{
					0D,
					new ImmutablePair<>(new Position(0, 0), new Position(1, 1))
			}, new Object[]{
					15D,
					new ImmutablePair<>(new Position(2, 2), new Position(3, 3))
			}).map(Arguments::of);
		}
	}
	
	@BeforeEach
	void setUp(){
		this.environment = new Environment(null, "junit-test");
		this.charger = new Charger(environment);
	}
	
	@ParameterizedTest
	@ArgumentsSource(DataProvider.class)
	void construct(final double time, final Pair<Position, Position> positions){
		final var event = new TourTravelBaseMetricEvent(environment, time, charger, positions);
		assertEquals(environment, event.getEnvironment());
		assertEquals(time, event.getTime());
		assertEquals(charger, event.getElement());
		assertEquals(positions, event.getNewValue());
	}
}