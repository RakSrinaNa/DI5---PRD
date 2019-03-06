package fr.mrcraftcod.simulator.jfx.tabs.sensor;

import fr.mrcraftcod.simulator.jfx.utils.MetricEventListenerTab;
import fr.mrcraftcod.simulator.metrics.MetricEvent;
import fr.mrcraftcod.simulator.metrics.events.SensorCapacityMetricEvent;
import fr.mrcraftcod.simulator.sensors.Sensor;
import javafx.application.Platform;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import java.util.Collection;
import java.util.HashMap;

/**
 * Draws the capacity of several sensors in a graph.
 * <p>
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2019-01-17.
 *
 * @author Thomas Couchoud
 * @since 2019-01-17
 */
public class SensorsCapacityChartTab extends MetricEventListenerTab{
	
	private final HashMap<Integer, Sensor> sensors;
	private final HashMap<Integer, XYChart.Series<Number, Number>> series;
	
	/**
	 * Constructor.
	 *
	 * @param sensors The sensors to track.
	 */
	public SensorsCapacityChartTab(final Collection<? extends Sensor> sensors){
		this.sensors = new HashMap<>();
		this.series = new HashMap<>();
		for(final var s : sensors){
			this.sensors.put(s.getID(), s);
			final var series1 = new XYChart.Series<Number, Number>();
			series1.setName(String.format("Sensor[%d]", s.getID()));
			this.series.put(s.getID(), series1);
		}
		
		final var xAxis = new NumberAxis();
		final var yAxis = new NumberAxis();
		
		xAxis.setAnimated(false);
		xAxis.setLabel("Simulation time");
		
		yAxis.setAnimated(false);
		yAxis.setLabel("Capacity");
		
		final var chart = new LineChart<>(xAxis, yAxis);
		chart.setAnimated(false);
		chart.getData().addAll(series.values());
		chart.setCreateSymbols(false);
		
		this.setContent(chart);
		this.setClosable(false);
		this.setText("Sensors capacity");
	}
	
	@Override
	public void onEvent(final MetricEvent event){
		if(event instanceof SensorCapacityMetricEvent && sensors.values().contains(((SensorCapacityMetricEvent) event).getElement())){
			Platform.runLater(() -> series.get(((SensorCapacityMetricEvent) event).getElement().getID()).getData().add(new XYChart.Data<>(event.getTime(), ((SensorCapacityMetricEvent) event).getNewValue().get())));
		}
	}
	
	@Override
	public void close(){
	
	}
}
