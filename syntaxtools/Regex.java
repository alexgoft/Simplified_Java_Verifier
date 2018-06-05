package oop.ex6.syntaxtools;

/**
 * This class contains all the regular expressions used in the program.
 *
 * @author Alex Goft/Eliav Shames
 */
public class Regex {

    public static final String SURROUNDING_BRACKETS = "\\(.*\\)";
    public static final String AND_OR = "&&|\\|\\|";
    public static final String METHOD_CALL = "^[a-zA-Z]+[a-zA-Z0-9_]*\\s*\\" +
            "(.*\\)\\s*;";
    public static final String GENERAL_LINE_SYNTAX = "(^(//)).*|(^(\\})$)|" +
            ".*((\\{)$)|.*((;)$)";
    public static final String RETURN_LINE = "(return)\\s*;";
    public static final String WHITE_SPACES_BRACKET = "\\s|\\(";
    public static final String WHITE_SPACES = "\\s";
    public static final String VARIABLE_DECLARATION = "[ \\t]*(\\bfinal\\b|" +
            "[ \\t]*)[ \\t]*(\\bint\\b|\\bdouble\\b|\\bchar\\b|\\bString" +
            "\\b|\\bboolean\\b|[ \\t]*)[ \\t]*(.+)[ \\t]*;[ \\t]*";

    public static final String SEPERATE_NAME_AND_EQUALS = "[ \\t]*" +
            "(\\b[^\\s\\t=]+\\b)[ \\t]*|[ \\t]*(\\b[^\\s\\t=]+\\b)" +
            "[ \\t]*=[ \\t]*(.+)";


    //Types of variables.
    public static final String INT_TYPE = "int";
    public static final String DOUBLE_TYPE = "double";
    public static final String CHAR_TYPE = "char";
    public static final String STRING_TYPE = "String";
    public static final String BOOLEAN_TYPE = "boolean";

    //Formats of names and variable types.
    public static final String VARIABLE_NAME_FORMAT = "(_+[a-zA-Z0-9]+)|" +
            "([a-zA-Z]+[\\w]*)";
    public static final String METHOD_NAME_FORMAT = "([a-zA-Z])+[0-9_]*" +
            "[A-Za-z0-9_]*";
    public static final String INT_FORMAT = "-*[0-9]+";
    public static final String DOUBLE_FORMAT = "(-*[0-9]+(\\.?[0-9]+)?)";
    public static final String CHAR_FORMAT = "(\\').(\\')";
    public static final String STRING_FORMAT = "(\").*(\")";
    public static final String BOOLEAN_FORMAT = "(-*[0-9]+(\\.?[0-9]+)?)|" +
            "(true)|(false)";
}
