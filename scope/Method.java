package oop.ex6.scope;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;

import oop.ex6.parser.MainParse;
import oop.ex6.syntaxtools.Regex;
import oop.ex6.syntaxtools.ReservedWords;
import oop.ex6.syntaxtools.Symbols;
import oop.ex6.syntaxtools.Tools;
import oop.ex6.variables.Variable;
import oop.ex6.variables.VariableException;
import oop.ex6.variables.VariableFactory;

/**
 * Represents a single Method, containing:
 * 1. The name of the Method.
 * 2. The Method variables.
 * 
 * @author Alex Goft/Eliav Shames
 */
public class Method {
	private static final String TYPE_ERROR = " type is not supported in" +
            " sJava. Only void methods are permitted.";
	private static final String ILLEGAL_METHOD_NAME_ERROR = "Illegal" +
            " method name.";
	private static final String SAME_METHOD_ERROR = "Multiple methods" +
            " with the same name.";
	private static final String METHOD_HEADLINE_ERROR = "Illegal method" +
			"declaration.";
	private static final String METHOD_TYPE_VOID = "void";

	private String name;
	private HashMap<String, Variable> methodVariables = new HashMap<>();
	private HashMap<String, Variable> globalVariables;
	private int firstLine;
	private int lastLine;

	/**
	 * Constructs a Method with the given parameters.
	 * 
	 * @param line the headline of the Method.
	 * @param methods the list of the variables that the Method receives.
	 * @param globalVariables
	 * @param firstLine the line in the file where the Method begins.
	 * @param lastLine the line in the file where the Method ends.
	 * 
	 * @throws VariableException if there was a variables related exception.
	 * @throws MethodException if there was a problem creating the
     * Method (e.g illegal name).
	 */
	public Method(String line, ArrayList<Method> methods,
				  HashMap<String, Variable> globalVariables,
				  int firstLine, int lastLine)
			throws VariableException, MethodException {
		this.firstLine = firstLine;
		this.lastLine = lastLine;
		this.globalVariables = globalVariables;
		line = checkVoidType(line);
		line = checkAndUpdateName(line, methods);
		line = checkSyntax(line);
		checkAndUpdateParameters(line);
	}

	/**
	 * @return the name of the Method.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return an ArrayList of the Method variables.
	 */
	public HashMap<String, Variable> getMethodVariables() {
		return methodVariables;
	}

	/**
	 * @return the number of first line of the Method.
	 */
	public int getFirstLine() {
		return firstLine;
	}

	/**
	 * @return the number of last line of the Method.
	 */
	public int getLastLine() {
		return lastLine;
	}

	//Checks if the Method declaration is valid.
	private String checkSyntax(String line) throws
            WrongMethodDeclarationException {
		line = line.trim();
		line = line.substring(0, line.length()-1);
		line = line.trim();
		if(!line.startsWith(Symbols.OPEN_ROUND_BRACKET) ||
                !line.endsWith(Symbols.CLOSE_ROUND_BRACKET))
			throw new WrongMethodDeclarationException(METHOD_HEADLINE_ERROR);
		return line.substring(1, line.length()-1);
	}

	//Add all the parameters that the Method receives to the local variables
    //hashset
	private void checkAndUpdateParameters(String line)
            throws VariableException {
		String[] parts = line.split(Symbols.COMMA);
		for(String x : parts) {
			if(!x.equals(Symbols.EMPTY_LINE)) {
				ArrayList<Variable> variables =
                        VariableFactory.variableFactory(x + Symbols.SEMICOLON,
                                ReservedWords.METHOD_SCOPE,
                                methodVariables, null);
				for(int i = 0 ; i < variables.size() ; i++){
					//variables.get(i).setInit(true);
					methodVariables.put(variables.get(i).getName(),
                            variables.get(i));
				}
			}
		}
	}

	// Validates the name of the Method.
	private String checkAndUpdateName(String line,
                                      ArrayList<Method> methods)
			throws WrongMethodDeclarationException {
		line = line.trim();
		String beginning = line.substring(0,
                Tools.findFirstSpaceOrBracket(line));
		Matcher matcher = Tools.fitMatcher(Regex.METHOD_NAME_FORMAT,
                beginning);
		matcher.find();
		if(matcher.matches()) {
			for(Method i : methods) {
				if(beginning.equals(i.getName()))
					throw new WrongMethodDeclarationException
                            (SAME_METHOD_ERROR);
			}
			this.name = beginning;
		}
		else
			throw new WrongMethodDeclarationException
                    (ILLEGAL_METHOD_NAME_ERROR);
		return line.substring(Tools.findFirstSpaceOrBracket(line));
	}

	//Checks if the type of the Method is "void".
	private String checkVoidType(String line)
            throws WrongMethodDeclarationException {
		line = line.trim();
		String beginning = line.substring(0, Tools.findFirstSpace(line));
		if(!beginning.equals(METHOD_TYPE_VOID))
			throw new WrongMethodDeclarationException(beginning + TYPE_ERROR);
		return line.substring(Tools.findFirstSpace(line));
	}
}