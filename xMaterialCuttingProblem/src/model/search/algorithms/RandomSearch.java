package model.search.algorithms;

import model.MCutProblem;
import model.ModelConstants;
import model.Order;
import model.SearchAlgorithm;

public class RandomSearch implements SearchAlgorithm {

	private MCutProblem materialCuttingProblem;

	private Order bestOrder;
	private double bestOrderFitness;

	public RandomSearch(MCutProblem materialCuttingProblem) 
	{
		this.materialCuttingProblem = materialCuttingProblem;
	}

	public Order bestOrder() 
	{
		bestOrder = materialCuttingProblem.generateRandomValidOrder();
		bestOrderFitness = materialCuttingProblem.calculateFitnessOfOrder(bestOrder);

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

	/**
	 * One iteration of random search
	 */
	private void oneIteration() 
	{
		Order randomOrder = materialCuttingProblem.generateRandomValidOrder();
		double randomOrderFitness = materialCuttingProblem.calculateFitnessOfOrder(randomOrder);

		if(randomOrderFitness < bestOrderFitness) 
		{
			bestOrder = randomOrder;
			bestOrderFitness = randomOrderFitness;
		}
	}

}
