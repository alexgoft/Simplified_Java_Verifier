package oop.ex6.scope;

public class MethodException extends Exception{
	/**
	 * A general exception for method errors
	 */
	private static final long serialVersionUID = 1L;

	public MethodException(String s){
		super(s);
	}

	public MethodException(){
		super("Error in method!");
	}
}
