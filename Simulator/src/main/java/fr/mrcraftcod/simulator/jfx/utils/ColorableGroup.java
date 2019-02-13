package fr.mrcraftcod.simulator.jfx.utils;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import java.util.Collection;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2019-01-17.
 *
 * @author Thomas Couchoud
 * @since 2019-01-17
 */
@SuppressWarnings({
		"WeakerAccess"
})
public class ColorableGroup extends Group{
	public ColorableGroup(){
	}
	
	public ColorableGroup(final Node... children){
		super(children);
	}
	
	@SuppressWarnings("unused")
	public ColorableGroup(final Collection<Node> children){
		super(children);
	}
	
	public void setColor(final Color color){
		getChildren().forEach(child -> {
			if(child instanceof Shape && !(child instanceof Text)){
				if(((Shape) child).getFill() != Color.TRANSPARENT){
					((Shape) child).setFill(color);
				}
				((Shape) child).setStroke(color);
			}
		});
	}
}
