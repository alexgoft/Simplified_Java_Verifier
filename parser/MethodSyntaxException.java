package oop.ex6.parser;

public class MethodSyntaxException extends SyntaxException{

	/**
	 * An exception thrown when a syntax error is discovered in a method,
	 * Not inside an if/while block.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * @param s Message
	 */
	public MethodSyntaxException(String s) {
		super(s);
	}
	
	public MethodSyntaxException() {
		super("Method syntax error!");
	}
}
