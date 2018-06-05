package oop.ex6.variables;

public class VariableException extends Exception{

	/**
	 * A general exception for variable errors
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * @param s Message
	 */
	public VariableException(String s){
		super(s);
	}

	public VariableException(){
		super("An error occurred while trying to create a Variable");
	}
}
