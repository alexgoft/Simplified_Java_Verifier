package oop.ex6.variables;

public class BadNameVariableException extends VariableException{

	/**
	 * Thrown when an invalid variable name is detected
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * @param s Message
	 */
	public BadNameVariableException(String s) {
		super(s);
	}
	public BadNameVariableException() {
		super("Illegal variable name");
	}
}
