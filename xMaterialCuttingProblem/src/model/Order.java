package model;

import java.util.ArrayList;

public class Order {

	private final ArrayList<Float> ORDERED_PIECE_LENGTHS;
	private final int[] ORDERED_PIECE_QUANTITIES;

	private ArrayList<CutActivity> orderCutActivities;
	private int[] currentPieceQuantities;

	public Order(ArrayList<Float> requestedPieceLengths, int[] requestedPieceQuantities) 
	{
		if(requestedPieceLengths.size() == requestedPieceQuantities.length && requestedPieceLengths.size() > 0) 
		{
			ORDERED_PIECE_LENGTHS = requestedPieceLengths;
			ORDERED_PIECE_QUANTITIES = requestedPieceQuantities;			
		}
		else
		{
			throw new IllegalArgumentException("The number of requested Piece Lengths " + requestedPieceLengths.size() + 
					" don't match the number of requested Piece Quantities " + requestedPieceQuantities.length + " or one of them is empty.");
		}

		orderCutActivities = new ArrayList<CutActivity>();

		currentPieceQuantities = new int[ORDERED_PIECE_QUANTITIES.length];
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
				//Lookup the cutLength's index in the ordered lengths
				int index = ORDERED_PIECE_LENGTHS.indexOf(cutLength);

				//Make sure that adding the cutLength to the Order won't exceed the ordered amounds
				if(!(currentPieceQuantities[index] < ORDERED_PIECE_QUANTITIES[index])) 
				{
					return false; 
				}
			}

			//Update the current piece quantities
			for(Float cutLength : cutActivity.getCutLengths()) 
			{
				//Lookup the cutLength's index in the ordered lengths
				int index = ORDERED_PIECE_LENGTHS.indexOf(cutLength);
				
				currentPieceQuantities[index] += 1;
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
				//Lookup the cutLength's index in the ordered lengths
				int index = ORDERED_PIECE_LENGTHS.indexOf(cutLength);
				
				currentPieceQuantities[index] -= 1;				
			}
			return true;
		}
		return false;
	}

	public ArrayList<CutActivity> getCutActivities() 
	{
		return (ArrayList<CutActivity>) orderCutActivities.clone();
	}

	public boolean completeOrder() 
	{
		return currentPieceQuantities.equals(ORDERED_PIECE_QUANTITIES);
	}
}