package controller;

import java.util.HashMap;

import model.MCutProblem;
import model.SEARCHMETHOD;
import model.SearchAlgorithm;
import model.search.algorithms.BaselineEvolutionAlgorithm;
import model.search.algorithms.LocalSearch;
import model.search.algorithms.RandomSearch;
import model.Order;

public class Main {

	public static void main(String[] args) 
	{
		/*Material Cutting Problem Main
		float[] stockLengths = { 120, 115, 110, 105, 100 };
		float[] stockCosts = { 12, 11.5f, 11, 10.5f, 10 };
		float[] orderLengths = {21, 22, 24, 25, 27, 29, 30, 31, 32, 33, 34, 35, 38, 39, 42, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 59, 60, 61, 63, 65, 66, 67};
		int[] orderQuantities = {13, 15, 7, 5, 9, 9, 3, 15, 18, 17, 4, 17, 20, 9, 4, 19, 4, 12, 15, 3, 20, 14, 15, 6, 4, 7, 5, 19, 19, 6, 3, 7, 20, 5, 10, 17};
		*/

		/*Material Cutting Problem Example
		float[] stockLengths = { 100, 80, 50 };
		float[] stockCosts = { 250, 175, 100 };
		float[] orderLengths = {20, 30, 25};
		int[] orderQuantities = {5, 5, 7};
		*/

		/*Material Cutting Problem One 
		float[] stockLengths = { 10, 13, 15 };
		float[] stockCosts = { 100, 130, 150 };
		float[] orderLengths = {3, 4, 5, 6, 7, 8, 9, 10};
		int[] orderQuantities = {5, 2, 1, 2, 4, 2, 1, 3};
        */
		
		/*Material Cutting Problem Two*/
		float[] stockLengths = { 4300, 4250, 4150, 3950, 3800, 3700, 3550, 3500 };
		float[] stockCosts = { 86, 85, 83, 79, 68, 66, 64, 63 };
		float[] orderLengths = {2350, 2250, 2200, 2100, 2050, 2000, 1950, 1900, 1850, 1700, 1650, 1350, 1300, 1250, 1200, 1150, 1100, 1050};
		int[] orderQuantities = {2, 4, 4, 15, 6, 11, 6, 15, 13, 5, 2, 9, 3, 6, 10, 4, 8, 3};
		

		MCutProblem materialCuttingProblem = createMaterialCuttingProblem(stockLengths, stockCosts, orderLengths, orderQuantities);

		evaluateMaterialCuttingProblem(materialCuttingProblem, SEARCHMETHOD.RANDOM_SEARCH);		
		evaluateMaterialCuttingProblem(materialCuttingProblem, SEARCHMETHOD.LOCAL_SEARCH);
		evaluateMaterialCuttingProblem(materialCuttingProblem, SEARCHMETHOD.BASELINE_EVOLUTION_SEARCH);
	}

	private static void evaluateMaterialCuttingProblem(MCutProblem materialCuttingProblem, SEARCHMETHOD searchMethod) 
	{
		SearchAlgorithm selectedAlgorithm;			
		System.out.println(searchMethod.toString());

		switch(searchMethod) 
		{
		case RANDOM_SEARCH:
			selectedAlgorithm = new RandomSearch(materialCuttingProblem);
			break;
		case LOCAL_SEARCH:
			selectedAlgorithm = new LocalSearch(materialCuttingProblem);
			break;
		case BASELINE_EVOLUTION_SEARCH:
			selectedAlgorithm = new BaselineEvolutionAlgorithm(materialCuttingProblem);
			break;
		default:
			throw new UnsupportedOperationException("Search method " + searchMethod.toString() + " is not supported.");	
		}

		Order bestOrder = selectedAlgorithm.bestOrder();
			
		//System.out.println("\nBest Order Found: \n" + bestOrder);
		System.out.println("Best Order Fitness: " + materialCuttingProblem.calculateFitnessOfOrder(bestOrder));

		System.out.println("----------------------------------------------------\n");
	}

	/**
	 * Create Material Cutting Problem
	 * @param stockLengths
	 * @param stockCosts
	 * @param orderLengths
	 * @param orderQuantities
	 * @return MaterialCuttingProblem
	 */
	private static MCutProblem createMaterialCuttingProblem(float[] stockLengths, float[] stockCosts, float[] orderLengths, int[] orderQuantities) 
	{		
		HashMap<Float, Float> stockLengthsAndCosts = new HashMap<Float, Float>(stockLengths.length);

		//Map Stock Lengths to their costs respectively
		for(int i = 0; i < stockLengths.length; i++) 
		{				
			stockLengthsAndCosts.put(stockLengths[i], stockCosts[i]);
		}

		HashMap<Float, Integer> orderedLengthsAndQuantities = new HashMap<Float, Integer>(orderLengths.length);

		//Map Ordered Lengths to their ordered qunatities respectively
		for(int i = 0; i < orderLengths.length; i++) 
		{
			orderedLengthsAndQuantities.put(orderLengths[i], orderQuantities[i]);
		}

		return new MCutProblem(stockLengthsAndCosts, orderedLengthsAndQuantities);
	}

}
