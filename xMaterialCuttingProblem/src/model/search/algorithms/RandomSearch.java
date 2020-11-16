package model.search.algorithms;

import model.MaterialCuttingProblem;
import model.ModelConstants;
import model.Order;
import model.SearchAlgorithm;

public class RandomSearch implements SearchAlgorithm {

	private MaterialCuttingProblem materialCuttingProblem;
	
	private Order bestOrder;
	private double bestOrderCost;
	
	public RandomSearch(MaterialCuttingProblem materialCuttingProblem) 
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
	
	private void oneIteration() 
	{
		Order randomOrder = materialCuttingProblem.generateRandomValidOrder();
		double randomOrderCost = materialCuttingProblem.calculateCostOfOrder(randomOrder);
		
		if(randomOrderCost < bestOrderCost) 
		{
			bestOrder = randomOrder;
			bestOrderCost = randomOrderCost;
		}
	}
	
}
