package oop.ex6.variables;

import oop.ex6.syntaxtools.*;

/**
 * A Boolean variable class
 */
public class BooleanVariable extends Variable {

    /**
     * The constructor.
     * @param name A String variable name
     * @param value A String variable value
     * @param isFinal true iff the variable should be set to a constant
     * @param varScope the scope of the variable
     * @throws VariableException if any error occurs when creating or
     * Assigning the variable.
     */
    public BooleanVariable(String name, String value, boolean isFinal,
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
            try {
                if(value.equals(ReservedWords.TRUE) ||
                        value.equals(ReservedWords.FALSE))
                    this.value = new Value<Boolean>
                            (Boolean.parseBoolean(value));
                else {
                    double check = Double.parseDouble(value);
                    this.value = new Value<Boolean>
                            (Boolean.parseBoolean(value));
                }
                initializeVariable();
            } catch (NumberFormatException e) {
                throw new VariableException("The value " + value + " is not a" +
                        " valid boolean Regex!");
            }
    }

    /**
     * Returns the variable type
     * @return "boolean"
     */
    public String getType(){
        return "boolean";
    }

    /**
     *
     * @return A string representation of the value
     */
    public String getValue(){
        if(this.value != null)
            return this.value.data().toString();
        else
            return null;
    }
}
