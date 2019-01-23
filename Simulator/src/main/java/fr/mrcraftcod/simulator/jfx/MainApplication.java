package fr.mrcraftcod.simulator.jfx;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import fr.mrcraftcod.simulator.CLIParameters;
import fr.mrcraftcod.simulator.Main;
import fr.mrcraftcod.simulator.SimulationParameters;
import fr.mrcraftcod.simulator.jfx.tabs.MapTab;
import fr.mrcraftcod.simulator.jfx.tabs.sensor.SensorsCapacityChartTab;
import fr.mrcraftcod.simulator.jfx.utils.JFXUtils;
import fr.mrcraftcod.simulator.sensors.Sensor;
import fr.mrcraftcod.simulator.simulation.Simulator;
import fr.mrcraftcod.simulator.utils.Positionable;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.awt.Taskbar;
import java.nio.file.Paths;
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
	private Stage stage;
	private TabPane tabPane;
	private Simulator simulator;
	
	@Override
	public void start(final Stage stage) throws Exception{
		this.stage = stage;
		final var scene = buildScene(stage);
		stage.setTitle(this.getFrameTitle());
		stage.setScene(scene);
		stage.sizeToScene();
		if(getIcon() != null){
			setIcon(getIcon());
		}
		stage.show();
		Objects.requireNonNull(this.getOnStageDisplayed()).accept(stage);
	}
	
	public Parent createContent(final Stage stage){
		final var root = new VBox();
		tabPane = new TabPane();
		
		final var play = new Button("Play");
		play.setMaxWidth(Double.MAX_VALUE);
		final var pause = new Button("Pause");
		pause.setMaxWidth(Double.MAX_VALUE);
		pause.setDisable(true);
		
		play.setOnAction(evt -> {
			simulator.setRunning(true);
			play.setDisable(true);
			pause.setDisable(false);
		});
		pause.setOnAction(evt -> {
			simulator.setRunning(false);
			play.setDisable(false);
			pause.setDisable(true);
		});
		
		final var controls = new HBox();
		controls.getChildren().addAll(play, pause);
		root.getChildren().addAll(tabPane, controls);
		
		VBox.setVgrow(tabPane, Priority.ALWAYS);
		HBox.setHgrow(tabPane, Priority.ALWAYS);
		HBox.setHgrow(play, Priority.ALWAYS);
		HBox.setHgrow(pause, Priority.ALWAYS);
		return root;
	}
	
	private Collection<? extends Tab> buildTabs(final SimulationParameters simulationParameters){
		return List.of(new SensorsCapacityChartTab(simulationParameters.getEnvironment().getElements(Sensor.class)), new MapTab(this.getStage().getScene(), simulationParameters.getEnvironment().getElements(Positionable.class)));
	}
	
	private void setIcon(final Image icon){
		this.stage.getIcons().clear();
		this.stage.getIcons().add(icon);
		Taskbar.getTaskbar().setIconImage(SwingFXUtils.fromFXImage(icon, null));
	}
	
	public Image getIcon(){
		return null;
	}
	
	public Scene buildScene(final Stage stage){
		return new Scene(createContent(stage), 640, 640);
	}
	
	public String getFrameTitle(){
		return "Simulator Charts";
	}
	
	@SuppressWarnings("Duplicates")
	private Consumer<Stage> getOnStageDisplayed(){
		return stage -> {
			stage.setOnCloseRequest(evt -> {
				if(Objects.nonNull(simulator)){
					simulator.stop();
				}
			});
			
			try{
				System.loadLibrary("jniortools");
			}
			catch(final Throwable e){
				LOGGER.error("Failed to load ORTools library", e);
				JFXUtils.displayExceptionAlert(e, "Simulator error", "Error while starting", "The simulator could not be initialized. Please add library path with java argument -Djava.library.path=/path/to/folder");
				System.exit(1);
			}
			LOGGER.info("Starting simulator version {}", Main.getSimulatorVersion());
			
			final var parameters = new CLIParameters();
			try{
				JCommander.newBuilder().addObject(parameters).build().parse(this.getParameters().getRaw().toArray(new String[0]));
			}
			catch(final ParameterException e){
				LOGGER.error("Failed to parse arguments", e);
				e.usage();
				System.exit(1);
			}
			
			SimulationParameters simulationParameters = null;
			try{
				simulationParameters = SimulationParameters.loadFomFile(Paths.get(parameters.getJsonConfigFile().toURI()));
				LOGGER.trace("Params: {}", simulationParameters);
			}
			catch(final Exception e){
				LOGGER.error("Failed to load parameters", e);
			}
			if(Objects.nonNull(simulationParameters)){
				this.tabPane.getTabs().addAll(buildTabs(simulationParameters));
				this.stage.setMaximized(true);
				
				simulator = Simulator.getSimulator(simulationParameters.getEnvironment());
				simulator.setDelay(250);
				simulator.setRunning(false);
				final var executor = Executors.newSingleThreadScheduledExecutor();
				executor.schedule(() -> simulator.run(), 5, TimeUnit.MILLISECONDS);
				executor.shutdown();
			}
		};
	}
	
	public Stage getStage(){
		return stage;
	}
	
	public static void main(final String[] args){
		launch(args);
	}
}
