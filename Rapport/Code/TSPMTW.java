//Setup sensors
final var timeDimension = routing.getMutableDimension("time");
timeDimension.setGlobalSpanCostCoefficient(1);
for(var i = 0; i < this.getTour().getStops().size(); i++){
    final var stopIndex = routing.nodeToIndex(i);
    final var timeWindowCumulVar = timeDimension.cumulVar(stopIndex);
    timeWindowCumulVar.setRange(0, MAX_DIMENSION_RANGE);
    for(final var occupation : getTour().getStops().get(i).getForbiddenTimes()){
        timeWindowCumulVar.removeInterval((long) occupation.getLeft().doubleValue(), (long) occupation.getRight().doubleValue());
        LOGGER.info("Node {} forbidden from {}, to {}", stopIndex, occupation.getLeft(), occupation.getRight());
    }
}