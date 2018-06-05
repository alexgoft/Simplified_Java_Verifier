package oop.ex6.parser;

import java.util.ArrayList;
import java.util.HashMap;

import oop.ex6.scope.Method;
import oop.ex6.scope.MethodException;
import oop.ex6.syntaxtools.ReservedWords;
import oop.ex6.syntaxtools.Symbols;
import oop.ex6.variables.Variable;
import oop.ex6.variables.VariableException;
import oop.ex6.variables.VariableFactory;

/**
 * Parses the methods in the code.
 * 
 * @author Alex Goft/Eliav Shames
 *
 */
class MethodParse {
	private static final String SYNTAX_ERROR = "Syntax error in line: ";
	private static final String NO_RETURN_ERROR =
            "The method must end with a return statement.";

	/**
	 * Parses the methods in a given a list.
	 * 
	 * @param lines the lines of the file.
	 * @param methods an array of all the methods in the file.
	 * @param globalVariables a hashset of the global variables.
	 * 
	 * @throws MethodException if any error occurs in a method deceleration
	 * @throws VariableException if any error occurs when
     * creating or assigning a variable.
	 * @throws BlockSyntaxException if there was a syntax error inside an
     * If/While block
     * @throws MethodSyntaxException if there was a syntax error inside a
     * Method
	 */
	public static void parseMethod(ArrayList<String> lines,
                                   ArrayList<Method> methods,
                                   HashMap<String, Variable>
	globalVariables) throws MethodException, VariableException,
            BlockSyntaxException, MethodSyntaxException {
		for(Method singleMethod : methods) {
			HashMap<String, Variable> localVariables= new HashMap<>();
			singleMethod.getMethodVariables().forEach
                    ((s, variable) ->
                            localVariables.put(variable.getName(), variable));

			for(int j= singleMethod.getFirstLine()+1 ; j <
                    singleMethod.getLastLine()-1 ; ) {
				if(lines.get(j).length() == 0){ //if the line is empty
					j++;
					continue;
				}
				//if the line ends with ";"
				if(lines.get(j).endsWith(Symbols.SEMICOLON) && !lines.get(j)
                        .startsWith(Symbols.DOUBLE_SLASH)){
					if (ParseTools.isAReturnLine(lines.get(j))){
						j++;
					}
					else{
						if(ParseTools.isMethodCall(lines.get(j), methods,
                                localVariables, globalVariables)){
							j++;
						}
						else{
							for(Variable v : VariableFactory.variableFactory
                                    (lines.get(j), ReservedWords.LOCAL_SCOPE,
                                            localVariables, globalVariables)){
								localVariables.put(v.getName(), v);
							}
							j++;
						}
					}
				}

				else{
					//if the line ends with "{"
					if(lines.get(j).endsWith(Symbols.OPEN_BRACE) && 
							!lines.get(j).startsWith(Symbols.DOUBLE_SLASH)){
						BlockParse.parseBlock(lines, methods, globalVariables,
                                localVariables, j,
								ParseTools.findCloser(j, lines,
                                        Symbols.OPEN_BRACE,
                                        Symbols.CLOSE_BRACE));
						j = ParseTools.findCloser(j, lines,
                                Symbols.OPEN_BRACE, Symbols.CLOSE_BRACE) + 1;
					}
					else{
						if(lines.get(j).startsWith(Symbols.DOUBLE_SLASH)){
							j++;
						}
						else
							throw new MethodSyntaxException
                                    (SYNTAX_ERROR + (j+1));
					}
				}
			}
			//if there is no return in the end of the Method.
			if(!ParseTools.isAReturnLine(lines.get
                    (singleMethod.getLastLine()-1))){
				throw new MethodException(NO_RETURN_ERROR +
                        singleMethod.getName());
			}
		}
	}
}