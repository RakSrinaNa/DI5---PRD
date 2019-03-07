package fr.mrcraftcod.simulator.jfx.tabs;

import fr.mrcraftcod.simulator.chargers.Charger;
import fr.mrcraftcod.simulator.jfx.utils.Arrow;
import fr.mrcraftcod.simulator.jfx.utils.ColorableGroup;
import fr.mrcraftcod.simulator.jfx.utils.MetricEventListenerTab;
import fr.mrcraftcod.simulator.metrics.MetricEvent;
import fr.mrcraftcod.simulator.positions.Position;
import fr.mrcraftcod.simulator.rault.metrics.events.*;
import fr.mrcraftcod.simulator.sensors.Sensor;
import fr.mrcraftcod.simulator.utils.Positionable;
import javafx.animation.PathTransition;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.scene.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

/**
 * Displays a map with different things happening.
 * <p>
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2019-01-17.
 *
 * @author Thomas Couchoud
 * @since 2019-01-17
 */
public class MapTab extends MetricEventListenerTab{
	private final static double ZOOM_FACTOR = 25;
	private final HashMap<Positionable, ColorableGroup> elements;
	private final HashMap<Positionable, PathTransition> transitions;
	private final Pane elementsPane;
	private final DoubleProperty delayProperty;
	private Double lastX = null;
	private Double lastY = null;
	
	/**
	 * Constructor.
	 *
	 * @param parentScene   The parent scene.
	 * @param delayProperty The property of the delay in the simulation. (Will be read to allow animations or not)
	 * @param elements      The elements to track.
	 */
	public MapTab(final Scene parentScene, final DoubleProperty delayProperty, final Collection<? extends Positionable> elements){
		this.elements = new HashMap<>();
		this.transitions = new HashMap<>();
		this.delayProperty = delayProperty;
		
		elementsPane = new Pane();
		final var subScene = new SubScene(elementsPane, 300, 300, true, SceneAntialiasing.BALANCED);
		subScene.widthProperty().bind(parentScene.widthProperty());
		subScene.heightProperty().bind(parentScene.heightProperty());
		final var sceneGroup = new Group();
		sceneGroup.getChildren().add(subScene);
		
		elements.forEach(elem -> this.elements.put(elem, buildElementRepresentation(elem)));
		elementsPane.getChildren().addAll(this.elements.values());
		
		final var axeX = new Rectangle(-1 * ZOOM_FACTOR, -0.1 * ZOOM_FACTOR, 2 * ZOOM_FACTOR, 0.2 * ZOOM_FACTOR);
		final var axeY = new Rectangle(-0.1 * ZOOM_FACTOR, -1 * ZOOM_FACTOR, 0.2 * ZOOM_FACTOR, 2 * ZOOM_FACTOR);
		elementsPane.getChildren().addAll(axeX, axeY);
		
		this.setContent(sceneGroup);
		this.setClosable(false);
		this.setText("Map");
		
		final var camera = new PerspectiveCamera();
		camera.setTranslateX(-parentScene.widthProperty().get() / 2);
		camera.setTranslateY(-parentScene.heightProperty().get() / 2);
		subScene.setCamera(camera);
		sceneGroup.addEventHandler(ScrollEvent.SCROLL, event -> camera.translateZProperty().set(Math.max(-40000, Math.min(1000, camera.getTranslateZ() + event.getDeltaY()))));
		sceneGroup.addEventHandler(MouseEvent.MOUSE_PRESSED, evt -> {
			lastX = evt.getSceneX();
			lastY = evt.getSceneY();
		});
		sceneGroup.addEventHandler(MouseEvent.MOUSE_DRAGGED, evt -> {
			if(Objects.nonNull(lastX) && Objects.nonNull(lastY)){
				camera.setTranslateX(camera.getTranslateX() - (evt.getSceneX() - lastX));
				camera.setTranslateY(camera.getTranslateY() - (evt.getSceneY() - lastY));
				lastX = evt.getSceneX();
				lastY = evt.getSceneY();
			}
		});
		sceneGroup.addEventHandler(MouseEvent.MOUSE_RELEASED, evt -> {
			lastX = null;
			lastY = null;
		});
	}
	
