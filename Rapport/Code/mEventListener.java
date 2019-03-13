public class SensorCapacityMetricEventListener implements MetricEventListener{
    private final PrintWriter outputFile;
    
    public SensorCapacityMetricEventListener(final Environment environment) throws FileNotFoundException{
        final var path = MetricEvent.getMetricSaveFolder(environment).resolve("sensor") .resolve("capacity.csv");
        outputFile = new PrintWriter(new FileOutputStream(path.toFile()));
        outputFile.print("time");
        outputFile.print(CSV_SEPARATOR);
        outputFile.println(environment.getElements(Sensor.class).stream() .map(Identifiable::getUniqueIdentifier).sorted() .collect(Collectors.joining(CSV_SEPARATOR)));
        outputFile.flush();
    }
    
    @Override
    public void onEvent(final MetricEvent event){
        if(event instanceof SensorsCapacityMetricEvent){
            final var evt = (SensorsCapacityMetricEvent) event;
            outputFile.print(evt.getTime());
            outputFile.print(CSV_SEPARATOR);
            outputFile.println(event.getEnvironment().getElements(Sensor.class) .stream().sorted(Comparator .comparing(Identifiable::getUniqueIdentifier)).map(s -> "" + s.getCurrentCapacity()) .collect(Collectors.joining(CSV_SEPARATOR)));
            outputFile.flush();
        }
    }
    
    @Override
    public void close(){
        outputFile.close();
    }
}
