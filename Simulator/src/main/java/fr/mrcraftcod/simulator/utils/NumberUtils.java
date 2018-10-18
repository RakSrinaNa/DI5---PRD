package fr.mrcraftcod.simulator.utils;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-10-18.
 *
 * @author Thomas Couchoud
 * @since 2018-10-18
 */
public class NumberUtils{
	public static boolean isPositiveNonZero(final double value){
		return isPositive(value) && isNonZero(value);
	}
	
	public static boolean isPositive(final double value){
		return value > 0;
	}
	
	private static boolean isNonZero(final double value){
		return value != 0;
	}
}
