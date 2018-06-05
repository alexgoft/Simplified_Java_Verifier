package oop.ex6.parser;

public class BlockSyntaxException extends SyntaxException {

	/**
	 * An exception for if/while block syntax errors
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * @param s Message
	 */
	public BlockSyntaxException(String s) {
		super(s);
	}
	
	public BlockSyntaxException() {
		super("If/While block syntax error!");
	}
}
