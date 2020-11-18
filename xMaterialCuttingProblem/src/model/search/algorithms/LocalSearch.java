package model.search.algorithms;

import java.util.ArrayList;

import model.CutActivity;
import model.MaterialCuttingProblem;
import model.ModelConstants;
import model.Order;
import model.SearchAlgorithm;

public class LocalSearch implements SearchAlgorithm {

	private MaterialCuttingProblem materialCuttingProblem;
	
	private Order bestOrder;
	private double bestOrderCost;
	
	public LocalSearch(MaterialCuttingProblem materialCuttingProblem) 
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
	 * @return ArrayList<Order>
	 */
	private ArrayList<Order> generateNeighbourhood(Order order)
	{
		ArrayList<Order> Neighbourhood = new ArrayList<Order>();
		
		//Create neighbours to the given order
		for(int i = 0; i < order.getCutActivities().size(); i++) 
		{
			for(int j = 0; j < order.getCutActivities().size(); j++) 
			{
				if(i < j) 
				{
					Order neighbour = order.clone();
					
					//Sellect two activities to change
					CutActivity activityOne = neighbour.getCutActivity(i);
					CutActivity activityTwo = neighbour.getCutActivity(j);
					//Remove them
					neighbour.removeCutActivity(activityOne);
					neighbour.removeCutActivity(activityTwo);
					
					//Fill a activityCutLengths list with their lengths
					ArrayList<Float> activityCutLengths = new ArrayList<Float>();
					activityCutLengths.addAll(activityOne.getCutLengths());
					activityCutLengths.addAll(activityTwo.getCutLengths());
					
					//Create and add new CutActivities from activityCutLengths
					neighbour.addAll(materialCuttingProblem.generateRandomValidCutActivities(activityCutLengths));
					
					Neighbourhood.add(neighbour);
				}				
			}			
		}
		
		return Neighbourhood;			
	}
	
	/**
	 * Find the best Neighbour in the given Neighbourhood
	 * @param Neighbourhood
	 * @return
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
