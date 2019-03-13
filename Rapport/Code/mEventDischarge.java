class DischargeSensorEvent extends SimulationEvent{
    DischargeSensorEvent(final double time){ super(time); }
    
    @Override
    public void accept(final Environment environment){
        environment.getElements(Sensor.class).forEach(s -> {
            s.removeCapacity(s.getDischargeSpeed());
            environment.getSimulator().getMetricEventDispatcher().dispatchEvent(new SensorCapacityMetricEvent(environment, getTime(), s, s::getCurrentCapacity));
        });
        environment.getSimulator().getMetricEventDispatcher().dispatchEvent(new SensorsCapacityMetricEvent(environment, getTime()));
        environment.getSimulator().getUnreadableQueue().add(new DischargeSensorEvent(getTime() + 1));
    }
}