	/**
	 * Build the representation of an element.
	 *
	 * @param positionable The element to build.
	 *
	 * @return A group to display on the map.
	 */
	private ColorableGroup buildElementRepresentation(final Positionable positionable){
		final ColorableGroup element;
		if(positionable instanceof Sensor){
			final var g = new ColorableGroup();
			
			final var polygon = new Rectangle(-0.5 * ZOOM_FACTOR, -0.5 * ZOOM_FACTOR, ZOOM_FACTOR, ZOOM_FACTOR);
			polygon.setTranslateZ(-0.01);
			final var text = new Text("" + positionable.getID());
			text.setTextAlignment(TextAlignment.CENTER);
			text.setTranslateY(-0.5 * ZOOM_FACTOR - 5);
			text.setTranslateZ(-0.02);
			
			g.getChildren().addAll(polygon);
			g.setColor(Color.GREEN);
			element = g;
		}
		else if(positionable instanceof Charger){
			final var g = new ColorableGroup();
			
			final var dot = new Circle(0, 0, ZOOM_FACTOR);
			final var radius = new Circle(0, 0, ZOOM_FACTOR * ((Charger) positionable).getRadius());
			radius.setFill(Color.TRANSPARENT);
			final var text = new Text("" + positionable.getID());
			text.setTextAlignment(TextAlignment.CENTER);
			text.setTranslateY(ZOOM_FACTOR + 15);
			text.setTranslateZ(-0.01);
			
			g.getChildren().addAll(dot, radius, text);
			g.setColor(Color.CADETBLUE);
			element = g;
		}
		else{
			final var g = new ColorableGroup();
			g.getChildren().add(new Circle(0, 0, ZOOM_FACTOR));
			element = g;
		}
		element.setId(positionable.getUniqueIdentifier());
		element.setTranslateX(ZOOM_FACTOR * positionable.getPosition().getX());
		element.setTranslateY(ZOOM_FACTOR * positionable.getPosition().getY());
		return element;
	}
	
	@Override
	public void onEvent(final MetricEvent event){
		if(event instanceof LrRequestMetricEvent){
			final var elem = ((LrRequestMetricEvent) event).getElement();
			if(elements.containsKey(elem)){
				Platform.runLater(() -> elements.get(elem).setColor(Color.ORANGE));
			}
		}
		else if(event instanceof LcRequestMetricEvent){
			final var elem = ((LcRequestMetricEvent) event).getElement();
			if(elements.containsKey(elem)){
				Platform.runLater(() -> elements.get(elem).setColor(Color.RED));
			}
		}
		else if(event instanceof SensorChargedMetricEvent){
			final var elem = ((SensorChargedMetricEvent) event).getElement();
			if(elements.containsKey(elem)){
				Platform.runLater(() -> elements.get(elem).setColor(Color.GREEN));
			}
		}
		else if(event instanceof TourStartMetricEvent){
			if(delayProperty.get() > 50){
				var lastPos = ((TourStartMetricEvent) event).getElement().getPosition();
				for(final var nextPos : ((TourStartMetricEvent) event).getNewValue().getStops()){
					final var id = String.format("tour-arrow-%d-%d", ((TourStartMetricEvent) event).getElement().getID(), nextPos.getID());
					final var finalLastPos = lastPos;
					Platform.runLater(() -> elementsPane.getChildren().add(buildArrow(id, finalLastPos, nextPos.getStopLocation().getPosition())));
					lastPos = nextPos.getStopLocation().getPosition();
				}
				if(!Objects.equals(lastPos, ((TourStartMetricEvent) event).getElement().getPosition())){
					final var id = String.format("tour-arrow-%d--1", ((TourStartMetricEvent) event).getElement().getID());
					final var finalLastPos = lastPos;
					Platform.runLater(() -> elementsPane.getChildren().add(buildArrow(id, finalLastPos, ((TourStartMetricEvent) event).getElement().getPosition())));
				}
			}
		}
		else if(event instanceof TourTravelMetricEvent){
			final var element = ((TourTravelMetricEvent) event).getElement();
			Optional.ofNullable(elements.get(element)).ifPresent(e -> Platform.runLater(() -> e.setColor(Color.SLATEBLUE)));
			final var id = String.format("tour-arrow-%d-%d", element.getID(), ((TourTravelMetricEvent) event).getNewValue().getRight().getID());
			Platform.runLater(() -> elementsPane.getChildren().removeIf(n -> n instanceof Arrow && Objects.equals(n.getId(), id)));
			if(delayProperty.get() > 50){
				Optional.ofNullable(elements.get(element)).ifPresent(e -> {
					if(transitions.containsKey(element)){
						Platform.runLater(() -> transitions.get(element).stop());
					}
					final var values = ((TourTravelMetricEvent) event).getNewValue();
					final var toPosition = values.getRight().getStopLocation().getPosition();
					final var fromPosition = values.getLeft();
					transitions.put(element, addAnimation(e, toPosition, fromPosition));
				});
			}
		}
		else if(event instanceof TourTravelBaseMetricEvent){
			final var id = String.format("tour-arrow-%d--1", ((TourTravelBaseMetricEvent) event).getElement().getID());
			Platform.runLater(() -> elementsPane.getChildren().removeIf(n -> n instanceof Arrow && Objects.equals(n.getId(), id)));
			final var element = ((TourTravelBaseMetricEvent) event).getElement();
			Optional.ofNullable(elements.get(element)).ifPresent(e -> Platform.runLater(() -> e.setColor(Color.SLATEBLUE)));
			if(delayProperty.get() > 50){
				Optional.ofNullable(elements.get(element)).ifPresent(e -> {
					if(transitions.containsKey(element)){
						Platform.runLater(() -> transitions.get(element).stop());
					}
					final var values = ((TourTravelBaseMetricEvent) event).getNewValue();
					final var toPosition = values.getRight();
					final var fromPosition = values.getLeft();
					transitions.put(element, addAnimation(e, toPosition, fromPosition));
				});
			}
		}
		else if(event instanceof TourTravelEndMetricEvent){
			final var element = ((TourTravelEndMetricEvent) event).getElement();
			if(transitions.containsKey(element)){
				Platform.runLater(() -> transitions.get(element).stop());
			}
			updatePosition(element);
			Optional.ofNullable(elements.get(element)).ifPresent(e -> Platform.runLater(() -> e.setColor(Color.CADETBLUE)));
		}
		else if(event instanceof TourEndMetricEvent){
			updatePosition(((TourEndMetricEvent) event).getElement());
			Optional.ofNullable(elements.get(((TourEndMetricEvent) event).getElement())).ifPresent(e -> Platform.runLater(() -> e.setColor(Color.CADETBLUE)));
		}
		else if(event instanceof TourChargeMetricEvent){
			final var evt = (TourChargeMetricEvent) event;
			Optional.ofNullable(elements.get(((TourChargeMetricEvent) event).getElement())).ifPresent(e -> Platform.runLater(() -> e.setColor(Color.HOTPINK)));
			if(delayProperty.get() > 50){
				evt.getNewValue().getStopLocation().getSensors().forEach(s -> {
					final var id = String.format("charging-arrow-%d-%d", ((TourChargeMetricEvent) event).getElement().getID(), s.getID());
					final var arrow = buildArrow(id, evt.getNewValue().getStopLocation().getPosition(), s.getPosition());
					arrow.setStrokeWidth(3);
					arrow.setTranslateZ(-0.04);
					arrow.setColor(Color.HOTPINK);
					Platform.runLater(() -> elementsPane.getChildren().add(arrow));
				});
			}
		}
		else if(event instanceof TourChargeEndMetricEvent){
			final var evt = (TourChargeEndMetricEvent) event;
			Optional.ofNullable(elements.get(((TourChargeEndMetricEvent) event).getElement())).ifPresent(e -> Platform.runLater(() -> e.setColor(Color.CADETBLUE)));
			evt.getNewValue().getStopLocation().getSensors().forEach(s -> {
				final var id = String.format("charging-arrow-%d-%d", ((TourChargeEndMetricEvent) event).getElement().getID(), s.getID());
				Platform.runLater(() -> elementsPane.getChildren().removeIf(n -> n instanceof Arrow && Objects.equals(n.getId(), id)));
			});
		}
		// Platform.runLater(() -> elements.forEach((key, value) -> {
		// 	value.setTranslateX(ZOOM_FACTOR * key.getPosition().getX());
		// 	value.setTranslateY(ZOOM_FACTOR * key.getPosition().getY());
		// }));
	}
	
