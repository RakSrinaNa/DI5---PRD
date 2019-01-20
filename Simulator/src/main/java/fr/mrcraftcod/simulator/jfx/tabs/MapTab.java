package fr.mrcraftcod.simulator.jfx.tabs;

import fr.mrcraftcod.simulator.chargers.Charger;
import fr.mrcraftcod.simulator.jfx.ColorableGroup;
import fr.mrcraftcod.simulator.metrics.MetricEvent;
import fr.mrcraftcod.simulator.metrics.MetricEventDispatcher;
import fr.mrcraftcod.simulator.metrics.MetricEventListener;
import fr.mrcraftcod.simulator.rault.metrics.events.LcRequestMetricEvent;
import fr.mrcraftcod.simulator.rault.metrics.events.LrRequestMetricEvent;
import fr.mrcraftcod.simulator.rault.metrics.events.SensorChargedMetricEvent;
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
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2019-01-17.
 *
 * @author Thomas Couchoud
 * @since 2019-01-17
 */
public class MapTab extends Tab implements MetricEventListener{
	private final HashMap<Positionable, ColorableGroup> elements;
	private final static double ZOOM_FACTOR = 25;
	private Double lastX = null;
	private Double lastY = null;
	
	public MapTab(final Scene parentScene, final Collection<? extends Positionable> elements){
		this.elements = new HashMap<>();
		
		final var group = new Pane();
		final var subScene = new SubScene(group, 300, 300, true, SceneAntialiasing.BALANCED);
		subScene.widthProperty().bind(parentScene.widthProperty());
		subScene.heightProperty().bind(parentScene.heightProperty());
		final var sceneGroup = new Group();
		sceneGroup.getChildren().add(subScene);
		
		elements.forEach(elem -> this.elements.put(elem, buildElementRepresentation(elem)));
		group.getChildren().addAll(this.elements.values());
		
		this.setContent(sceneGroup);
		this.setClosable(false);
		this.setText("Map");
		MetricEventDispatcher.addListener(this);
		
		final var camera = new PerspectiveCamera();
		subScene.addEventHandler(ScrollEvent.SCROLL, event -> {
			camera.translateZProperty().set(camera.getTranslateZ() + event.getDeltaY());
		});
		subScene.addEventHandler(MouseEvent.MOUSE_PRESSED, evt -> {
			lastX = evt.getSceneX();
			lastY = evt.getSceneY();
		});
		subScene.addEventHandler(MouseEvent.MOUSE_DRAGGED, evt -> {
			if(Objects.nonNull(lastX) && Objects.nonNull(lastY)){
				camera.setTranslateX(camera.getTranslateX() - (evt.getSceneX() - lastX));
				camera.setTranslateY(camera.getTranslateY() - (evt.getSceneY() - lastY));
				lastX = evt.getSceneX();
				lastY = evt.getSceneY();
			}
		});
		subScene.addEventHandler(MouseEvent.MOUSE_RELEASED, evt -> {
			lastX = null;
			lastY = null;
		});
		subScene.setCamera(camera);
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
			text.setTranslateZ(-0.02);
			
			g.getChildren().addAll(dot, radius, text);
			g.setColor(Color.CADETBLUE);
			element = g;
		}
		else{
			final var g = new ColorableGroup();
			g.getChildren().add(new Circle(0, 0, ZOOM_FACTOR));
			element = g;
		}
		element.setTranslateX(ZOOM_FACTOR * positionable.getPosition().getX());
		element.setTranslateY(ZOOM_FACTOR * positionable.getPosition().getY());
		return element;
	}
	
	@Override
	public void onEvent(final MetricEvent event){
		if(event instanceof LrRequestMetricEvent){
			final var elem = ((LrRequestMetricEvent) event).getElement();
			if(elem instanceof Positionable && elements.containsKey(elem)){
				elements.get(elem).setColor(Color.ORANGE);
			}
		}
		else if(event instanceof LcRequestMetricEvent){
			final var elem = ((LcRequestMetricEvent) event).getElement();
			if(elem instanceof Positionable && elements.containsKey(elem)){
				elements.get(elem).setColor(Color.RED);
			}
		}
		else if(event instanceof SensorChargedMetricEvent){
			final var elem = ((SensorChargedMetricEvent) event).getElement();
			if(elem instanceof Positionable && elements.containsKey(elem)){
				elements.get(elem).setColor(Color.GREEN);
			}
		}
		Platform.runLater(() -> elements.forEach((key, value) -> {
			value.setTranslateX(ZOOM_FACTOR * key.getPosition().getX());
			value.setTranslateY(ZOOM_FACTOR * key.getPosition().getY());
		}));
	}
	
	@Override
	public void onEnd(){
	
	}
}
