package model;

import java.util.Random;

public class MaterialCuttingProblem {

	private final float[] STOCK_LENGTHS;
	private final float[] STOCK_COSTS;
	private final float[] ORDERED_PIECE_LENGTHS;
	private final float[] ORDERED_PIECE_QUANTITIES;
	
	private Random random;

	public MaterialCuttingProblem(float[] stockLengths, float[] stockCosts, float[] orderedPieceLengths, float[] orderedPieceQuantities) 
	{
		//Stock constraints
		if(stockLengths.length == stockCosts.length && stockLengths.length > 0) 
		{
			STOCK_LENGTHS = stockLengths;
			STOCK_COSTS = stockCosts;
		}
		else 
		{
			throw new IllegalArgumentException("The number of Stock Lengths " + stockLengths.length + 
					" don't match the number of Stock Costs " + stockCosts.length + " or one of them is empty.");
		}

		//ordered length constraints
        if(orderedPieceLengths.length == orderedPieceQuantities.length && orderedPieceLengths.length > 0) 
		{
			ORDERED_PIECE_LENGTHS = orderedPieceLengths;
			ORDERED_PIECE_QUANTITIES = orderedPieceQuantities;			
		}
		else
		{
			throw new IllegalArgumentException("The number of ordered Piece Lengths " + orderedPieceLengths.length + 
					" don't match the number of ordered Piece Quantities " + orderedPieceQuantities.length + " or one of them is empty.");
		}
		
		random = new Random();
	}
	
	public Order generateRandomValidOrder() 
	{
		//Create enough random cut activites for one order
		//Pick random stock lengths for cut activities
		//Create new Arraylist that is ORDERED_PIECE_LENGTHS * ORDERED_PIECE_QUANTITIES
		//Pick one at random, put it in arraylist and then remove.
		return null;
	}
	
	public double calculateCostOfOrder(Order order) 
	{
		//Loop through order's cut activities and sum thier costs using stock lengths and stock costs
		return 0.0;
	}

}
