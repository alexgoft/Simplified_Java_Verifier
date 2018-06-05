package oop.ex6.variables;


/**
 * An Integer Variable class
 */
public class IntVariable extends Variable {

    /**
     * The constructor.
     * @param name A String variable name
     * @param value A String variable value
     * @param isFinal true iff the variable should be set to a constant
     * @param varScope the scope of the variable
     * @throws VariableException if an error occurs when initializing or
     * Changing the variable
     */
    public IntVariable(String name, String value, boolean isFinal ,
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
                this.value = new Value<Integer>(Integer.parseInt(value));
                initializeVariable();
            } catch (NumberFormatException e) {
                throw new VariableException("The value " + value +
                        " is not a valid integer!");
                /////
            }
        }

    /**
     * Returns the variable type
     * @return "int"
     */
    public String getType(){
        return "int";
    }

    /**
     *
     * @return A string representation of the variable's value
     */
    public String getValue(){
        if(this.value != null)
            return this.value.data().toString();
        else
            return null;
    }
}
