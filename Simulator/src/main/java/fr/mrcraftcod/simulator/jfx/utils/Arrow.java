package fr.mrcraftcod.simulator.jfx.utils;

import fr.mrcraftcod.simulator.positions.Position;
import javafx.beans.InvalidationListener;
import javafx.beans.property.DoubleProperty;
import javafx.scene.shape.Line;

/**
 * Draw an arrow.
 * <p>
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2019-01-23.
 * <p>
 * See <a href="https://stackoverflow.com/a/41353991/3281185">Stackoverflow</a>
 *
 * @author Thomas Couchoud
 * @since 2019-01-23
 */
@SuppressWarnings("WeakerAccess")
public class Arrow extends ColorableGroup{
	
	private static final double arrowLength = 10;
	private static final double arrowWidth = 10;
	private final Line line;
	
	/**
	 * Constructor.
	 */
	public Arrow(){
		this(new Line(), new Line(), new Line());
	}
	
	/**
	 * Constructor.
	 *
	 * @param line   The main line.
	 * @param arrow1 The first branch.
	 * @param arrow2 The second branch.
	 */
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
			
			arrow1.setStrokeWidth(line.getStrokeWidth());
			arrow2.setStrokeWidth(line.getStrokeWidth());
		};
		startXProperty().addListener(updater);
		startYProperty().addListener(updater);
		endXProperty().addListener(updater);
		endYProperty().addListener(updater);
		strokeWidthProperty().addListener(updater);
		updater.invalidated(null);
	}
	
	/**
	 * Get the property of the stroke width.
	 *
	 * @return The stroke width property.
	 */
	private DoubleProperty strokeWidthProperty(){
		return line.strokeWidthProperty();
	}
	
	/**
	 * Set the stroke width.
	 *
	 * @param width The stroke width.
	 */
	public void setStrokeWidth(final double width){
		line.setStrokeWidth(width);
	}
	
	/**
	 * Get the end X coordinate.
	 *
	 * @return The end X.
	 */
	public final double getEndX(){
		return line.getEndX();
	}
	
	/**
	 * Set the end X coordinate.
	 *
	 * @param value The end X.
	 */
	public final void setEndX(final double value){
		line.setEndX(value);
	}
	
	/**
	 * Get the end Y coordinate.
	 *
	 * @return The end Y.
	 */
	public final double getEndY(){
		return line.getEndY();
	}
	
	/**
	 * Get the start X coordinate.
	 *
	 * @return The start X.
	 */
	public final double getStartX(){
		return line.getStartX();
	}
	
	/**
	 * Set the start X coordinate.
	 *
	 * @param value The start X.
	 */
	public final void setStartX(final double value){
		line.setStartX(value);
	}
	
	/**
	 * Get the start Y coordinate.
	 *
	 * @return The start Y.
	 */
	public final double getStartY(){
		return line.getStartY();
	}
	
	/**
	 * Get the start X coordinate property.
	 *
	 * @return The start X property.
	 */
	public final DoubleProperty startXProperty(){
		return line.startXProperty();
	}
	
	/**
	 * Get the start Y coordinate property.
	 *
	 * @return The start Y property.
	 */
	public final DoubleProperty startYProperty(){
		return line.startYProperty();
	}
	
	/**
	 * Get the end X coordinate property.
	 *
	 * @return The end X property.
	 */
	public final DoubleProperty endXProperty(){
		return line.endXProperty();
	}
	
	/**
	 * Get the end Y coordinate property.
	 *
	 * @return The end Y property.
	 */
	public final DoubleProperty endYProperty(){
		return line.endYProperty();
	}
	
	/**
	 * Set the start Y coordinate.
	 *
	 * @param value The start Y.
	 */
	public final void setStartY(final double value){
		line.setStartY(value);
	}
	
	/**
	 * Set the end Y coordinate.
	 *
	 * @param value The end Y.
	 */
	public final void setEndY(final double value){
		line.setEndY(value);
	}
	
	/**
	 * Set the end position.
	 *
	 * @param endPosition The end position.
	 */
	public void setEndPosition(final Position endPosition){
		setEndX(endPosition.getX());
		setEndY(endPosition.getY());
	}
	
	/**
	 * Set the start position.
	 *
	 * @param startPosition The start position.
	 */
	public void setStartPosition(final Position startPosition){
		setStartX(startPosition.getX());
		setStartY(startPosition.getY());
	}
}
