package fr.mrcraftcod.simulator.jfx;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Shape;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2019-01-17.
 *
 * @author Thomas Couchoud
 * @since 2019-01-17
 */
public class ColorableGroup extends Group{
	
	public void setColor(final Color color){
		getChildren().forEach(child -> {
			if(child instanceof Arc){
				((Arc) child).setStroke(color);
			}
			else if(child instanceof Shape){
				((Shape) child).setFill(color);
			}
		});
	}
}
