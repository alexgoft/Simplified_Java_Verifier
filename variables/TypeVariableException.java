package oop.ex6.variables;

public class TypeVariableException extends VariableException{

	/**
	 * An exception for unsupported variable type errors
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * @param s Message
	 */
	public TypeVariableException(String s){
		super(s);
	}

	public TypeVariableException(){
		super("The type is not supported");
	}
}
