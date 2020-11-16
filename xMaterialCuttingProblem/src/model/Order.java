package model;

import java.util.ArrayList;
import java.util.HashMap;

public class Order {	

	//Unique lengths as floats and quantities as integers
	private final HashMap<Float, Integer> ORDERED_LENGTHS_AND_QUANTITIES;	
	private HashMap<Float, Integer> currentOrderedLengthsAndQuantities;
	
	private ArrayList<CutActivity> orderCutActivities;

	public Order(HashMap<Float, Integer> orderedLengthsAndQuantities) 
	{
		ORDERED_LENGTHS_AND_QUANTITIES = orderedLengthsAndQuantities;
		orderCutActivities = new ArrayList<CutActivity>();

		currentOrderedLengthsAndQuantities = new HashMap<Float, Integer>();		
		for(Float length : ORDERED_LENGTHS_AND_QUANTITIES.keySet()) 
		{
			currentOrderedLengthsAndQuantities.put(length, 0);
		}
	}

	/**
	 * Add a cut activity to the order
	 * @param cutActivity
	 * @return boolean, was the element added
	 */
	public boolean addCutActivity(CutActivity cutActivity) 
	{
		//Don't add if the order has been completed
		if(!completeOrder()) 
		{
			for(Float cutLength : cutActivity.getCutLengths()) 
			{
				//Make sure that adding the cutLength to the Order won't exceed the ordered amounds
				if(currentOrderedLengthsAndQuantities.get(cutLength) >= ORDERED_LENGTHS_AND_QUANTITIES.get(cutLength)) 
				{
					return false; 
				}
			}

			//Update the current piece quantities
			for(Float cutLength : cutActivity.getCutLengths()) 
			{
				currentOrderedLengthsAndQuantities.put(cutLength, currentOrderedLengthsAndQuantities.get(cutLength) + 1);
			}

			orderCutActivities.add(cutActivity);

			return true;
		}
		return false;
	}

	/**
	 * Remove a cut activity from the order and adjust the current piece quantities
	 * @param cutActivity
	 * @return boolean, was the element removed
	 */
	public boolean removeCutActivity(CutActivity cutActivity) 
	{
		//Don't remove if the order is empty or the order doesn't have the cut activity
		if(orderCutActivities.size() > 0 && orderCutActivities.contains(cutActivity)) 
		{
			orderCutActivities.remove(cutActivity);

			//Update the current piece quantities
			for(Float cutLength : cutActivity.getCutLengths()) 
			{
				currentOrderedLengthsAndQuantities.put(cutLength, currentOrderedLengthsAndQuantities.get(cutLength) - 1);
			}
			return true;
		}
		return false;
	}

	/**
	 * Get Cut Activities
	 * @return CutActivities
	 */
	public ArrayList<CutActivity> getCutActivities() 
	{
		return (ArrayList<CutActivity>) orderCutActivities.clone();
	}

	/**
	 * Is the Order complete
	 * @return boolean
	 */
	public boolean completeOrder() 
	{		
		return currentOrderedLengthsAndQuantities.entrySet().equals(ORDERED_LENGTHS_AND_QUANTITIES.entrySet());
	}
	
	/**
	 * Convert Order to string
	 * @return string representation of Order
	 */
	public String toString() 
	{
		String string = "Order: \n";
		for(CutActivity cutActivity : orderCutActivities) 
		{
			string = string + cutActivity.toString() + "\n";
		}
		
		return string;
	}
	
	/**
	 * @param Object obj
	 * @return boolean, if same route true else false;
	 */
	public boolean equals(Object obj) 
	{
		if(!(obj instanceof Order)) 
		{
			return false;
		}
		
		for(int i = 0; i < orderCutActivities.size(); i++) 
		{
			if(!orderCutActivities.get(i).equals(((Order)obj).getCutActivities().get(i)))
			{
				return false;				
			}
		}
		
		return true;
	}
}