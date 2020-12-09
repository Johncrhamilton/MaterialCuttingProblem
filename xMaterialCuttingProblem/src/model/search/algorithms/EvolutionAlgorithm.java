package model.search.algorithms;

import java.util.ArrayList;
import java.util.Collections;

import model.CutActivity;
import model.MCutProblem;
import model.ModelConstants;
import model.Order;
import model.OrderException;
import model.SearchAlgorithm;

public class EvolutionAlgorithm  implements SearchAlgorithm {

	private MCutProblem materialCuttingProblem;

	private ArrayList<Order> currentPopulation;
	private Order fittestIndividual;
	private double fittestIndividualFitness;

	public EvolutionAlgorithm(MCutProblem materialCuttingProblem)
	{
		this.materialCuttingProblem = materialCuttingProblem;
	}

	public Order bestOrder()
	{
		fittestIndividual = null;
		fittestIndividualFitness = Double.MAX_VALUE;
		
		currentPopulation = initialisation();

		if(ModelConstants.LIMITED_ITERATIONS) 
		{
			int currentIteration = 0;
			while(currentIteration < ModelConstants.ITERATION_LIMIT) 
			{
				oneGeneration();
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
				oneGeneration();
				currentTime = java.lang.System.currentTimeMillis();
			}
		}
		
		//Evaluate the population
		evaluatePopulation(currentPopulation);

		return fittestIndividual;
	}

	/**
	 * Initialisation done by filling up a collection with random valid Orders
	 * @return initialPopulation
	 */
	private ArrayList<Order> initialisation()
	{
		ArrayList<Order> initialPopulation = new ArrayList<Order>(ModelConstants.POPULATION_SIZE);

		for(int i = 0; i < ModelConstants.POPULATION_SIZE; i++) 
		{
			initialPopulation.add(materialCuttingProblem.generateRandomValidOrder());
		}

		return initialPopulation;
	}	

	/**
	 * One Generation of the Evolutionary Algorithm
	 */
	private void oneGeneration()
	{
		//Evaluate Individuals and update fittestIndividual for Centre of Mass
		evaluatePopulation(currentPopulation);
		
		//Parent Selection and offspring creation through Recombination and mutation
		ArrayList<Order> offspringPopulation = produceOffspring();

		//Survivor Selection
		ArrayList<Order> survivorPopulation = generationalSurvivorSelection(offspringPopulation);

		//Update the current population
		currentPopulation = survivorPopulation;
	}

	/**
	 * Produce Offspring from parents
	 * @param population 
	 * @param parents
	 * @return offspring population
	 */
	private ArrayList<Order> produceOffspring() 
	{
		ArrayList<Order> offspring = new ArrayList<Order>(ModelConstants.OFFSPRING_POPULATION_SIZE);
		ArrayList<Order> parentPopulation = centreOfMassSelection();

		while(offspring.size() < ModelConstants.OFFSPRING_POPULATION_SIZE) 
		{
			//Select two parents
			Order parentOne = parentPopulation.get(ModelConstants.RANDOM.nextInt(parentPopulation.size()));
			Order parentTwo = parentPopulation.get(ModelConstants.RANDOM.nextInt(parentPopulation.size()));

			//Create two Offspring through recombination
			Order[] twoOffspring = recombination(parentOne, parentTwo);

			//Add Offspring with potential mutations
			offspring.add(mutation(twoOffspring[0]));
			offspring.add(mutation(twoOffspring[1]));
		}

		return offspring;
	}

