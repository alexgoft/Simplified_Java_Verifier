package oop.ex6.variables;

import oop.ex6.syntaxtools.Regex;
import oop.ex6.syntaxtools.ReservedWords;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * An abstract Variable class.
 */
public abstract class Variable {
    protected String name;
    protected boolean isFinal;
    protected int varScope;
    protected boolean isInitialized = false;
    protected Value<?> value;
   public static Pattern validName =
           Pattern.compile(Regex.VARIABLE_NAME_FORMAT);

    protected enum VarTypes{
        INT{public String toString(){return "int";}},
        DOUBLE{public String toString(){return "double";}},
        STRING{public String toString(){return "String";}},
        CHAR{public String toString(){return "char";}},
        BOOLEAN{public String toString(){return "boolean";}}
    }
    /**
     * Given a String that represents the type of the variable, the Method returns a String of the
     * regex that fits that type.
     *
     * @param type the String of the type.
     * @return a String of the regex that fits that type. null if the type is not supported by Sjavac.
     */
    public static String getPattern(String type) {
        switch (type) {
            case Regex.BOOLEAN_TYPE:
                return Regex.BOOLEAN_FORMAT;
            case Regex.CHAR_TYPE:
                return Regex.CHAR_FORMAT;
            case Regex.DOUBLE_TYPE:
                return Regex.DOUBLE_FORMAT;
            case Regex.INT_TYPE:
                return Regex.INT_FORMAT;
            case Regex.STRING_TYPE:
                return Regex.STRING_FORMAT;
            default:
                return null;
        }
    }
    /**
     * The constructor.
     * @param name A String variable name
     * @param value A String variable value
     * @param isFinal true iff the variable should be set to a constant
     * @param varScope An integer indicating the scope of the variable
     * @throws VariableException if any error occurs when creating or
     * Assigning the variable.
     */
    public Variable(String name, String value, boolean isFinal,
                    int varScope) throws VariableException {
        setName(name);
        this.varScope = varScope;
        if(value != null) {
            setValue(value);
            this.isInitialized = true;
        }
        if(this.varScope == ReservedWords.METHOD_SCOPE)
            this.isInitialized = true;
        this.isFinal = isFinal;
        if(this.isFinal) {
            if (value == null) {
                throw new VariableException("The constant " + this.name +
                        "must have an assigned value!");
            }
        }
    }


    /**
     * Sets the name data member of the variable after checking the name's
     * Validity. An invalid name throws an exception
     * @param name A String variable name
     * @throws VariableException if any error occurs when creating or
     * Assigning the variable.
     */
    protected void setName(String name) throws VariableException{
        Matcher m = validName.matcher(name);
        if(m.matches()) {
            this.name = name;
            for (VarTypes types : VarTypes.values()) {
                if (this.name.equals(types.toString())) {
                    throw new BadNameVariableException("A reserved word" +
                            " was used to name a variable!");
                }
            }
        }
        else
            throw new BadNameVariableException("The variable" + name +
                    "is illegal!");
    }

    /**
     * Sets the value of the variable after checking validity. An invalid
     * Value throws an exception.
     * @param value A String variable value
     * @throws VariableException if any error occurs when creating or
     * Assigning the variable.
     */
    public void setValue(String value) throws VariableException{
        initializeVariable();
    }


    /**
     *
     * @return The Variable's name
     */
    public String getName(){
        return this.name;
    }

    /**
     * Throws an exception if the variable is final. This Method is used only
     * When changing a Variable's value.
     * @throws VariableException if any error occurs when creating or
     * Assigning the variable.
     */
    protected void exceptionFinal() throws VariableException{
        if(this.isFinal)
            throw new VariableException("The variable " + this.name + " is a " +
                    "constant and cannot be changed!");
    }

    /**
     *
     * @return true iff the variable is initialized
     */
    public boolean isInit(){
        return this.isInitialized;
    }

    /**
     *
     * @return The variable scope
     */
    public int getVarScope(){
        return this.varScope;
    }

    /**
     * This function is used when changing a variable's value. If the variable
     * Is a global variable, the isInitialized value will not be changed.
     */
    protected void initializeVariable(){
        if(this.varScope != ReservedWords.GLOBAL_SCOPE)
            this.isInitialized = true;
    }

    /**
     *
     * @return true iff the variable is a constant
     */
    public boolean isFinal(){
        return this.isFinal;
    }

    /**
     * Returns the variable type
     * @return A String of the variable type
     */
    public abstract String getType();

    /**
     * Returns a string representation of the variable's value
     * @return A string representation of the variable's value
     */
    public abstract String getValue();


}
