package model;

import java.util.ArrayList;

public class CutActivity {

	private final float STOCK_LENGTH;

	private ArrayList<Float> lengths;

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
		
		lengths = new ArrayList<Float>();		
		availableSpace = stockLength;
	}
	
	/**
	 * Add Cut Length
	 * @param cutLength
	 * @return boolean, was the element added
	 */
	public boolean add(float cutLength) 
	{
		if(availableSpace >= cutLength && cutLength > 0) 
		{
			lengths.add(cutLength);
			
			availableSpace -= cutLength;
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Remove first Cut Length with length
	 * @param length
	 * @return boolean, was the element removed
	 */
	public boolean remove(float length) 
	{
		if(lengths.size() > 0 && lengths.contains(length)) 
		{
			availableSpace += length;
			lengths.remove(length);
			
			return true;
		}
		return false;
	}
	
	/**
	 * Get Length at index
	 * @param index
	 * @return length
	 */
	public float get(int index) 
	{
		return lengths.get(index);
	}
	
	/**
	 * Get a copy of the lengths in CutActivity
	 * @return ArrayList<Float>
	 */
	public ArrayList<Float> getLengths() 
	{
		return (ArrayList<Float>) lengths.clone();
	}
	
	/**
	 * Number of lengths
	 * @return size
	 */
	public int size() 
	{
		return lengths.size();
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
		for(Float cutLength : lengths) 
		{
			string = string + cutLength + " ";
		}
		string = string + "]";
		
		return string;
	}
	
	/**
	 * @param Object obj
	 * @return boolean, if same cut activity true else false
	 */
	public boolean equals(Object obj) 
	{
		//Type check
		if(!(obj instanceof CutActivity)) 
		{
			return false;
		}
		
		//Size check
		if(size() != ((CutActivity)obj).size()) 
		{
			return false;
		}
		
		//Content check
		for(int i = 0; i < lengths.size(); i++)
		{
			if(lengths.get(i) != ((CutActivity)obj).get(i))
			{
				return false;				
			}
		}
		
		return true;
	}
	
	/**
	 * Creates a clone of the CutActivity
	 * @return CutActivity
	 */
	public CutActivity clone() 
	{
		CutActivity copy = new CutActivity(STOCK_LENGTH);
		ArrayList<Float> lengths = (ArrayList<Float>) this.lengths.clone();

		for(int i = 0; i < lengths.size(); i++) 
		{
			copy.add(lengths.get(i));
		}

		return copy;
	}
}