	/**
	 * Creates a parent population around the Centre Of Mass Individual (Fittest Individual)
	 * @param population
	 * @return Parent Population
	 */
	private ArrayList<Order> centreOfMassSelection()
	{
		//The centreOfMass is the fittestIndividual and the centreOfMassPopulation is built around this individual
		ArrayList<Order> centreOfMassPopulation = new ArrayList<Order>(ModelConstants.CENTRE_OF_MASS_NEIGHBOURHOOD_SIZE);
		Order centreOfMass = fittestIndividual;

		//Stock lengths, shortestStockLength and longestStockLength for reference in formula
		ArrayList<Float> stockLengths = new ArrayList<Float>(materialCuttingProblem.getStockLengthsAndCosts().keySet());
		float shortestStockLength = materialCuttingProblem.getShortestStockLength();
		float longestStockLength = materialCuttingProblem.getLongestStockLength();

		//Keep track of the ith new individual
		int index = 1;

		//Create neighbours to the given order
		while(centreOfMassPopulation.size() < ModelConstants.CENTRE_OF_MASS_NEIGHBOURHOOD_SIZE) 
		{
			Order individual = new Order(materialCuttingProblem.getOrderedLengthsAndQuantities());

			//Ordered lengths that need to be added to new individual
			ArrayList<Float> orderedLengthsNeeded = (ArrayList<Float>) materialCuttingProblem.allOrderLengths.clone();
			Collections.shuffle(orderedLengthsNeeded);

			//Calculate and convert new stocklengths that will be used to create cutActivities for the new individual
			ArrayList<Float> newStockLengths = new ArrayList<Float>(centreOfMass.size());

			for(int i = 0; i < centreOfMass.size(); i++) 
			{
				float calculatedStockLength;

				//If true try increase the Stock Length teir else try decrease the Stock Length teir
				if(ModelConstants.RANDOM.nextFloat() < ModelConstants.CENTRE_OF_MASS_POSITIVE_TO_NEGATIVE_DISTRIBUTION) 
				{
					//Increase formula: newStockLength = currentStockLength + (UpperBoundStockLength * Random(0,1) / index)
					calculatedStockLength = centreOfMass.get(i).getStockLength() + ((longestStockLength * ModelConstants.RANDOM.nextFloat()) / index);
				}
				else 
				{
					//Decrease formula: newStockLength = currentStockLength - (UpperBoundStockLength * Random(0,1) / index)
					calculatedStockLength = centreOfMass.get(i).getStockLength() - ((shortestStockLength * ModelConstants.RANDOM.nextFloat()) / index);
				}

				//Convert the calculated StockLength into a valid StockLength
				float smallestDifference = Float.MAX_VALUE;
				float selectedStockLength = 0f;

				for(int j = 0; j < stockLengths.size(); j++)
				{
					float difference = Math.abs(calculatedStockLength - stockLengths.get(j));
					if(difference < smallestDifference) 
					{
						selectedStockLength = stockLengths.get(j);
						smallestDifference = difference;
					}
				}

				newStockLengths.add(selectedStockLength);
			}

			//Use the newStockLengths to create a new individual
			for(int i = 0; i < newStockLengths.size(); i++) 
			{
				//If there aren't any ordered lengths left to add
				if(!(orderedLengthsNeeded.size() > 0))
				{
					break;
				}

				individual.add(materialCuttingProblem.createCutActivity(newStockLengths.get(i), orderedLengthsNeeded));
			}

			//If there are any left over ordered lengths, add them in as new random valid cut activities
			if(orderedLengthsNeeded.size() > 0)
			{
				individual.addAll(materialCuttingProblem.generateRandomValidCutActivities(orderedLengthsNeeded));
			}			

			//Add neighbour to neighbourhood
			if(individual.isComplete())
			{
				centreOfMassPopulation.add(individual);
			}
			else
			{
				throw new OrderException("Tried to add order which is not valid. " + individual.toString());
			}

			index++;
		}

		return centreOfMassPopulation;
	}

	/**
	 * Perform a recombination the two parents to get two offspring
	 * @param parentOne
	 * @param parentTwo
	 * @return Order[] of two offspring
	 */
	private Order[] recombination(Order parentOne, Order parentTwo) 
	{
		Order[] twoOffspring = {parentOne, parentTwo};

		int startCopyIndex = ModelConstants.RANDOM.nextInt(parentOne.size());

		//Recombination
		if(ModelConstants.RANDOM.nextDouble() < ModelConstants.RECOMBINATION_PROBABILITIY) 
		{
			twoOffspring[0] = orderOneCrossover(parentOne, parentTwo, startCopyIndex);
			twoOffspring[1] = orderOneCrossover(parentTwo, parentOne, startCopyIndex);	
		}

		return twoOffspring;
	}

