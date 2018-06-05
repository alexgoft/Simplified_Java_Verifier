package oop.ex6.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;

import oop.ex6.scope.Method;
import oop.ex6.syntaxtools.Regex;
import oop.ex6.syntaxtools.ReservedWords;
import oop.ex6.syntaxtools.Symbols;
import oop.ex6.syntaxtools.Tools;
import oop.ex6.variables.Variable;
import oop.ex6.variables.VariableException;
import oop.ex6.variables.VariableFactory;

/**
 * Parses a block (if condition or while loop).
 * 
 * @author Alex Goft/Eliav Shames
 *
 */
public class BlockParse {
	private static final String CONDITION_ERROR =
            "A condition in a block must be boolean.";
	private static final String BLOCK_DECLARATION_ERROR =
            "Wrong block declaration";
	private static final String SYNTAX_ERROR = "Syntax error in line: ";

	private static final String WHILE_BLOCK = "while";
	private static final String IF_BLOCK = "if";

	/**
	 * Parses a single If/While block.
	 * @param lines the lines of the file.
	 * @param methods a hashset of all the methods in the file.
	 * @param memberVariables a hashset of all the member variables.
	 * @param localVariables a hashset of all the local variables.
	 * @param firstLine the line in the file where the block begins
	 * @param lastLine the line in the file where the block ends.
	 * 
	 * @throws VariableException if an error occured while creating
     * or assigning a variable.
	 * @throws BlockSyntaxException if there was a syntax error
     * inside the block.
	 */
	public static void parseBlock(ArrayList<String> lines,
                                  ArrayList<Method> methods,
                                  HashMap<String, Variable> memberVariables,
                                  HashMap<String, Variable> localVariables,
                                  int firstLine, int lastLine)
            throws VariableException, BlockSyntaxException {

		checkBlockHeadline(lines.get(firstLine), memberVariables,
                localVariables);

		HashMap<String, Variable> blockVariables= new HashMap<>();
        localVariables.forEach
                ((s, variable) ->
                        blockVariables.put(variable.getName(), variable));

		// Iterate over each line in the file
		for(int j= firstLine +1 ; j < lastLine ; ) {
			if(lines.get(j).length() == 0){ //if the line is empty
				j++;
				continue;
			}
			//if the line ends with ;
			if(lines.get(j).endsWith(Symbols.SEMICOLON) &&
                    !lines.get(j).startsWith(Symbols.DOUBLE_SLASH)){
				if (ParseTools.isAReturnLine(lines.get(j))){
					j++;
					continue;
				}
				else{
					if(ParseTools.isMethodCall(lines.get(j),
                            methods,memberVariables,blockVariables)){
						j++;
						continue;
					}
					else{
						for(Variable v : VariableFactory.variableFactory
                                (lines.get(j), ReservedWords.LOCAL_SCOPE,
								blockVariables, memberVariables)){
							blockVariables.put(v.getName(), v);
						}
						j++;
					}
				}
			}
			else{
				//if the line ends with {
				if(lines.get(j).endsWith(Symbols.OPEN_BRACE) &&
                        !lines.get(j).startsWith(Symbols.DOUBLE_SLASH)){
					parseBlock(lines, methods, memberVariables,
                            blockVariables, j, ParseTools.findCloser
							(j, lines, Symbols.OPEN_BRACE,
                                    Symbols.CLOSE_BRACE));
					j = ParseTools.findCloser(j, lines, Symbols.OPEN_BRACE,
                            Symbols.CLOSE_BRACE) + 1;
				}
				else{
					if(lines.get(j).startsWith(Symbols.DOUBLE_SLASH)){
						j++;
						continue;
					}
					else
						throw new BlockSyntaxException(SYNTAX_ERROR +(j+1));
				}
			}
		}
	}

    /*
     * Checks if the block's expression is valid.
     * @param line
     * @param memberVariables
     * @param localVariables
     * @throws BlockSyntaxException
     */
	private static void checkBlockHeadline(String line, HashMap<String,
            Variable> memberVariables,
			HashMap<String, Variable> localVariables)
            throws BlockSyntaxException {
		int index = Tools.findFirstSpaceOrBracket(line);
		if(index!=-1) {
		//if the block declaration doesn't contain only "if" or "while"
			String beginning = line.substring(0, index);
			line = line.substring(index);
			line = line.trim();
			//if the block is not if or while
			switch (beginning) {
			case IF_BLOCK:
				checkCondition(line, memberVariables, localVariables);
				break;
			case WHILE_BLOCK:
				checkCondition(line, memberVariables, localVariables);
				break;
			default:
				throw new BlockSyntaxException(BLOCK_DECLARATION_ERROR);
			}
		}
		else
			throw new BlockSyntaxException(BLOCK_DECLARATION_ERROR);
	}

    /*
     * Validates the condition in the block, given the hashsets of global
     * And local variables, as well as a String boolean expression.
     * @param line
     * @param globalVariables
     * @param localVariables
     * @throws BlockSyntaxException
     */
	private static void checkCondition(String line ,HashMap<String, Variable>
            globalVariables, HashMap<String, Variable> localVariables)
            throws BlockSyntaxException {
		line = line.substring(0, line.length() - 1);
		line = line.trim();
		Matcher matcher = Tools.fitMatcher(Regex.SURROUNDING_BRACKETS, line);
		matcher.find();
		if(matcher.matches()) { // if it contains a condition
			line = line.substring(1, line.length()-1);
			String[] parts = line.split(Regex.AND_OR);
			for(int i = 0 ; i < parts.length ; i++){
				parts[i]= parts[i].trim();
				if(!Tools.checkValue(parts[i], globalVariables,
                        localVariables, Regex.BOOLEAN_TYPE))
					throw new BlockSyntaxException(CONDITION_ERROR);
			}
		}
		else
			throw new BlockSyntaxException(BLOCK_DECLARATION_ERROR);
	}
}