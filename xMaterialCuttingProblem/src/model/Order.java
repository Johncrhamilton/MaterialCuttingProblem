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
	public boolean add(CutActivity cutActivity) 
	{
		//Don't add if the order has been completed
		if(!isComplete()) 
		{
			for(Float cutLength : cutActivity.getLengths()) 
			{
				//Make sure that adding the cutLength to the Order won't exceed the ordered amounds
				if(currentOrderedLengthsAndQuantities.get(cutLength) >= ORDERED_LENGTHS_AND_QUANTITIES.get(cutLength)) 
				{
					return false; 
				}
			}

			//Update the current piece quantities
			for(Float cutLength : cutActivity.getLengths()) 
			{
				currentOrderedLengthsAndQuantities.put(cutLength, currentOrderedLengthsAndQuantities.get(cutLength) + 1);
			}

			orderCutActivities.add(cutActivity);

			return true;
		}
		return false;
	}

	/**
	 * Try add all cutActivities given 
	 * @param cutActivities
	 * @return boolean, were all the elements added
	 */
	public boolean addAll(ArrayList<CutActivity> cutActivities) 
	{
		for(CutActivity cutActivity : cutActivities) 
		{
			if(!add(cutActivity)) 
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * Remove a cut activity from the order and adjust the current piece quantities
	 * @param cutActivity
	 * @return boolean, was the element removed
	 */
	public boolean remove(CutActivity cutActivity) 
	{
		//Don't remove if the order is empty or the order doesn't have the cut activity
		if(orderCutActivities.size() > 0 && orderCutActivities.contains(cutActivity)) 
		{
			orderCutActivities.remove(cutActivity);

			//Update the current piece quantities
			for(Float cutLength : cutActivity.getLengths()) 
			{
				currentOrderedLengthsAndQuantities.put(cutLength, currentOrderedLengthsAndQuantities.get(cutLength) - 1);
			}
			return true;
		}
		return false;
	}

	/**
	 * Get CutActivity at index
	 * @param index
	 * @return CutActivity
	 */
	public CutActivity get(int index) 
	{
		return orderCutActivities.get(index);
	}

	/**
	 * Get Cut Activities
	 * @return CutActivities
	 */
	public ArrayList<CutActivity> getCutActivities() 
	{
		return orderCutActivities;
	}

	/**
	 * Number of Order Cut Activities
	 * @return size
	 */
	public int size() 
	{
		return orderCutActivities.size();
	}

	/**
	 * Is the Order complete
	 * @return boolean
	 */
	public boolean isComplete() 
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
	 * @return boolean, if same order true else false
	 */
	public boolean equals(Object obj) 
	{
		//Type check
		if(!(obj instanceof Order)) 
		{
			return false;
		}

		//Size check
		if(orderCutActivities.size() != ((Order)obj).getCutActivities().size()) 
		{
			return false;
		}

		//Content check
		for(int i = 0; i < orderCutActivities.size(); i++) 
		{
			if(!orderCutActivities.get(i).equals(((Order)obj).get(i)))
			{
				return false;				
			}
		}

		return true;
	}

	/**
	 * Creates a clone of the Order
	 * @return Order
	 */
	public Order clone() 
	{
		Order copy = new Order(ORDERED_LENGTHS_AND_QUANTITIES);
		ArrayList<CutActivity> orderCutActivities = (ArrayList<CutActivity>) this.orderCutActivities.clone();

		for(int i = 0; i < orderCutActivities.size(); i++) 
		{
			copy.add(orderCutActivities.get(i));
		}

		return copy;
	}
}