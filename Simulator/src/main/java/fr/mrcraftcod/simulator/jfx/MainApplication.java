package fr.mrcraftcod.simulator.jfx;

import fr.mrcraftcod.simulator.Main;
import fr.mrcraftcod.simulator.SimulationParameters;
import fr.mrcraftcod.simulator.jfx.tabs.MapTab;
import fr.mrcraftcod.simulator.jfx.tabs.sensor.SensorsCapacityChartTab;
import fr.mrcraftcod.simulator.jfx.utils.MetricEventListenerTab;
import fr.mrcraftcod.simulator.sensors.Sensor;
import fr.mrcraftcod.simulator.utils.Positionable;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.awt.Taskbar;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2019-01-17.
 *
 * @author Thomas Couchoud
 * @since 2019-01-17
 */
public class MainApplication extends Application{
	private static final Logger LOGGER = LoggerFactory.getLogger(MainApplication.class);
	private static SimulationParameters simulationParameters;
	private Stage stage;
	private TabPane tabPane;
	private Slider delaySlider;
	
	@Override
	public void start(final Stage stage){
		this.stage = stage;
		final var scene = buildScene(stage);
		stage.setTitle(this.getFrameTitle());
		stage.setScene(scene);
		stage.sizeToScene();
		//noinspection ConstantConditions
		if(getIcon() != null){
			setIcon(getIcon());
		}
		stage.show();
		Objects.requireNonNull(this.getOnStageDisplayed()).accept(stage);
	}
	
	public static void main(final String[] args, final SimulationParameters simulationParameters){
		MainApplication.simulationParameters = simulationParameters;
		launch(args);
	}
	
	private Parent createContent(final Stage stage){
		final var root = new VBox();
		tabPane = new TabPane();
		
		final var play = new Button("Play");
		play.setMaxWidth(Double.MAX_VALUE);
		final var pause = new Button("Pause");
		pause.setMaxWidth(Double.MAX_VALUE);
		pause.setDisable(true);
		
		play.setOnAction(evt -> {
			simulationParameters.getEnvironment().getSimulator().setRunning(true);
			play.setDisable(true);
			pause.setDisable(false);
		});
		pause.setOnAction(evt -> {
			simulationParameters.getEnvironment().getSimulator().setRunning(false);
			play.setDisable(false);
			pause.setDisable(true);
		});
		
		final var timeText = new Text();
		timeText.textProperty().bind(simulationParameters.getEnvironment().getSimulator().currentTimeProperty().asString("Current simulation time: %f"));
		delaySlider = new Slider();
		delaySlider.setMin(0);
		delaySlider.setMax(1000);
		delaySlider.setValue(250);
		
		final var middleInfos = new VBox();
		middleInfos.getChildren().addAll(timeText, delaySlider);
		
		final var controls = new HBox(3);
		controls.getChildren().addAll(play, middleInfos, pause);
		root.getChildren().addAll(tabPane, controls);
		
		VBox.setVgrow(tabPane, Priority.ALWAYS);
		HBox.setHgrow(tabPane, Priority.ALWAYS);
		HBox.setHgrow(play, Priority.ALWAYS);
		HBox.setHgrow(pause, Priority.ALWAYS);
		return root;
	}
	
	private void setIcon(final Image icon){
		this.stage.getIcons().clear();
		this.stage.getIcons().add(icon);
		Taskbar.getTaskbar().setIconImage(SwingFXUtils.fromFXImage(icon, null));
	}
	
	private Image getIcon(){
		return new Image(Main.class.getResourceAsStream("/jfx/icon.png"));
	}
	
	private Scene buildScene(final Stage stage){
		return new Scene(createContent(stage), 640, 640);
	}
	
	@SuppressWarnings("SameReturnValue")
	private String getFrameTitle(){
		return "Simulator Charts";
	}
	
	@SuppressWarnings("Duplicates")
	private Consumer<Stage> getOnStageDisplayed(){
		return stage -> {
			stage.setOnCloseRequest(evt -> simulationParameters.getEnvironment().getSimulator().stop());
			
			this.tabPane.getTabs().addAll(buildTabs(simulationParameters));
			this.stage.setMaximized(true);
			
			simulationParameters.getEnvironment().getSimulator().delayProperty().bind(delaySlider.valueProperty());
			simulationParameters.getEnvironment().getSimulator().setRunning(false);
			final var executor = Executors.newSingleThreadScheduledExecutor();
			executor.schedule(() -> simulationParameters.getEnvironment().getSimulator().run(), 5, TimeUnit.MILLISECONDS);
			executor.shutdown();
		};
	}
	
	private Stage getStage(){
		return stage;
	}
	
	private Collection<? extends MetricEventListenerTab> buildTabs(final SimulationParameters simulationParameters){
		final var tabs = List.of(new SensorsCapacityChartTab(simulationParameters.getEnvironment().getElements(Sensor.class)), new MapTab(this.getStage().getScene(), delaySlider.valueProperty(), simulationParameters.getEnvironment().getElements(Positionable.class)));
		tabs.forEach(t -> simulationParameters.getEnvironment().getSimulator().getMetricEventDispatcher().addListener(t));
		return tabs;
	}
}
