package fr.mrcraftcod.simulator.jfx.tabs.sensor;

import fr.mrcraftcod.simulator.jfx.utils.MetricEventListenerTab;
import fr.mrcraftcod.simulator.metrics.MetricEvent;
import fr.mrcraftcod.simulator.metrics.events.SensorCapacityMetricEvent;
import fr.mrcraftcod.simulator.sensors.Sensor;
import javafx.application.Platform;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import java.util.Objects;

/**
 * Draws the capacity of a sensor in a graph.
 * <p>
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2019-01-17.
 *
 * @author Thomas Couchoud
 * @since 2019-01-17
 */
public class SensorCapacityChartTab extends MetricEventListenerTab{
	
	private final Sensor sensor;
	private final XYChart.Series<Number, Number> series;
	
	/**
	 * Constructor;
	 *
	 * @param sensor The sensor to track.
	 */
	public SensorCapacityChartTab(final Sensor sensor){
		this.sensor = sensor;
		
		final var xAxis = new NumberAxis();
		final var yAxis = new NumberAxis();
		
		xAxis.setAnimated(false);
		xAxis.setLabel("Simulation time");
		
		yAxis.setAnimated(false);
		yAxis.setLabel(String.format("Capacity of Sensor[%d]", sensor.getID()));
		
		series = new XYChart.Series<>();
		series.setName("Capacity");
		
		final var chart = new LineChart<>(xAxis, yAxis);
		chart.setAnimated(false);
		chart.getData().add(series);
		chart.setCreateSymbols(false);
		
		this.setContent(chart);
		this.setClosable(false);
		this.setText(String.format("Capacity sensor[%d]", sensor.getID()));
	}
	
	@Override
	public void onEvent(final MetricEvent event){
		if(event instanceof SensorCapacityMetricEvent && Objects.equals(this.sensor, ((SensorCapacityMetricEvent) event).getElement())){
			Platform.runLater(() -> series.getData().add(new XYChart.Data<>(event.getTime(), ((SensorCapacityMetricEvent) event).getNewValue().get())));
		}
	}
	
	@Override
	public void close(){
	
	}
}
