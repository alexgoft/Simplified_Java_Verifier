package oop.ex6.variables;

import oop.ex6.syntaxtools.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The variable factory class. Receives a line of code declaring variables
 * And returns an ArrayList of Variable objects.
 *
 * @author Alex Goft/Eliav Shames
 */
public class VariableFactory {


    private static final String UNINITIALIZED_VARIABLE_ERROR = "You can't " +
            "initialize a variable with another" +
            " uninitialized variable!";

    private static final String VARIABLE_SYNTAX_ERROR = "Invalid variable" +
            " declaration syntax!";

    private static final String VARIABLE_FORMAT_ERROR = "The value " +
            " is not formatted properly!";

    private static final String VARIABLE_SCOPE_ERROR = "A variable with" +
            " this name already exists in this scope!";

    private static final String VARIABLE_NONEXISTENT_ERROR = "The variable" +
            " assigned does not exist in the current scope or" +
            " the global variables!";

    //Separates the variable type from the rest of the declaration.
    private static Pattern pVariableDeclaration = Pattern.compile
            (Regex.VARIABLE_DECLARATION);
    //Separates the variable name and value into separate groups.
    private static Pattern pSeparateNameAndEquals = Pattern.compile
            (Regex.SEPERATE_NAME_AND_EQUALS);

    private static Pattern pValidName =
            Pattern.compile(Regex.VARIABLE_NAME_FORMAT);

    private static Pattern pBooleanFormat =
            Pattern.compile(Regex.BOOLEAN_FORMAT);


    /**
     * Parses a given variable declaration line, breaks it down into type,
     * Name, value and final/not final, after which the parameters are sent
     * to a helper function to create the given variables in the line.
     * All variables are added to an ArrayList which is returned when all
     * Variables have been created
     * @param line A String variable declaration line
     * @param varScope The scope of the variable
     * @param varSet A given variables HashMap
     * @param globalVarSet The global variables HashMap
     * @return An Variable ArrayList
     * @throws VariableException if any error occurs when creating or
     * Assigning the variable.
     */
    public static ArrayList<Variable> variableFactory
    (String line, int varScope, HashMap<String,Variable> varSet,
     HashMap<String, Variable> globalVarSet)
            throws VariableException{
        boolean isFinal = false;
        String varType;
        ArrayList<Variable> vars = new ArrayList<>();
        Matcher mVariableDeclaration = pVariableDeclaration.matcher(line);
        if(mVariableDeclaration.matches()) {
            //Check to see if the variable was declared final
            if (mVariableDeclaration.group(1).equals(ReservedWords.FINAL))
                isFinal = true;
            varType = mVariableDeclaration.group(2);//varType
            //varsAndValues
            String[] nameAndType = mVariableDeclaration.group(3).split
                    (Symbols.COMMA);
            checkNameAndType(vars, isFinal, varType, nameAndType,
                    varScope, varSet, globalVarSet);
            return vars;
        }
            throw new TypeVariableException();
    }


    private static void checkNameAndType(ArrayList<Variable> vars,
                                         boolean isFinal, String varType,
                                         String[] nameAndType,
                                         int varScope,
                                         HashMap<String, Variable> varSet,
                                         HashMap<String, Variable>
                                                 globalVarSet)
            throws VariableException{
        String varName, varValue;
        final int NO_ASSIGNMENT_NAME = 1;
        final int ASSIGNMENT_NAME = 2;
        final int ASSIGNMENT_VALUE = 3;
        final int NOSPACES_ASSIGNMENT_VALUE = 1;
        for (int i = 0; i < nameAndType.length; i++) {
            varValue = null;
            Matcher mSeparateNameAndEquals =
                    pSeparateNameAndEquals.matcher(nameAndType[i]);
            if (mSeparateNameAndEquals.matches()) {
                if (mSeparateNameAndEquals.group(NO_ASSIGNMENT_NAME)
                        != null) {
                    //The variable name is here if there is no equals
                    varName = mSeparateNameAndEquals.group
                            (NO_ASSIGNMENT_NAME);
                    if(varSet.containsKey(varName))
                        throw new BadNameVariableException
                                (VARIABLE_SCOPE_ERROR);
                    vars.add(createVariable(varType, varName, varValue,
                            isFinal, varScope));
                }
                else {
                    //The variable name is here if there is an equals sign
                    varName = mSeparateNameAndEquals.group(ASSIGNMENT_NAME);
                    if(varSet.containsKey(varName)) {
                        if(varType.equals("")) {
                            varType = varSet.get(varName).getType();
                            varValue = mSeparateNameAndEquals.group
                                    (ASSIGNMENT_VALUE);
                            varValue = varValue.trim();
                            varSet.get(varName).setValue(varValue);
                        }
                        else
                            throw new BadNameVariableException
                                    (VARIABLE_SCOPE_ERROR);
                    }
                    else if(varType.equals("") && globalVarSet.containsKey
                            (varName)) {
                        varType = globalVarSet.get(varName).getType();
                        varValue = mSeparateNameAndEquals.group
                                (ASSIGNMENT_VALUE);
                        varValue = varValue.trim();
                        globalVarSet.get(varName).setValue(varValue);
                    }
                    //The variable value is here if there is an equals
                    varValue = mSeparateNameAndEquals.group(ASSIGNMENT_VALUE);
                    varValue = varValue.trim();
                        assignValue(vars, isFinal, varName, varType, varValue,
                                varScope, varSet, globalVarSet);
                    }
                }
            //Couldn't separate the name=value expression
            else
                throw new VariableException
                        (VARIABLE_SYNTAX_ERROR);
        }
    }

