package fr.mrcraftcod.simulator.jfx.utils;

import fr.mrcraftcod.simulator.positions.Position;
import javafx.beans.InvalidationListener;
import javafx.beans.property.DoubleProperty;
import javafx.scene.shape.Line;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2019-01-23.
 * <p>
 * See <a href="https://stackoverflow.com/a/41353991/3281185">Stackoverflow</a>
 *
 * @author Thomas Couchoud
 * @since 2019-01-23
 */
public class Arrow extends ColorableGroup{
	
	private static final double arrowLength = 10;
	private static final double arrowWidth = 10;
	private final Line line;
	
	public Arrow(){
		this(new Line(), new Line(), new Line());
	}
	
	private Arrow(final Line line, final Line arrow1, final Line arrow2){
		super(line, arrow1, arrow2);
		this.line = line;
		final InvalidationListener updater = o -> {
			final var ex = getEndX();
			final var ey = getEndY();
			final var sx = getStartX();
			final var sy = getStartY();
			
			arrow1.setEndX((ex + sx) / 2);
			arrow1.setEndY((ey + sy) / 2);
			arrow2.setEndX((ex + sx) / 2);
			arrow2.setEndY((ey + sy) / 2);
			
			if(ex == sx && ey == sy){
				arrow1.setStartX(ex);
				arrow1.setStartY(ey);
				arrow2.setStartX(ex);
				arrow2.setStartY(ey);
			}
			else{
				final var factor = arrowLength / Math.hypot(sx - ex, sy - ey);
				final var factorO = arrowWidth / Math.hypot(sx - ex, sy - ey);
				final var dx = (sx - ex) * factor;
				final var dy = (sy - ey) * factor;
				final var ox = (sx - ex) * factorO;
				final var oy = (sy - ey) * factorO;
				arrow1.setStartX((ex + sx) / 2 + dx - oy);
				arrow1.setStartY((ey + sy) / 2 + dy + ox);
				arrow2.setStartX((ex + sx) / 2 + dx + oy);
				arrow2.setStartY((ey + sy) / 2 + dy - ox);
			}
		};
		startXProperty().addListener(updater);
		startYProperty().addListener(updater);
		endXProperty().addListener(updater);
		endYProperty().addListener(updater);
		updater.invalidated(null);
	}
	
	public final double getEndX(){
		return line.getEndX();
	}
	
	public final void setEndX(final double value){
		line.setEndX(value);
	}
	
	public final double getEndY(){
		return line.getEndY();
	}
	
	public final double getStartX(){
		return line.getStartX();
	}
	
	public final void setStartX(final double value){
		line.setStartX(value);
	}
	
	public final double getStartY(){
		return line.getStartY();
	}
	
	public final DoubleProperty startXProperty(){
		return line.startXProperty();
	}
	
	public final DoubleProperty startYProperty(){
		return line.startYProperty();
	}
	
	public final DoubleProperty endXProperty(){
		return line.endXProperty();
	}
	
	public final DoubleProperty endYProperty(){
		return line.endYProperty();
	}
	
	public final void setStartY(final double value){
		line.setStartY(value);
	}
	
	public final void setEndY(final double value){
		line.setEndY(value);
	}
	
	public void setEndPosition(final Position endPosition){
		setEndX(endPosition.getX());
		setEndY(endPosition.getY());
	}
	
	public void setStartPosition(final Position startPosition){
		setStartX(startPosition.getX());
		setStartY(startPosition.getY());
	}
}
