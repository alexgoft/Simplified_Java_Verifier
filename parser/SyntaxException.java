package oop.ex6.parser;

public class SyntaxException extends Exception {

	/**
	 * A general syntax exception
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * @param s Message
	 */
	public SyntaxException(String s) {
		super(s);
	}
	
	public SyntaxException() {
		super("Syntax error!");
	}
}
