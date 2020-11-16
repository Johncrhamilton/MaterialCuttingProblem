package controller;

import java.util.HashMap;

import model.MaterialCuttingProblem;
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
		
		for(int i = 0; i < 10; i++) 
		{
			Order o1 = materialCuttingProblemOne.generateRandomValidOrder();
			System.out.println(o1.toString());
			System.out.println(materialCuttingProblemOne.calculateCostOfOrder(o1));
		}
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
