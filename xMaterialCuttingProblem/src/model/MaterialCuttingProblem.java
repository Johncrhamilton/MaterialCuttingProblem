package model;

import java.util.ArrayList;
import java.util.HashMap;

public class MaterialCuttingProblem {
	
	//Unique lengths as floats and costs as floats
	private final HashMap<Float, Float> STOCK_LENGTHS_AND_COSTS;	
	//Unique lengths as floats and quantities as integers
	private final HashMap<Float, Integer> ORDERED_LENGTHS_AND_QUANTITIES;
	
	private ArrayList<Float> allOrderLengths;
	private ArrayList<Float> stockLengths;
	private float longestOrderedLength;

	public MaterialCuttingProblem(HashMap<Float, Float> stockLengthsAndCosts, HashMap<Float, Integer> orderedLengthsAndQuantities) 
	{
		//Stock lengths and costs constrained by hashmap
		STOCK_LENGTHS_AND_COSTS = stockLengthsAndCosts;
		//Ordered lengths' and quantities constrained by hashmap
		ORDERED_LENGTHS_AND_QUANTITIES = orderedLengthsAndQuantities;

		stockLengths = new ArrayList<Float>(stockLengthsAndCosts.keySet());
		longestOrderedLength = Float.MIN_VALUE;
		
		//Create new Arraylist that is the total ORDERED_PIECE_LENGTHS * ORDERED_PIECE_QUANTITIES
		allOrderLengths = new ArrayList<Float>();
		
		//For each length ordered
		for(Float length : orderedLengthsAndQuantities.keySet()) 
		{
			//Find the shortest ordered length
			if(longestOrderedLength < length) 
			{
				longestOrderedLength = length;
			}
			
			//Add the required quantities
			for(int j = 0; j < orderedLengthsAndQuantities.get(length); j++) 
			{
				allOrderLengths.add(length);
			}
		}
	}
	
	/**
	 * Generate Random Valid Order
	 * @return Order
	 */
	public Order generateRandomValidOrder() 
	{			
		Order randomValidOrder = new Order(ORDERED_LENGTHS_AND_QUANTITIES);
		
		ArrayList<Float> tempAllOrderLengths = (ArrayList<Float>) allOrderLengths.clone();
		
		//While there are still ordered lengths left keep creating Cut Activities
		while(tempAllOrderLengths.size() != 0) 
		{
			//Create new Cut Activity of a random stock length
			CutActivity randomValidCutActivity = new CutActivity(stockLengths.get(ModelConstants.random.nextInt(stockLengths.size())));
			
			//Populate Cut Activity with random lengths from all available lengths in order
			while(tempAllOrderLengths.size() > 0 && randomValidCutActivity.getAvailableSpace() >= longestOrderedLength) 
			{
				int randomIndex = ModelConstants.random.nextInt(tempAllOrderLengths.size());
				
				//Add random length
				if(randomValidCutActivity.addCutLength(tempAllOrderLengths.get(randomIndex))) 
				{
					//Remove random length 
					tempAllOrderLengths.remove(randomIndex);					
				}
			}
			
			randomValidOrder.addCutActivity(randomValidCutActivity);
		}
		
		return randomValidOrder;
	}
	
	/**
	 * Calculate the cost for a given agent
	 * @param order
	 * @return cost
	 */
	public double calculateCostOfOrder(Order order) 
	{
		//Loop through order's cut activities and sum thier costs using stock lengths and stock costs
		double orderCost = 0;
		
		for(CutActivity cutActivity : order.getCutActivities()) 
		{
			orderCost += STOCK_LENGTHS_AND_COSTS.get(cutActivity.getStockLength());
		}
		
		return orderCost;
	}
}
