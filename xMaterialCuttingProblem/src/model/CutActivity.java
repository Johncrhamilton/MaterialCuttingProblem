package model;

import java.util.ArrayList;

public class CutActivity {

	private final float STOCK_LENGTH;
	private final float STOCK_COST;

	private ArrayList<Float> cutLengths;

	private float availableSpace;

	public CutActivity(float stockLength, float stockCost)
	{		
		if(stockLength > 0 && stockCost > 0) 
		{
			STOCK_LENGTH = stockLength;
			STOCK_COST = stockCost;			
		}
		else 
		{
			throw new IllegalArgumentException("Stock Length " + stockLength + " and Stock Cost " + stockCost + " must be greater than 0");
		}
		
		cutLengths = new ArrayList<Float>();		
		availableSpace = stockLength;
	}
	
	/**
	 * Add Cut Length
	 * @param cutLength
	 * @return boolean, was the element added
	 */
	public boolean addCutLength(float cutLength) 
	{
		if(availableSpace >= cutLength && cutLength > 0) 
		{
			cutLengths.add(cutLength);
			
			availableSpace -= cutLength;
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Remove Cut Length with index
	 * @param index
	 * @return boolean, was the element removed
	 */
	public boolean removeCutLength(int index) 
	{
		if(cutLengths.size() > index) 
		{
			availableSpace += cutLengths.get(index);
			cutLengths.remove(index);
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Remove first Cut Length with length
	 * @param length
	 * @return boolean, was the element removed
	 */
	public boolean removeCutLength(float length) 
	{
		if(cutLengths.size() > 0 && cutLengths.contains(length)) 
		{
			availableSpace += length;
			cutLengths.remove(length);
			
			return true;
		}
		return false;
	}
	
	/**
	 * The number of a specific Cut Length in Cut Activity
	 * @param length
	 * @return count
	 */
	public int cutLengthQuantity(float length) 
	{
		int count = 0;
		
		if(!cutLengths.contains(length)) 
		{
			return count;
		}
		
		for(int i = 0; i < cutLengths.size(); i++) 
		{
			if(cutLengths.get(i) == length) 
			{
				count++;
			}
		}
		
		return count;
	}

	public ArrayList<Float> getCutLengths() 
	{
		return cutLengths;
	}
	
	public float getStockLength() 
	{
		return STOCK_LENGTH;
	}
	
	public float getStockCost() 
	{
		return STOCK_COST;
	}
	
	/**
	 * @param Object obj
	 * @return boolean, if same route true else false;
	 */
	public boolean equals(Object obj) 
	{
		if(!(obj instanceof CutActivity)) 
		{
			return false;
		}
		
		for(int i = 0; i < cutLengths.size(); i++) 
		{
			if(cutLengths.get(i) != ((CutActivity)obj).getCutLengths().get(i))
			{
				return false;				
			}
		}
		
		return true;
	}
}