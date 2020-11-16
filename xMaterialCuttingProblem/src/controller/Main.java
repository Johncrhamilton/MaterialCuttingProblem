package controller;

import java.util.HashMap;

import model.MaterialCuttingProblem;
import model.SEARCHMETHOD;
import model.SearchAlgorithm;
import model.search.algorithms.RandomSearch;
import model.Order;

public class Main {

	public static void main(String[] args) 
	{
		//Material Cutting Problem One
		float[] stockLengths = { 100, 80, 50 };
		float[] stockCosts = { 250, 175, 100 };
		float[] orderLengths = {20, 30, 25};
		int[] orderQuantities = {5, 5, 7};

		MaterialCuttingProblem materialCuttingProblemOne = createMaterialCuttingProblem(stockLengths, stockCosts, orderLengths, orderQuantities);
		evaluateMaterialCuttingProblem(materialCuttingProblemOne, SEARCHMETHOD.RANDOM_SEARCH);
	}

	private static void evaluateMaterialCuttingProblem(MaterialCuttingProblem materialCuttingProblem, SEARCHMETHOD searchMethod) 
	{
		SearchAlgorithm selectedAlgorithm;			
		System.out.println(searchMethod.toString());

		switch(searchMethod) 
		{
		case RANDOM_SEARCH:
			selectedAlgorithm = new RandomSearch(materialCuttingProblem);
			break;
		case LOCAL_SEARCH:
			selectedAlgorithm = null;
			break;
		case BASE_EVOLUTION_SEARCH:
			selectedAlgorithm = null;
			break;
		case EVOLUTION_SEARCH:
			selectedAlgorithm = null;
			break;
		default:
			throw new UnsupportedOperationException("Search method " + searchMethod.toString() + " is not supported.");	
		}

		Order bestOrder = selectedAlgorithm.bestOrder();
		System.out.println("Best Order Found: \n");
		System.out.println(bestOrder);
		System.out.println("Best Order Cost: " + materialCuttingProblem.calculateCostOfOrder(bestOrder));
		
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
	private static MaterialCuttingProblem createMaterialCuttingProblem(float[] stockLengths, float[] stockCosts, float[] orderLengths, int[] orderQuantities) 
	{		
		HashMap<Float, Float> stockLengthsAndCosts = new HashMap<Float, Float>(3);
		for(int i = 0; i < stockLengths.length; i++) 
		{
			stockLengthsAndCosts.put(stockLengths[i], stockCosts[i]);
		}

		HashMap<Float, Integer> orderedLengthsAndQuantities = new HashMap<Float, Integer>(3);
		for(int i = 0; i < orderLengths.length; i++) 
		{
			orderedLengthsAndQuantities.put(orderLengths[i], orderQuantities[i]);
		}

		return new MaterialCuttingProblem(stockLengthsAndCosts, orderedLengthsAndQuantities);
	}

}
