package model;

import java.util.ArrayList;

public class CutActivity {

	private final float STOCK_LENGTH;

	private ArrayList<Float> cutLengths;

	private float availableSpace;

	public CutActivity(float stockLength)
	{		
		if(stockLength > 0) 
		{
			STOCK_LENGTH = stockLength;	
		}
		else 
		{
			throw new IllegalArgumentException("Stock Length " + stockLength + " must be greater than 0");
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
		if(cutLengths.size() > index && index >= 0) 
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

	/**
	 * Get Cut Lengths
	 * @return cutLengths
	 */
	public ArrayList<Float> getCutLengths() 
	{
		return cutLengths;
	}
	
	/**
	 * Get Stock Length
	 * @return StockLength
	 */
	public float getStockLength() 
	{
		return STOCK_LENGTH;
	}

	/**
	 * Get Available Space
	 * @return AvailableSpace
	 */
	public float getAvailableSpace() 
	{
		return availableSpace;
	}
	
	/**
	 * Convert Cut Activity to string
	 * @return string representation of CutActivity
	 */
	public String toString() 
	{
		String string = "[ Stock length " + STOCK_LENGTH + ": ";
		for(Float cutLength : cutLengths) 
		{
			string = string + cutLength + " ";
		}
		string = string + "]";
		
		return string;
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