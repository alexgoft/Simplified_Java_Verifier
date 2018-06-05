package oop.ex6.scope;

public class WrongMethodDeclarationException extends MethodException {

	/**
	 * An exception for an illegal method declaration
	 */
	private static final long serialVersionUID = 1L;
	
	public WrongMethodDeclarationException(String s){
		super(s);
	}

	public WrongMethodDeclarationException(){
		super("Illegal method declaration syntax!");
	}
}
