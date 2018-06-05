package oop.ex6.syntaxtools;

import oop.ex6.variables.Variable;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Functions that are used throughout the code.
 *
 * @author Alex Goft/Eliav Shames
 */
public class Tools {

    /**
     * Returns the index of the first space in a given line
     *
     * @param line the line to scan
     * @return the index of the first space, -1 if it is not found.
     */
    public static int findFirstSpace(String line) {
        Matcher matcher = fitMatcher(Regex.WHITE_SPACES, line);
        matcher.find();
        try {
            return (matcher.start());
        } catch (IllegalStateException e) {
            return -1;
        }
    }

    /**
     * Finds the first space or bracket inside a line, and returns its index.
     *
     * @param line the line to check.
     * @return the index of the first space or bracket, -1 if it is not found.
     */
    public static int findFirstSpaceOrBracket(String line) {
        Matcher matcher = fitMatcher(Regex.WHITE_SPACES_BRACKET, line);
        matcher.find();
        try {
            return (matcher.start());
        } catch (IllegalStateException e) {
            return -1;
        }
    }


    /**
     * Returns a matcher according to a given string of the pattern and
     * the text to match against.
     *
     * @param patternString the pattern String.
     * @param text          the text to match to pattern to.
     * @return the Matcher that fits the pattern.
     */
    public static Matcher fitMatcher(String patternString, String text) {
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(text);
        return matcher;
    }

    /**
     * Given a value, this Method checks if it is of the same type of
     * the given expected type.
     *
     * @param value           the value to check.
     * @param memberVariables an array of all the member variables.
     * @param localVariables  an array of all the local variables.
     * @param type            the expected type.
     * @return true if the type is as expected, false otherwise.
     */
    public static boolean checkValue(String value, HashMap<String, Variable>
            memberVariables,
                                     HashMap<String, Variable> localVariables,
                                     String type) {

        Matcher matcher = fitMatcher(Variable.getPattern(type), value);
        matcher.find();
        if (matcher.matches()) {
            return true;
        }

        //Check if exists and matches
        if (localVariables != null) {
            for (HashMap.Entry<String, Variable> var :
                    localVariables.entrySet()) {
                if (value.equals(var.getValue().getName())) {
                    if ((type.equals(Regex.BOOLEAN_TYPE)) && (var.getValue()
                            .isInit())) {
                        if (var.getValue().getType().equals(Regex.INT_TYPE) ||
                                var.getValue().getType().equals
                                        (Regex.DOUBLE_TYPE))
                            return true;
                    }
                    if ((var.getValue().getType().equals(type)) &&
                            (var.getValue().isInit()))
                        return true;
                }
            }
        }
        for (HashMap.Entry<String, Variable> var :
                memberVariables.entrySet()) {
            if (value.equals(var.getValue().getName())) {
                if ((type.equals(Regex.BOOLEAN_TYPE) &&
                        var.getValue().isInit())) {
                    if (var.getValue().getType().equals(Regex.INT_TYPE) ||
                            var.getValue().getType().
                                    equals(Regex.DOUBLE_TYPE))
                        return true;
                }
                if (var.getValue().getType().equals(type) &&
                        (var.getValue().isInit()))
                    return true;
            }
        }
        return false;
    }
}