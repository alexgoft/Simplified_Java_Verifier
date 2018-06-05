package oop.ex6.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;

import oop.ex6.scope.Method;
import oop.ex6.syntaxtools.Regex;
import oop.ex6.syntaxtools.Symbols;
import oop.ex6.syntaxtools.Tools;
import oop.ex6.variables.Variable;

/**
 * Tools for the parsing process.
 * 
 * @author Alex Goft/Eliav Shames
 */
public class ParseTools {
	
	/**
	 * Checks if the given line is a legal Method call.
	 * 
	 * @param line the line where we expect to be a method call.
	 * @param methods an array of all the methods in the file.
	 * @param globalVariables a hashset of all the member variables.
	 * @param localVariables a hashset of all the local variables.
	 * @return true iff the line is a legal method call.
	 */
	public static boolean isMethodCall(String line, ArrayList<Method> methods,
                                       HashMap<String, Variable>
                                               globalVariables
			, HashMap<String, Variable> localVariables) {
		int index = Tools.findFirstSpaceOrBracket(line);
		String name = line.substring(0, index);
		line = line.substring(index,line.length()-1);
		line = line.trim();
		
		for(Method m : methods) {
			if(m.getName().equals(name)) {
				HashMap<String, Variable> variables = m.getMethodVariables();
				Matcher matcher = Tools.fitMatcher
                        (Regex.SURROUNDING_BRACKETS, line);
				matcher.find();
				if(matcher.matches()) {
					line = line.substring(1, line.length()-1);
					return checkVariables(line, variables,
                            globalVariables, localVariables);
				}
			}
		}
	
		return false;
	}

    /**
     * Given the parameters inside the Method call, this function checks if
     * They are consistent with the expected method variables.
     * @param line The line of code to check
     * @param methodVariables The method variables hashmap
     * @param globalVariables The global variables hashmap
     * @param localVariables The non-parameter local variables
     * @return True iff the method variables are used as specified by the
	 * Parameters.
     */
	private static boolean checkVariables(String line, HashMap
            <String, Variable> methodVariables, HashMap<String, Variable>
	globalVariables, HashMap<String, Variable> localVariables) {
		String[] parts = line.split(Symbols.COMMA);
		if(line.equals(Symbols.EMPTY_LINE) && methodVariables.size() == 0)
			return true;
		if(parts.length == methodVariables.size()) {
			int i = 0;
			for(HashMap.Entry<String, Variable> method: methodVariables
                    .entrySet()) {
				parts[i] = parts[i].trim();
				if(!Tools.checkValue(parts[i], globalVariables,
                        localVariables, method.getValue().getType()))
					return false;
				i++;
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Given an opening bracket, the function returns the line number of
	 * The appropriate closing bracket.
	 * @param beginning the number of the opening bracket.
	 * @param lines an array of the lines of the code.
	 * @param open the String of the type of the opening bracket.
	 * @param close the String of the type of the closing bracket.
	 * 
	 * @return the line number of the closing bracket.
	 */
	public static int findCloser(int beginning, ArrayList<String> lines,
                                 String open, String close) {
		int counter = 0;
		for(int i = beginning; i<lines.size();i++) {
			if(lines.get(i).endsWith(open))
				counter++;
			if(lines.get(i).endsWith(close))
				counter--;
			if(counter==0)
				return i;
		}
		return -1;
	}

	/**
	 * Checks if a given line is a return line.
	 * 
	 * @param line the line to check.
	 * @return true iff the line is a return line
	 */
	public static boolean isAReturnLine(String line){
		Matcher matcher = Tools.fitMatcher(Regex.RETURN_LINE, line);
		matcher.find();
		return (matcher.matches());
	}
}
