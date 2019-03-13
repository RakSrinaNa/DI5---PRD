package fr.mrcraftcod.simulator.utils;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by mrcraftcod (MrCraftCod - zerderr@gmail.com) on 2019-03-07.
 *
 * @author Thomas Couchoud
 * @since 2019-03-07
 */
class RechargeableTest{
	@ParameterizedTest
	@ValueSource(ints = {
			10,
			50,
			500
	})
	void getters(final int max){
		final Rechargeable rechargeable = new Rechargeable(){
			private double capacity = 0;
			
			@Override
			public double getMaxCapacity(){
				return max;
			}
			
			@Override
			public double getCurrentCapacity(){
				return this.capacity;
			}
			
			@Override
			public void setCurrentCapacity(final double currentCapacity){
				this.capacity = currentCapacity;
			}
		};
		assertEquals(max, rechargeable.getMaxCapacity(), 0);
		assertEquals(0, rechargeable.getCurrentCapacity(), 0);
		rechargeable.setCurrentCapacity(max / 2);
		assertEquals(max / 2, rechargeable.getCurrentCapacity(), 0);
		rechargeable.setCurrentCapacity(2 * max);
		assertEquals(2 * max, rechargeable.getCurrentCapacity(), 0);
	}
	
	@ParameterizedTest
	@ValueSource(ints = {
			10,
			50,
			500
	})
	void removeCapacity(final int max){
		final Rechargeable rechargeable = new Rechargeable(){
			private double capacity = max;
			
			@Override
			public double getMaxCapacity(){
				return max;
			}
			
			@Override
			public double getCurrentCapacity(){
				return this.capacity;
			}
			
			@Override
			public void setCurrentCapacity(final double currentCapacity){
				this.capacity = currentCapacity;
			}
		};
		assertEquals(max, rechargeable.getMaxCapacity(), 0);
		assertEquals(max, rechargeable.getCurrentCapacity(), 0);
		rechargeable.removeCapacity(max / 2);
		assertEquals(max / 2, rechargeable.getCurrentCapacity(), 0);
		rechargeable.removeCapacity(2 * max);
		assertEquals(0, rechargeable.getCurrentCapacity(), 0);
	}
	
	@ParameterizedTest
	@ValueSource(ints = {
			10,
			50,
			500
	})
	void addCapacity(final int max){
		final Rechargeable rechargeable = new Rechargeable(){
			private double capacity = 0;
			
			@Override
			public double getMaxCapacity(){
				return max;
			}
			
			@Override
			public double getCurrentCapacity(){
				return this.capacity;
			}
			
			@Override
			public void setCurrentCapacity(final double currentCapacity){
				this.capacity = currentCapacity;
			}
		};
		assertEquals(0, rechargeable.getCurrentCapacity(), 0);
		rechargeable.addCapacity(max / 2);
		assertEquals(max / 2, rechargeable.getCurrentCapacity(), 0);
		rechargeable.addCapacity(2 * max);
		assertEquals(max, rechargeable.getCurrentCapacity(), 0);
	}
}