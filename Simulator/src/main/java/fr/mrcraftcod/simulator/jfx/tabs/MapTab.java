package fr.mrcraftcod.simulator.jfx.tabs;

import fr.mrcraftcod.simulator.chargers.Charger;
import fr.mrcraftcod.simulator.jfx.utils.Arrow;
import fr.mrcraftcod.simulator.jfx.utils.ColorableGroup;
import fr.mrcraftcod.simulator.metrics.MetricEvent;
import fr.mrcraftcod.simulator.metrics.MetricEventDispatcher;
import fr.mrcraftcod.simulator.metrics.MetricEventListener;
import fr.mrcraftcod.simulator.positions.Position;
import fr.mrcraftcod.simulator.rault.metrics.events.*;
import fr.mrcraftcod.simulator.rault.routing.ChargingStop;
import fr.mrcraftcod.simulator.sensors.Sensor;
import fr.mrcraftcod.simulator.utils.Positionable;
import javafx.application.Platform;
import javafx.scene.*;
import javafx.scene.control.Tab;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2019-01-17.
 *
 * @author Thomas Couchoud
 * @since 2019-01-17
 */
public class MapTab extends Tab implements MetricEventListener{
	private static final Logger LOGGER = LoggerFactory.getLogger(MapTab.class);
	private final HashMap<Positionable, ColorableGroup> elements;
	private final Pane elementsPane;
	private final static double ZOOM_FACTOR = 25;
	private Double lastX = null;
	private Double lastY = null;
	
	public MapTab(final Scene parentScene, final Collection<? extends Positionable> elements){
		this.elements = new HashMap<>();
		
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
		MetricEventDispatcher.addListener(this);
		
		final var camera = new PerspectiveCamera();
		camera.setTranslateX(-parentScene.widthProperty().get() / 2);
		camera.setTranslateY(-parentScene.heightProperty().get() / 2);
		subScene.setCamera(camera);
		sceneGroup.addEventHandler(ScrollEvent.SCROLL, event -> {
			camera.translateZProperty().set(Math.max(-40000, Math.min(1000, camera.getTranslateZ() + event.getDeltaY())));
		});
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
				elements.get(elem).setColor(Color.ORANGE);
			}
		}
		else if(event instanceof LcRequestMetricEvent){
			final var elem = ((LcRequestMetricEvent) event).getElement();
			if(elements.containsKey(elem)){
				elements.get(elem).setColor(Color.RED);
			}
		}
		else if(event instanceof SensorChargedMetricEvent){
			final var elem = ((SensorChargedMetricEvent) event).getElement();
			if(elements.containsKey(elem)){
				elements.get(elem).setColor(Color.GREEN);
			}
		}
		else if(event instanceof TourStartMetricEvent){
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
		else if(event instanceof TourTravelMetricEvent){
			Optional.ofNullable(elements.get(((TourTravelMetricEvent) event).getElement())).ifPresent(e -> e.setColor(Color.SLATEBLUE));
			final var id = String.format("tour-arrow-%d-%d", ((TourTravelMetricEvent) event).getElement().getID(), ((TourTravelMetricEvent) event).getNewValue().getID());
			Platform.runLater(() -> elementsPane.getChildren().removeIf(n -> n instanceof Arrow && Objects.equals(n.getId(), id)));
		}
		else if(event instanceof TourTravelEndMetricEvent){
			Optional.ofNullable(elements.get(((TourTravelEndMetricEvent) event).getElement())).ifPresent(e -> e.setColor(Color.CADETBLUE));
		}
		else if(event instanceof TourEndMetricEvent){
			final var id = String.format("tour-arrow-%d--1", ((TourEndMetricEvent) event).getElement().getID());
			Platform.runLater(() -> elementsPane.getChildren().removeIf(n -> n instanceof Arrow && Objects.equals(n.getId(), id)));
		}
		else if(event instanceof TourChargeMetricEvent){
			Optional.ofNullable(elements.get(((TourChargeMetricEvent) event).getElement())).ifPresent(e -> e.setColor(Color.HOTPINK));
			((ChargingStop) event.getNewValue()).getStopLocation().getSensors().forEach(s -> {
				final var id = String.format("charging-arrow-%d-%d", ((TourChargeMetricEvent) event).getElement().getID(), s.getID());
				final var arrow = buildArrow(id, ((ChargingStop) event.getNewValue()).getStopLocation().getPosition(), s.getPosition());
				arrow.setColor(Color.HOTPINK);
				Platform.runLater(() -> elementsPane.getChildren().add(arrow));
			});
		}
		else if(event instanceof TourChargeEndMetricEvent){
			Optional.ofNullable(elements.get(((TourChargeEndMetricEvent) event).getElement())).ifPresent(e -> e.setColor(Color.CADETBLUE));
			((ChargingStop) event.getNewValue()).getStopLocation().getSensors().forEach(s -> {
				final var id = String.format("charging-arrow-%d-%d", ((TourChargeEndMetricEvent) event).getElement().getID(), s.getID());
				Platform.runLater(() -> elementsPane.getChildren().removeIf(n -> n instanceof Arrow && Objects.equals(n.getId(), id)));
			});
		}
		Platform.runLater(() -> elements.forEach((key, value) -> {
			value.setTranslateX(ZOOM_FACTOR * key.getPosition().getX());
			value.setTranslateY(ZOOM_FACTOR * key.getPosition().getY());
		}));
	}
	
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
	
	@Override
	public void onEnd(){
	
	}
}
