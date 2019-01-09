//
// Copyright 2012 Google
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package fr.mrcraftcod.simulator.utils;

import com.google.ortools.constraintsolver.FirstSolutionStrategy;
import com.google.ortools.constraintsolver.NodeEvaluator2;
import com.google.ortools.constraintsolver.RoutingModel;
import com.google.ortools.constraintsolver.RoutingSearchParameters;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Sample showing how to model and solve a capacitated vehicle routing problem
 * with time windows using the swig-wrapped version of the vehicle routing
 * library in src/constraint_solver.
 */

@SuppressWarnings("ALL")
public class CapacitatedVehicleRoutingProblemWithTimeWindows{
	private static final Logger LOGGER = LoggerFactory.getLogger(CapacitatedVehicleRoutingProblemWithTimeWindows.class);
	// Locations representing either an order location or a vehicle route
	// start/end.
	private final List<Pair<Integer, Integer>> locations = new ArrayList<>();
	
	// Quantity to be picked up for each order.
	private final List<Integer> orderDemands = new ArrayList<>();
	// Time window in which each order must be performed.
	private final List<Pair<Integer, Integer>> orderTimeWindows = new ArrayList<>();
	// Penalty cost "paid" for dropping an order.
	private final List<Integer> orderPenalties = new ArrayList<>();
	// Latest time at which each vehicle must end its tour.
	private final List<Integer> vehicleEndTime = new ArrayList<>();
	// Cost per unit of distance of each vehicle.
	private final List<Integer> vehicleCostCoefficients = new ArrayList<>();
	// Random number generator to produce data.
	private final Random randomGenerator = new Random(0xBEEF);
	// Capacity of the vehicles.
	private int vehicleCapacity = 0;
	// Vehicle start and end indices. They have to be implemented as int[] due
	// to the available SWIG-ed interface.
	private int[] vehicleStarts;
	private int[] vehicleEnds;
	
	/**
	 * Constructs a capacitated vehicle routing problem with time windows.
	 */
	private CapacitatedVehicleRoutingProblemWithTimeWindows(){}
	
	public static void main(final String[] args){
		System.loadLibrary("jniortools");
		
		final var problem = new CapacitatedVehicleRoutingProblemWithTimeWindows();
		final var xMax = 20;
		final var yMax = 20;
		final var demandMax = 3;
		final var timeWindowMax = 24 * 60;
		final var timeWindowWidth = 4 * 60;
		final var penaltyMin = 50;
		final var penaltyMax = 100;
		final var endTime = 24 * 60;
		final var costCoefficientMax = 3;
		
		final var orders = 100;
		final var vehicles = 1;
		final var capacity = 50;
		
		problem.buildOrders(orders, xMax, yMax, demandMax, timeWindowMax, timeWindowWidth, penaltyMin, penaltyMax);
		problem.buildFleet(vehicles, xMax, yMax, endTime, capacity, costCoefficientMax);
		problem.solve(orders, vehicles);
	}
	
	/**
	 * Creates order data. Location of the order is random, as well as its
	 * demand (quantity), time window and penalty.
	 *
	 * @param numberOfOrders  number of orders to build.
	 * @param xMax            maximum x coordinate in which orders are located.
	 * @param yMax            maximum y coordinate in which orders are located.
	 * @param demandMax       maximum quantity of a demand.
	 * @param timeWindowMax   maximum starting time of the order time window.
	 * @param timeWindowWidth duration of the order time window.
	 * @param penaltyMin      minimum pernalty cost if order is dropped.
	 * @param penaltyMax      maximum pernalty cost if order is dropped.
	 */
	private void buildOrders(final int numberOfOrders, final int xMax, final int yMax, final int demandMax, final int timeWindowMax, final int timeWindowWidth, final int penaltyMin, final int penaltyMax){
		LOGGER.info("Building orders.");
		for(var order = 0; order < numberOfOrders; ++order){
			locations.add(Pair.of(randomGenerator.nextInt(xMax + 1), randomGenerator.nextInt(yMax + 1)));
			orderDemands.add(randomGenerator.nextInt(demandMax + 1));
			final var timeWindowStart = randomGenerator.nextInt(timeWindowMax + 1);
			orderTimeWindows.add(Pair.of(timeWindowStart, timeWindowStart + timeWindowWidth));
			orderPenalties.add(randomGenerator.nextInt(penaltyMax - penaltyMin + 1) + penaltyMin);
		}
	}
	
	/**
	 * Creates fleet data. Vehicle starting and ending locations are random, as
	 * well as vehicle costs per distance unit.
	 *
	 * @param numberOfVehicles
	 * @param xMax               maximum x coordinate in which orders are located.
	 * @param yMax               maximum y coordinate in which orders are located.
	 * @param endTime            latest end time of a tour of a vehicle.
	 * @param capacity           capacity of a vehicle.
	 * @param costCoefficientMax maximum cost per distance unit of a vehicle
	 *                           (mimimum is 1),
	 */
	private void buildFleet(final int numberOfVehicles, final int xMax, final int yMax, final int endTime, final int capacity, final int costCoefficientMax){
		LOGGER.info("Building fleet.");
		vehicleCapacity = capacity;
		vehicleStarts = new int[numberOfVehicles];
		vehicleEnds = new int[numberOfVehicles];
		for(var vehicle = 0; vehicle < numberOfVehicles; ++vehicle){
			vehicleStarts[vehicle] = locations.size();
			locations.add(Pair.of(randomGenerator.nextInt(xMax + 1), randomGenerator.nextInt(yMax + 1)));
			vehicleEnds[vehicle] = locations.size();
			locations.add(Pair.of(randomGenerator.nextInt(xMax + 1), randomGenerator.nextInt(yMax + 1)));
			vehicleEndTime.add(endTime);
			vehicleCostCoefficients.add(randomGenerator.nextInt(costCoefficientMax) + 1);
		}
	}
	
