package model.search.algorithms;

import java.util.ArrayList;

import model.CutActivity;
import model.MCutProblem;
import model.ModelConstants;
import model.Order;
import model.OrderException;
import model.SearchAlgorithm;

public class LocalSearch implements SearchAlgorithm {

	private MCutProblem materialCuttingProblem;

	private Order bestOrder;
	private double bestOrderCost;

	public LocalSearch(MCutProblem materialCuttingProblem)
	{
		this.materialCuttingProblem = materialCuttingProblem;
	}

	public Order bestOrder() 
	{
		bestOrder = materialCuttingProblem.generateRandomValidOrder();
		bestOrderCost = materialCuttingProblem.calculateCostOfOrder(bestOrder);

		if(ModelConstants.LIMITED_ITERATIONS)
		{
			int currentIteration = 0;
			while(currentIteration < ModelConstants.ITERATION_LIMIT) 
			{
				oneIteration();
				currentIteration++;
			}
			System.out.println("Number of iterations: " + currentIteration);
		}
		else 
		{
			//Current time in milliseconds
			long currentTime = java.lang.System.currentTimeMillis();

			//Time Limit in milliseconds
			long timeLimit = currentTime + ModelConstants.TIME_LIMIT;

			while(currentTime < timeLimit) 
			{
				oneIteration();
				currentTime = java.lang.System.currentTimeMillis();
			}
		}

		return bestOrder;
	}

	/**
	 * One iteration of Local Search
	 */
	private void oneIteration() 
	{
		//Create neighbourhood
		ArrayList<Order> neighbourhood = generateNeighbourhood(bestOrder);

		//Find best neighbour in the neighbourhood
		Order bestNeighbour = bestNeighbourStepFunction(neighbourhood);
		double bestNeighbourCost = materialCuttingProblem.calculateCostOfOrder(bestNeighbour);

		if(bestNeighbourCost < bestOrderCost) 
		{
			bestOrder = bestNeighbour;
			bestOrderCost = bestNeighbourCost;
		}
	}

	/**
	 * Generate a Neighbourhood for a given order
	 * @param order
	 * @return Neighbourhood of orders
	 */
	private ArrayList<Order> generateNeighbourhood(Order order)
	{
		ArrayList<Order> Neighbourhood = new ArrayList<Order>();

		//Create neighbours to the given order
		for(int i = 0; i < order.size() - 1; i++) 
		{
			int j = i + 1;
			Order neighbour = order.clone();

			//Sellect two activities to change
			CutActivity activityOne = neighbour.get(i).clone();
			CutActivity activityTwo = neighbour.get(j).clone();

			//Remove them
			neighbour.remove(activityOne);
			neighbour.remove(activityTwo);

			//Fill a activityCutLengths list with their lengths
			ArrayList<Float> activityCutLengths = new ArrayList<Float>();
			activityCutLengths.addAll(activityOne.getLengths());
			activityCutLengths.addAll(activityTwo.getLengths());

			//Create and add new CutActivities from activityCutLengths
			neighbour.addAll(materialCuttingProblem.generateRandomValidCutActivities(activityCutLengths));

			//Add neighbour to neighbourhood
			if(neighbour.isComplete())
			{
				Neighbourhood.add(neighbour);
			}
			else
			{
				throw new OrderException("Tried to add order which is not complete." + neighbour.toString());
			}
		}

		return Neighbourhood;			
	}

	/**
	 * Find the best Neighbour in the given Neighbourhood
	 * @param Neighbourhood
	 * @return bestNeighbour
	 */
	private Order bestNeighbourStepFunction(ArrayList<Order> Neighbourhood) 
	{
		Order bestNeighbour = null;
		double bestNeighbourCost = Double.MAX_VALUE;

		for(Order neighbour : Neighbourhood) 
		{
			double neighbourCost = materialCuttingProblem.calculateCostOfOrder(neighbour);

			if(neighbourCost < bestNeighbourCost) 
			{
				bestNeighbour = neighbour;
				bestNeighbourCost = neighbourCost;
			}
		}

		return bestNeighbour;	
	}	
}