    private static void assignValue(ArrayList<Variable> vars,
                                    boolean isFinal,String varName,
                                    String varType, String varValue,
                                    int varScope,
                                    HashMap<String, Variable> varSet,
                                    HashMap<String, Variable> globalVarSet)
            throws VariableException{
        // if varvalue is valid name, check if var with
        // name exists. If true, replace varValue with
        // the other var's value. Otherwise, we have an
        // invalid variable value.
        Matcher mValueIsOtherVariable = pValidName.matcher(varValue);
        Matcher mBooleanFormat = pBooleanFormat.matcher(varValue);
        if(mValueIsOtherVariable.matches()) {
            //search the hashmaps for an existing var
            if (varSet.containsKey(varValue)) {
                if(!varSet.get(varValue).isInit())
                    throw new InitializeVariableException
                            (UNINITIALIZED_VARIABLE_ERROR);
                varValue = varSet.get(varValue).getValue();
                vars.add(createVariable(varType, varName, varValue,
                        isFinal, varScope));
            }
            //The following only occurs when only the global variable set
            //Is sent to the factory, in which case sending both local
            //And global sets is unnecessary
            else if(globalVarSet != null) {
                if(globalVarSet.containsKey(varValue)) {
                    Variable globalVar = globalVarSet.get(varValue);
                    if(!globalVar.isInit())
                        throw new InitializeVariableException
                                (UNINITIALIZED_VARIABLE_ERROR);
                    varValue = globalVarSet.get(varValue).getValue();
                    vars.add(createVariable(varType, varName, varValue,
                            isFinal, varScope));
                }
            }
            else if(mBooleanFormat.matches()){
                vars.add(createVariable(varType, varName, varValue, isFinal,
                        varScope));
            }
            else {
                throw new InitializeVariableException
                        (VARIABLE_NONEXISTENT_ERROR);
            }
        }
        vars.add(createVariable(varType, varName, varValue,
                isFinal, varScope));
    }
    /*
     * Given a variable type, name, value and isFinal boolean, the function
     * Will return a single variable based on the given parameters.
     * @param varType A String variable type
     * @param varName A String variable Name
     * @param varValue A String variable value (null is permitted)
     * @param isFinal True iff the variable to be created should be final
     * @param varScope The variable scope
     * @return A Variable object with the given parameters
     * @throws VariableException
     */
    private static Variable createVariable(String varType, String varName,
                                           String varValue, boolean isFinal,
                                           int varScope)
            throws VariableException {
              switch (varType) {
                  case ReservedWords.INT:
                      checkValueSyntax(varValue, 'N');
                      return new IntVariable(varName,
                              varValue, isFinal, varScope);
                  case ReservedWords.DOUBLE:
                      checkValueSyntax(varValue, 'N');
                      return new DoubleVariable(varName,
                              varValue, isFinal, varScope);
                  case ReservedWords.BOOLEAN:
                      checkValueSyntax(varValue, 'N');
                      return new BooleanVariable(varName,
                              varValue, isFinal, varScope);
                  case ReservedWords.STRING:
                      checkValueSyntax(varValue, '\"');
                      return new StringVariable(varName,
                              varValue, isFinal, varScope);
                  case ReservedWords.CHAR:
                      checkValueSyntax(varValue, '\'');
                      return new CharVariable(varName,
                              varValue, isFinal, varScope);
                default:
                    throw new TypeVariableException();
            }

    }

    /*
     * This function makes sure that a given string is wrapped by a given
     * Wrapper delimiter. If not, an exception is thrown.
     * @param value A String value of the variable we want to checks
     * @param wrapper A char indicating which delimiter to search for
     * @param varScope The scope of the variable
     * @throws VariableException
     */
    private static void checkValueSyntax(String value, char wrapper)
            throws VariableException{
        if(value != null) {
            String regex;
            switch (wrapper){
                case '\"':
                    regex = Symbols.QUOTE_MARK + ".*" + Symbols.QUOTE_MARK;
                    break;
                case '\'':
                    regex = Symbols.APOSTROPHE + ".*" + Symbols.APOSTROPHE;
                    break;
                default:
                    regex = "(?!" + Symbols.QUOTE_MARK + ")" +
                            "(?!" + Symbols.APOSTROPHE + ").+" +
                            "(?!" + Symbols.QUOTE_MARK + ")" +
                            "(?!" + Symbols.APOSTROPHE + ")";
                    break;
            }
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(value);
            if (!m.matches())
                throw new VariableException(VARIABLE_FORMAT_ERROR);
        }
    }

}
