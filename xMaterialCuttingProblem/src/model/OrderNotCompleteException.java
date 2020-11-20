package model;

public class OrderNotCompleteException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public OrderNotCompleteException(String message) 
	{
		super(message);
	}
}