	/**
	 * Solves the current routing problem.
	 */
	private void solve(final int numberOfOrders, final int numberOfVehicles){
		LOGGER.info("Creating model with " + numberOfOrders + " orders and " + numberOfVehicles + " vehicles.");
		// Finalizing model
		final var numberOfLocations = locations.size();
		
		final var model = new RoutingModel(numberOfLocations, numberOfVehicles, vehicleStarts, vehicleEnds);
		
		// Setting up dimensions
		final var bigNumber = 100000;
		final NodeEvaluator2 manhattanCallback = new NodeEvaluator2(){
			@Override
			public long run(final int firstIndex, final int secondIndex){
				try{
					final var firstLocation = locations.get(firstIndex);
					final var secondLocation = locations.get(secondIndex);
					return Math.abs(firstLocation.getLeft() - secondLocation.getLeft()) + Math.abs(firstLocation.getRight() - secondLocation.getRight());
				}
				catch(final Throwable thrown){
					LOGGER.warn("", thrown);
					return 0;
				}
			}
		};
		model.addDimension(manhattanCallback, bigNumber, bigNumber, false, "time");
		final NodeEvaluator2 demandCallback = new NodeEvaluator2(){
			@Override
			public long run(final int firstIndex, final int secondIndex){
				try{
					if(firstIndex < numberOfOrders){
						return orderDemands.get(firstIndex);
					}
					return 0;
				}
				catch(final Throwable thrown){
					LOGGER.warn("", thrown);
					return 0;
				}
			}
		};
		model.addDimension(demandCallback, 0, vehicleCapacity, true, "capacity");
		
		// Setting up vehicles
		for(var vehicle = 0; vehicle < numberOfVehicles; ++vehicle){
			final int costCoefficient = vehicleCostCoefficients.get(vehicle);
			final NodeEvaluator2 manhattanCostCallback = new NodeEvaluator2(){
				@Override
				public long run(final int firstIndex, final int secondIndex){
					try{
						final var firstLocation = locations.get(firstIndex);
						final var secondLocation = locations.get(secondIndex);
						return costCoefficient * (Math.abs(firstLocation.getLeft() - secondLocation.getLeft()) + Math.abs(firstLocation.getRight() - secondLocation.getRight()));
					}
					catch(final Throwable thrown){
						LOGGER.warn("", thrown);
						return 0;
					}
				}
			};
			model.setArcCostEvaluatorOfVehicle(manhattanCostCallback, vehicle);
			model.cumulVar(model.end(vehicle), "time").setMax(vehicleEndTime.get(vehicle));
		}
		
		// Setting up orders
		for(var order = 0; order < numberOfOrders; ++order){
			final var cumulVar = model.cumulVar(order, "time");
			cumulVar.setRange(orderTimeWindows.get(order).getLeft(), orderTimeWindows.get(order).getRight());
			cumulVar.removeInterval(orderTimeWindows.get(order).getLeft(), orderTimeWindows.get(order).getRight() - 50);
			LOGGER.warn("NODE {} from {}, to {}", order, orderTimeWindows.get(order).getRight() - 50, orderTimeWindows.get(order).getRight());
			final int[] orders = {order};
			model.addDisjunction(orders, orderPenalties.get(order));
		}
		
		// Solving
		final var parameters = RoutingSearchParameters.newBuilder().mergeFrom(RoutingModel.defaultSearchParameters()).setFirstSolutionStrategy(FirstSolutionStrategy.Value.ALL_UNPERFORMED).build();
		
		LOGGER.info("Search");
		final var solution = model.solveWithParameters(parameters);
		if(solution != null){
			final var output = new StringBuilder("Total cost: " + solution.objectiveValue() + "\n");
			// Dropped orders
			var dropped = "";
			for(var order = 0; order < numberOfOrders; ++order){
				if(solution.value(model.nextVar(order)) == order){
					dropped += " " + order;
				}
			}
			if(dropped.length() > 0){
				output.append("Dropped orders:").append(dropped).append("\n");
			}
			// Routes
			for(var vehicle = 0; vehicle < numberOfVehicles; ++vehicle){
				final var route = new StringBuilder("Vehicle " + vehicle + ": ");
				var order = model.start(vehicle);
				if(model.isEnd(solution.value(model.nextVar(order)))){
					route.append("Empty");
				}
				else{
					for(; !model.isEnd(order); order = solution.value(model.nextVar(order))){
						final var load = model.cumulVar(order, "capacity");
						final var time = model.cumulVar(order, "time");
						route.append(order).append(" Load(").append(solution.value(load)).append(") ").append("Time(").append(solution.min(time)).append(", ").append(solution.max(time)).append(") -> ");
					}
					final var load = model.cumulVar(order, "capacity");
					final var time = model.cumulVar(order, "time");
					route.append(order).append(" Load(").append(solution.value(load)).append(") ").append("Time(").append(solution.min(time)).append(", ").append(solution.max(time)).append(")");
				}
				output.append(route).append("\n");
			}
			LOGGER.info(output.toString());
		}
	}
}