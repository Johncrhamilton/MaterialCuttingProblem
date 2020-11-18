package model.search.algorithms;

import java.util.ArrayList;

import model.MaterialCuttingProblem;
import model.ModelConstants;
import model.Order;
import model.SearchAlgorithm;

public class BaseEvolutionAlgorithm  implements SearchAlgorithm {

	private MaterialCuttingProblem materialCuttingProblem;

	private ArrayList<Order> population;
	private Order fittestIndividual;
	private double fittestIndividualCost;
	
	public BaseEvolutionAlgorithm(MaterialCuttingProblem materialCuttingProblem) 
	{
		this.materialCuttingProblem = materialCuttingProblem;		
	}
	
	public Order bestOrder()
	{
		population = initialisation(ModelConstants.POPULATION_SIZE);
		
		System.out.println("Starting fittest individual: " + fittestIndividual.toString() + " Cost: " + fittestIndividualCost);
		
		if(ModelConstants.LIMITED_ITERATIONS) 
		{
			int currentIteration = 0;
			while(currentIteration < ModelConstants.ITERATION_LIMIT) 
			{
				oneGeneration();
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
				oneGeneration();
				currentTime = java.lang.System.currentTimeMillis();
			}
		}
		
		return fittestIndividual;
	}
	
	/**
	 * One iteration of Local Search
	 */
	private void oneGeneration() 
	{
		//Parent Selection and offspring creation through Recombination and mutation
		ArrayList<Order> offspringPopulation = produceOffspring(population);
		
		//Evaluate Individuals
		evaluatePopulation(offspringPopulation);
		
		//SurvivorSelection
		generationalSurvivorSelection(offspringPopulation);
	}

	/**
	 * Produce Offspring from parents
	 * @param currentPopulation 
	 * @param parents
	 * @return ArrayList<Order> offspring
	 */
	private ArrayList<Order> produceOffspring(ArrayList<Order> currentPopulation) 
	{
		Order parentOne = tournamentParentSelection(currentPopulation);
		Order parentTwo = tournamentParentSelection(currentPopulation);
		
		//TODO
		return null;
	}
	
	/**
	 * Evaluate Population and update fittest individual
	 * @param offspringPopulation
	 */
	private void evaluatePopulation(ArrayList<Order> offspringPopulation) 
	{		
		for(int i = 0; i < offspringPopulation.size(); i++) 
		{
			Order individual = offspringPopulation.get(i);
			double individualCost = materialCuttingProblem.calculateCostOfOrder(individual);
			
			if(individualCost < fittestIndividualCost) 
			{
				fittestIndividual = individual;
				fittestIndividualCost = individualCost;
			}			
		}
	}
	
	/**
	 * Generational Survivor Selection with optional ELITISM
	 * @param offspringPopulation
	 */
	private void generationalSurvivorSelection(ArrayList<Order> offspringPopulation) 
	{
		if(ModelConstants.ELITISM) 
		{
			offspringPopulation.remove(ModelConstants.random.nextInt(offspringPopulation.size()));
			offspringPopulation.add(fittestIndividual);
		}
		
		population = offspringPopulation;
	}
	
	/**
	 * Look a TOURNAMENT_SELECTIVE_SAMPLE number of random parents (Orders) and pick the best one
	 * @param currentPopulation
	 * @return Order
	 */
	private Order tournamentParentSelection(ArrayList<Order> currentPopulation) 
	{
		Order fittestParent = null;
		double fittestParentCost = Double.MAX_VALUE;
		
		for(int i = 0; i < ModelConstants.TOURNAMENT_SELECTIVE_SAMPLE; i++) 
		{
			Order randomParent = currentPopulation.get(ModelConstants.random.nextInt(currentPopulation.size()));
			double randomParentCost = materialCuttingProblem.calculateCostOfOrder(randomParent);
			
			if(randomParentCost < fittestParentCost) 
			{
				fittestParent = randomParent;
				fittestParentCost = randomParentCost;
			}
		}

		return fittestParent;
	}

	/**
	 * Initialisation done by filling up a collection with random valid routes
	 * @param populationSize
	 * @return ArrayList<Route> initialPopulation
	 */
	private ArrayList<Order> initialisation(int populationSize)
	{
		ArrayList<Order> initialPopulation = new ArrayList<Order>(populationSize);
		
		fittestIndividual = null;
		fittestIndividualCost = Double.MAX_VALUE;

		for(int i = 0; i < populationSize; i++) 
		{
			Order individual = materialCuttingProblem.generateRandomValidOrder();
			double individualCost = materialCuttingProblem.calculateCostOfOrder(fittestIndividual);

			if(individualCost < fittestIndividualCost) 
			{
				fittestIndividual = individual;
				fittestIndividualCost = individualCost;
			}

			initialPopulation.add(individual);
		}

		return initialPopulation;
	}	
}
