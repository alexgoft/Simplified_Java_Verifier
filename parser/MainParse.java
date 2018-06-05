package oop.ex6.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;

import oop.ex6.scope.Method;
import oop.ex6.scope.MethodException;
import oop.ex6.syntaxtools.Regex;
import oop.ex6.syntaxtools.ReservedWords;
import oop.ex6.syntaxtools.Symbols;
import oop.ex6.syntaxtools.Tools;
import oop.ex6.variables.Variable;
import oop.ex6.variables.VariableException;
import oop.ex6.variables.VariableFactory;

/**
 * This class parses a String ArrayList of lines from the code, creates global
 * Variables and passes method sections to the MethodParser class for further
 * Parsing.
 * @author Alex Goft/Eliav Shames
 */
public class MainParse {

	public static final boolean IS_METHOD_VARIABLE = true;
	private static final String CALL_METHOD_ERROR = 
			" is a call to a non existing Method or the parameters of the" +
					" Method do not match.";
	private static final String SYNTAX_ERROR = "Syntax error in line: ";

	/**
	 * The compile Method gets an array list of strings which contains in each
	 * A line of text in each cell in the original order of the file.
	 * The function will throw an exception if an error is found in the lines
	 * array.
	 * 
	 * @param lines the lines of the text.
	 * 
	 * @throws SyntaxException if found a general error in a line
	 * @throws VariableException if occur any error with line of a variables
	 * @throws MethodException if occur any error with line of a Method
	 */
	public static void compile(ArrayList<String> lines) 
			throws SyntaxException, VariableException, MethodException {
		//run over the lines and check general pattern of the lines
		for(int i=0; i < lines.size() ; i++){
			lines.set(i, lines.get(i).trim());
			Matcher matcher = Tools.fitMatcher
					(Regex.GENERAL_LINE_SYNTAX, lines.get(i));
			if(!matcher.matches()){
				if(lines.get(i).length() > 0)
					throw new SyntaxException(SYNTAX_ERROR + (i+1));
			}
		}

		HashMap<String, Variable> globalVariables = new HashMap<>();
		ArrayList<Method> methods = new ArrayList<>();

		/* Keep the Method calls in the class level in order
		to check their correctness after creating all the methods */
		ArrayList<String> methodCalls = new ArrayList<>();
		//Iterate over all the lines
		for(int i = 0; i< lines.size(); i++){
			// If the line ends with ;
			if(lines.get(i).endsWith(Symbols.SEMICOLON) &&
					!lines.get(i).startsWith(Symbols.DOUBLE_SLASH)) {
				Matcher matcher = Tools.fitMatcher
						(Regex.METHOD_CALL, lines.get(i));
				if(matcher.matches()){
					methodCalls.add(lines.get(i));
					continue;
				}

				ArrayList<Variable> lineVariables = 
						VariableFactory.variableFactory(lines.get(i),
								ReservedWords.GLOBAL_SCOPE, globalVariables,
                                null);
				for(Variable v : lineVariables)
					globalVariables.put(v.getName(), v);
			}
			else {
				//if the line ends with "{"
				if(lines.get(i).endsWith(Symbols.OPEN_BRACE) &&
						!lines.get(i).startsWith(Symbols.DOUBLE_SLASH)) {
					int firstLine = i;
					int lastLine = ParseTools.findCloser(i, lines,
							Symbols.OPEN_BRACE, Symbols.CLOSE_BRACE);
					if(lastLine!=-1) {
						methods.add(new Method(lines.get(i), methods,
								globalVariables, firstLine, lastLine));
						i = lastLine;
					}
					else
						throw new SyntaxException(SYNTAX_ERROR + (i+1));
				}
				else {
					if((!lines.get(i).startsWith(Symbols.DOUBLE_SLASH) &&
							!lines.get(i).equals(Symbols.EMPTY_LINE)) ||
							(!lines.get(i).startsWith(Symbols.DOUBLE_SLASH)
									&& lines.get(i).endsWith
									(Symbols.CLOSE_BRACE)))
						throw new SyntaxException(SYNTAX_ERROR + (i+1));
				}
			}
		}
		//check the Method calls in the class level.
		for(String s : methodCalls) {
			if(!ParseTools.isMethodCall(s, methods, globalVariables, null))
				throw new SyntaxException(s + CALL_METHOD_ERROR);
		}
		MethodParse.parseMethod(lines, methods, globalVariables);
	}
}