	/**
	 * Order One Crossover for two Parents
	 * @param firstParent
	 * @param secondParent
	 * @return Order child
	 */
	private Order orderOneCrossover(Order firstParent, Order secondParent, int startCopyIndex) 
	{
		Order child = new Order(materialCuttingProblem.getOrderedLengthsAndQuantities());

		//Keep track of Ordered lengths that still need to be added
		ArrayList<Float> orderedLengthsNeeded = (ArrayList<Float>) materialCuttingProblem.allOrderLengths.clone();

		//In the event that the FirstParent isn't equal in size to the second parent
		//Extract a size proportional to the first parent
		int copyLengthFirstParent = firstParent.size()/2;
		int firstParentIndex = startCopyIndex % firstParent.size();

		//Copy in first parent's cut activities
		for(int i = 0; i < copyLengthFirstParent; i++) 
		{
			CutActivity cutActivity = firstParent.get(firstParentIndex);
			child.add(cutActivity);

			for(int j = 0; j < cutActivity.size(); j++) 
			{
				orderedLengthsNeeded.remove(cutActivity.get(j));
			}

			firstParentIndex = (firstParentIndex + 1) % firstParent.size();
		}

		Collections.shuffle(orderedLengthsNeeded);

		//In the event that the SecondParent isn't equal in size to the first parent
		//Extract a size proportional to the first parent
		int copyLengthSecondParent = secondParent.size()/2;
		int secondParentIndex = (startCopyIndex + copyLengthSecondParent) % secondParent.size();

		//Copy in second parent's cut activities
		for(int i = 0; i < copyLengthSecondParent; i++) 
		{
			//If there aren't any ordered lengths left to add
			if(!(orderedLengthsNeeded.size() > 0))
			{
				break;
			}

			child.add(materialCuttingProblem.createCutActivity(secondParent.get(secondParentIndex).getStockLength(), orderedLengthsNeeded));

			secondParentIndex = (secondParentIndex + 1) % secondParent.size();
		}

		//If there are any left over ordered lengths, add them in as new random valid cut activities
		if(orderedLengthsNeeded.size() > 0)
		{
			child.addAll(materialCuttingProblem.generateRandomValidCutActivities(orderedLengthsNeeded));
		}

		if(child.isComplete()) 
		{
			return child;
		}
		else 
		{
			throw new OrderException("Tried to add order which is not valid. " + child.toString());
		}
	}

	/**
	 * Perform mutation on the order
	 * @param order
	 * @return order with mutated cut activites
	 */
	private Order mutation(Order order) 
	{
		//Mutation
		if(ModelConstants.RANDOM.nextDouble() < ModelConstants.MUTATION_PROBABILITY) 
		{
			//Randomly select two cut activites from order and remove the two cut activites from order
			CutActivity randomActivityOne = order.get(ModelConstants.RANDOM.nextInt(order.size()));			
			order.remove(randomActivityOne);			
			CutActivity randomActivityTwo = order.get(ModelConstants.RANDOM.nextInt(order.size()));
			order.remove(randomActivityTwo);			

			//Create new cut activites from their cut lengths
			ArrayList<Float> activityCutLengths = new ArrayList<Float>();
			activityCutLengths.addAll(randomActivityOne.getLengths());
			activityCutLengths.addAll(randomActivityTwo.getLengths());

			//Add the new mutated cut activites to the order
			order.addAll(materialCuttingProblem.generateRandomValidCutActivities(activityCutLengths));
		}

		if(order.isComplete()) 
		{
			return order;
		}
		else 
		{
			throw new OrderException("Tried to add order which is not valid. " + order.toString());
		}
	}

	/**
	 * Evaluate Population and update fittest individual
	 * @param population
	 */
	private void evaluatePopulation(ArrayList<Order> population) 
	{
		for(Order individual : population) 
		{
			double individualFitness = materialCuttingProblem.calculateFitnessOfOrder(individual);

			if(individualFitness < fittestIndividualFitness) 
			{
				fittestIndividual = individual;
				fittestIndividualFitness = individualFitness;
			}			
		}
	}

	/**
	 * Generational Survivor Selection with optional ELITISM
	 * @param population
	 */
	private ArrayList<Order> generationalSurvivorSelection(ArrayList<Order> population) 
	{
		ArrayList<Order> survivors = new ArrayList<Order>(ModelConstants.POPULATION_SIZE);

		while(survivors.size() < ModelConstants.POPULATION_SIZE)
		{
			survivors.add(tournamentParentSelection(population));
		}

		if(ModelConstants.ELITISM)
		{
			survivors.remove(ModelConstants.RANDOM.nextInt(survivors.size()));
			survivors.add(fittestIndividual);
		}

		return survivors;
	}

	/**
	 * Tournament Selection: Look at a random TOURNAMENT_SELECTIVE_SAMPLE number of random parents (Orders) and pick the best one
	 * @param population
	 * @return fittestParent
	 */
	private Order tournamentParentSelection(ArrayList<Order> population) 
	{
		Order fittestParent = null;
		double fittestParentFitness = Double.MAX_VALUE;

		for(int i = 0; i < ModelConstants.TOURNAMENT_SELECTIVE_SAMPLE; i++) 
		{
			Order randomParent = population.get(ModelConstants.RANDOM.nextInt(population.size()));
			double randomParentFitness = materialCuttingProblem.calculateFitnessOfOrder(randomParent);

			if(randomParentFitness < fittestParentFitness) 
			{
				fittestParent = randomParent;
				fittestParentFitness = randomParentFitness;
			}
		}

		return fittestParent;
	}
}
