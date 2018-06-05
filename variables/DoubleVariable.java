package oop.ex6.variables;

/**
 * A Double Variable class
 */
public class DoubleVariable extends Variable {
    //private Double value;

    /**
     * The constructor.
     * @param name A String variable name
     * @param value A String variable value
     * @param isFinal true iff the variable should be set to a constant
     * @param varScope The scope of the variable
     * @throws VariableException if any error occurs when creating or
     * Assigning the variable.
     */
    public DoubleVariable(String name, String value, boolean isFinal,
                          int varScope) throws VariableException{
        super(name, value, isFinal, varScope);
    }

    /**
     * Sets the value of the variable after checking validity. An invalid
     * Value throws an exception.
     * @param value A String variable value
     * @throws VariableException if any error occurs when creating or
     * Assigning the variable.
     */
    public void setValue(String value) throws VariableException{
        exceptionFinal();
        try {
            this.value = new Value<Double>(Double.parseDouble(value));
            initializeVariable();
        }
        catch(NumberFormatException e){
            throw new VariableException("The value " + value +
                    " is not a valid double!");
        }
    }

    /**
     * Returns the variable type
     * @return "double"
     */
    public String getType(){
        return "double";
    }

    /**
     *
     * @return A string representation of the variable's value
     */
    public String getValue(){
        if(this.value != null)
            return this.value.data().toString();
        return null;
    }
}
