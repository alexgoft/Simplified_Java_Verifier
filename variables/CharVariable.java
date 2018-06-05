package oop.ex6.variables;

/**
 * A Character Variable class
 */
public class CharVariable extends Variable {
    static final int CHAR_LENGTH = 3; //format 'A'
    static final int CHARACTER_INDEX = 1;

    /**
     * The constructor.
     * @param name A String variable name
     * @param value A String variable value
     * @param isFinal true iff the variable should be set to a constant
     * @param varScope the scope of the variable
     * @throws VariableException if an error occurs when initializing or
     * Changing the variable
     */
    public CharVariable(String name, String value, boolean isFinal,
                        int varScope) throws VariableException{
        super(name, value, isFinal, varScope);
    }

    /**
     * Sets the value of the variable after checking validity. An invalid
     * Value throws an exception.
     * @param value A String variable value
     * @throws VariableException if an error occurs when initializing or
     * Changing the variable
     */
    public void setValue(String value) throws VariableException{
        exceptionFinal();
        if(value.length() == CHAR_LENGTH) {
            this.value = new Value<Character>(value.charAt(CHARACTER_INDEX));
            initializeVariable();
        }
        else
            throw new VariableException("The variable" + this.name +
                    "must contain a single string character only.");
    }

    /**
     * Returns the variable type
     * @return "char"
     */
    public String getType(){
        return "char";
    }

    /**
     *
     * @return A string representation of the variable's value
     */
    public String getValue(){
        if(this.value != null)
            return "\'" + this.value.data().toString() + "\'";
        return null;
    }
}
