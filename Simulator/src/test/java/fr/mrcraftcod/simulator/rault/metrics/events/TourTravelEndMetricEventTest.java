package fr.mrcraftcod.simulator.rault.metrics.events;

import fr.mrcraftcod.simulator.Environment;
import fr.mrcraftcod.simulator.chargers.Charger;
import fr.mrcraftcod.simulator.positions.Position;
import fr.mrcraftcod.simulator.rault.routing.ChargingStop;
import fr.mrcraftcod.simulator.rault.routing.StopLocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TourTravelEndMetricEventTest{
	private Environment environment;
	private Charger charger;
	
	static class DataProvider implements ArgumentsProvider{
		@Override
		public Stream<? extends Arguments> provideArguments(final ExtensionContext context) throws Exception{
			return Stream.of(new Object[]{
					0D,
					new ChargingStop(new StopLocation(new Position(1, 1)), 10)
			}, new Object[]{
					15D,
					new ChargingStop(new StopLocation(new Position(3, 3)), 20)
			}).map(Arguments::of);
		}
	}
	
	@BeforeEach
	void setUp(){
		this.environment = new Environment(null);
		this.charger = new Charger(environment);
	}
	
	@ParameterizedTest
	@ArgumentsSource(DataProvider.class)
	void construct(final double time, final ChargingStop stop){
		final var event = new TourTravelEndMetricEvent(environment, time, charger, stop);
		assertEquals(environment, event.getEnvironment());
		assertEquals(time, event.getTime());
		assertEquals(charger, event.getElement());
		assertEquals(stop, event.getNewValue());
	}
}