	/**
	 * Create an arrow.
	 *
	 * @param id            The id of the arrow.
	 * @param startPosition The start position of the arrow.
	 * @param endPosition   The end position of the arrow.
	 *
	 * @return The arrow.
	 */
	private Arrow buildArrow(final String id, final Position startPosition, final Position endPosition){
		final var arrow = new Arrow();
		arrow.setStartX(ZOOM_FACTOR * startPosition.getX());
		arrow.setStartY(ZOOM_FACTOR * startPosition.getY());
		arrow.setEndX(ZOOM_FACTOR * endPosition.getX());
		arrow.setEndY(ZOOM_FACTOR * endPosition.getY());
		arrow.setTranslateZ(-0.03);
		arrow.setId(id);
		return arrow;
	}
	
	/**
	 * Create a linear movement animation for a node.
	 *
	 * @param node         The node to animate.
	 * @param toPosition   The start position.
	 * @param fromPosition The end position.
	 *
	 * @return The animation.
	 */
	private PathTransition addAnimation(final Node node, final Position toPosition, final Position fromPosition){
		final var path = new Line(ZOOM_FACTOR * fromPosition.getX(), ZOOM_FACTOR * fromPosition.getY(), ZOOM_FACTOR * toPosition.getX(), ZOOM_FACTOR * toPosition.getY());
		path.setTranslateZ(node.getTranslateZ());
		final var pathTransition = new PathTransition();
		pathTransition.setDuration(Duration.millis(500));
		pathTransition.setPath(path);
		pathTransition.setNode(node);
		pathTransition.setCycleCount(1);
		pathTransition.setAutoReverse(false);
		Platform.runLater(pathTransition::play);
		return pathTransition;
	}
	
	/**
	 * Update the position of an element.
	 *
	 * @param positionable The element to update.
	 */
	private void updatePosition(final Positionable positionable){
		Optional.ofNullable(elements.get(positionable)).ifPresent(e -> Platform.runLater(() -> {
			final var pos = positionable.getPosition();
			e.setTranslateX(ZOOM_FACTOR * pos.getX());
			e.setTranslateY(ZOOM_FACTOR * pos.getY());
		}));
	}
	
	@Override
	public void close(){
	
	}
}
