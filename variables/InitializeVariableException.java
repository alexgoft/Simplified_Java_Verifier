package oop.ex6.variables;

public class InitializeVariableException extends VariableException{

	/**
	 * An exception thrown when an uninitialized variable is used.
	 */
	private static final long serialVersionUID = 1L;

	public InitializeVariableException(String s) {
		super(s);
	}
	public InitializeVariableException() {
		super("The variable must be initialized.");
	}
}
