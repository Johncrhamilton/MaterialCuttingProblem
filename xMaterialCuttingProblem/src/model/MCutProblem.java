package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

public class MCutProblem {

	public final ArrayList<Float> allOrderLengths;
	//Unique lengths as floats and costs as floats
	private final HashMap<Float, Float> STOCK_LENGTHS_AND_COSTS;
	//Unique lengths as floats and quantities as integers
	private final HashMap<Float, Integer> ORDERED_LENGTHS_AND_QUANTITIES;

	private final float shortestOrderedLength;

	public MCutProblem(HashMap<Float, Float> stockLengthsAndCosts, HashMap<Float, Integer> orderedLengthsAndQuantities) 
	{
		//Stock lengths and costs constrained by hashmap
		STOCK_LENGTHS_AND_COSTS = stockLengthsAndCosts;
		//Ordered lengths' and quantities constrained by hashmap
		ORDERED_LENGTHS_AND_QUANTITIES = orderedLengthsAndQuantities;
		
		float tempShortestOrderedLength = Float.MAX_VALUE;
		
		//Create new Arraylist that is the total ORDERED_PIECE_LENGTHS * ORDERED_PIECE_QUANTITIES
		allOrderLengths = new ArrayList<Float>();

		//For each length ordered
		for(Float length : orderedLengthsAndQuantities.keySet()) 
		{
			//Find the shortest ordered length
			if(length < tempShortestOrderedLength) 
			{
				tempShortestOrderedLength = length;
			}

			//Add the required quantities
			for(int j = 0; j < orderedLengthsAndQuantities.get(length); j++) 
			{
				allOrderLengths.add(length);
			}
		}
		
		shortestOrderedLength = tempShortestOrderedLength;
	}

	/**
	 * Generate Random Valid Order
	 * @return Order
	 */
	public Order generateRandomValidOrder() 
	{			
		Order randomValidOrder = new Order(ORDERED_LENGTHS_AND_QUANTITIES);

		randomValidOrder.addAll(generateRandomValidCutActivities((ArrayList<Float>) allOrderLengths.clone()));

		if(randomValidOrder.isComplete()) 
		{
			return randomValidOrder;
		}
		else 
		{
			throw new OrderException("Tried to add order which is not complete." + randomValidOrder.toString());
		}
	}

	/**
	 * Generate Random Valid Cut Activities from a list of ordered lengths
	 * This method will remove lengths from orderedLengths to add to the CutActivities
	 * @param orderedLengths
	 * @return CutActivities that have been randomly generated
	 */
	public ArrayList<CutActivity> generateRandomValidCutActivities(ArrayList<Float> orderedLengths)
	{
		ArrayList<CutActivity> cutActivities = new ArrayList<CutActivity>();
		ArrayList<Float> stockLengths = new ArrayList<Float>(STOCK_LENGTHS_AND_COSTS.keySet());
		
		//Randomly shuffle all the ordered lengths
		Collections.shuffle(orderedLengths);
		
		//While there are still lengths left keep creating Cut Activities
		while(orderedLengths.size() > 0)
		{
			//Add a cut activity with a random stockLength
			cutActivities.add(randomlyCreateCutActivity(stockLengths.get(ModelConstants.RANDOM.nextInt(stockLengths.size())), orderedLengths));
		}
		
		return cutActivities;
	} 
	
	/**
	 * Create CutActivity randomly with orderedLengths
	 * This method will remove lengths from orderedLengths to add to the CutActivity
	 * @return CutActivity
	 */
	public CutActivity randomlyCreateCutActivity(float stockLength, ArrayList<Float> orderedLengths) 
	{
		//Create new Cut Activity of a random stock length
		CutActivity cutActivity = new CutActivity(stockLength);
		
		int randomCutActivityMaxAttempts = ModelConstants.RANDOM_CUT_ACTIVITY_MAX_ATTEMPTS;

		//Populate Cut Activity with lengths from available lengths
		while(cutActivity.getAvailableSpace() > 0 && randomCutActivityMaxAttempts > 0)
		{
			if(orderedLengths.size() <= 0) 
			{
				break;
			}
			
			int randomIndex = ModelConstants.RANDOM.nextInt(orderedLengths.size());
			
			//Add length
			if(cutActivity.add(orderedLengths.get(randomIndex))) 
			{
				orderedLengths.remove(randomIndex);
			}
			else 
			{
				randomCutActivityMaxAttempts--;
			}
		}
		
		return cutActivity;
	}

	/**
	 * Create CutActivity with orderedLengths
	 * This method will remove lengths from orderedLengths to add to the CutActivity
	 * @return CutActivity
	 */
	public CutActivity createCutActivity(float stockLength, ArrayList<Float> orderedLengths) 
	{
		//Create new Cut Activity of a random stock length
		CutActivity cutActivity = new CutActivity(stockLength);

		//Populate Cut Activity with lengths from available lengths
		for(Iterator<Float> iterator = orderedLengths.iterator(); iterator.hasNext();)
		{
			//If not possible to add more lengths
			if(cutActivity.getStockLength() < shortestOrderedLength) 
			{
				break;
			}
			
			//Add length
			if(cutActivity.add(iterator.next())) 
			{
				iterator.remove();
			}
		}
		
		return cutActivity;
	}
	/**
	 * Calculate the cost for a given agent
	 * @param order
	 * @return orderCost
	 */
	public double calculateCostOfOrder(Order order) 
	{
		//Loop through order's cut activities and sum thier costs using stock lengths and stock costs
		double orderCost = 0;

		for(int i = 0; i < order.size(); i++) 
		{
			orderCost += STOCK_LENGTHS_AND_COSTS.get(order.get(i).getStockLength());
		}

		return orderCost;
	}

	/**
	 * Get Ordered Lengths And Quantities
	 * @return OrderedLengthsAndQuantities
	 */
	public HashMap<Float, Integer> getOrderedLengthsAndQuantities() 
	{
		return ORDERED_LENGTHS_AND_QUANTITIES;
	}

	/**
	 * Get Stock Lengths And Costs
	 * @return StockLengthsAndCosts
	 */
	public HashMap<Float, Float> getStockLengthsAndCosts() 
	{
		return STOCK_LENGTHS_AND_COSTS;
	}
}
