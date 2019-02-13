/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2019-01-09.
 *
 * @author Thomas Couchoud
 * @since 2019-01-09
 */
@SuppressWarnings("Java9RedundantRequiresStatement")
open module fr.mrcraftcod.simulator {
	requires annotations;
	
	requires org.slf4j;
	requires org.apache.logging.log4j;
	requires org.apache.commons.lang3;
	requires org.json;
	requires ortools;
	requires jcommander;
	
	requires java.scripting;
	requires javafx.graphics;
	requires javafx.controls;
	requires javafx.swing;
	
	exports fr.mrcraftcod.simulator;
	exports fr.mrcraftcod.simulator.jfx to javafx.graphics;
}