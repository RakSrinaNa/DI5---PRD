/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2019-01-09.
 *
 * @author Thomas Couchoud
 * @since 2019-01-09
 */
@SuppressWarnings("Java9RedundantRequiresStatement")
open module fr.mrcraftcod.simulator {
	requires org.jetbrains.annotations;
	
	requires org.slf4j;
	requires org.apache.logging.log4j;
	requires java.scripting;
	
	requires org.apache.commons.lang3;
	requires org.json;
	requires ortools;
	requires jcommander;
	
	requires javafx.graphics;
	requires javafx.controls;
	requires javafx.swing;
	
	exports fr.mrcraftcod.simulator;
	exports fr.mrcraftcod.simulator.capacity;
	exports fr.mrcraftcod.simulator.chargers;
	exports fr.mrcraftcod.simulator.exceptions;
	exports fr.mrcraftcod.simulator.jfx;
	exports fr.mrcraftcod.simulator.jfx.tabs;
	exports fr.mrcraftcod.simulator.jfx.tabs.sensor;
	exports fr.mrcraftcod.simulator.jfx.utils;
	exports fr.mrcraftcod.simulator.metrics;
	exports fr.mrcraftcod.simulator.metrics.events;
	exports fr.mrcraftcod.simulator.metrics.listeners;
	exports fr.mrcraftcod.simulator.positions;
	exports fr.mrcraftcod.simulator.routing;
	exports fr.mrcraftcod.simulator.sensors;
	exports fr.mrcraftcod.simulator.simulation;
	exports fr.mrcraftcod.simulator.simulation.events;
	exports fr.mrcraftcod.simulator.utils;
}