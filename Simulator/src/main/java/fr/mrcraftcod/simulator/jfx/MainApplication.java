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
import java.awt.Taskbar;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Main entry point for the UI.
 *
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2019-01-17.
 *
 * @author Thomas Couchoud
 * @since 2019-01-17
 */
public class MainApplication extends Application{
	private static SimulationParameters simulationParameters;
	private Stage stage;
	private TabPane tabPane;
	private Slider delaySlider;
	
	/**
	 * The main method.
	 *
	 * @param args                 The args.
	 * @param simulationParameters The parameters of the simulation.
	 */
	public static void main(final String[] args, final SimulationParameters simulationParameters){
		MainApplication.simulationParameters = simulationParameters;
		launch(args);
	}
	
	@Override
	public void start(final Stage stage){
		this.stage = stage;
		final var scene = buildScene();
		stage.setTitle("Simulator Charts");
		stage.setScene(scene);
		stage.sizeToScene();
		setIcon();
		stage.show();
		onStageDisplayed(stage);
	}
	
	/**
	 * Builds the main scene.
	 *
	 * @return The scene.
	 */
	private Scene buildScene(){
		return new Scene(createContent(), 640, 640);
	}
	
	/**
	 * Set the icon of the frame.
	 */
	private void setIcon(){
		final var icon = new Image(Main.class.getResourceAsStream("/jfx/icon.png"));
		this.stage.getIcons().clear();
		this.stage.getIcons().add(icon);
		Taskbar.getTaskbar().setIconImage(SwingFXUtils.fromFXImage(icon, null));
	}
	
	/**
	 * Method executed when the frame is displayed.
	 *
	 * @param stage The stage displayed.
	 */
	private void onStageDisplayed(final Stage stage){
		stage.setOnCloseRequest(evt -> simulationParameters.getEnvironment().getSimulator().stop());
		
		this.tabPane.getTabs().addAll(buildTabs(simulationParameters));
		this.stage.setMaximized(true);
		
		simulationParameters.getEnvironment().getSimulator().delayProperty().bind(delaySlider.valueProperty());
		simulationParameters.getEnvironment().getSimulator().setRunning(false);
		final var executor = Executors.newSingleThreadScheduledExecutor();
		executor.schedule(() -> simulationParameters.getEnvironment().getSimulator().run(), 5, TimeUnit.MILLISECONDS);
		executor.shutdown();
	}
	
	/**
	 * Create the frame content.
	 *
	 * @return The content.
	 */
	private Parent createContent(){
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
	
	/**
	 * Build the different tabs of the frame.
	 *
	 * @param simulationParameters The parameters of the simulation.
	 *
	 * @return The tabs.
	 */
	private Collection<? extends MetricEventListenerTab> buildTabs(final SimulationParameters simulationParameters){
		final var tabs = List.of(new SensorsCapacityChartTab(simulationParameters.getEnvironment().getElements(Sensor.class)), new MapTab(this.getStage().getScene(), delaySlider.valueProperty(), simulationParameters.getEnvironment().getElements(Positionable.class)));
		tabs.forEach(t -> simulationParameters.getEnvironment().getSimulator().getMetricEventDispatcher().addListener(t));
		return tabs;
	}
	
	/**
	 * Get the stage.
	 *
	 * @return The stage.
	 */
	private Stage getStage(){
		return stage;
	}
}
