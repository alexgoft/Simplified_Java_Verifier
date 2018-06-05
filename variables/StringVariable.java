package oop.ex6.variables;

/**
 * A String Variable class
 */
public class StringVariable extends Variable {

    /**
     * The constructor.
     * @param name A String variable name
     * @param value A String variable value
     * @param isFinal true iff the variable should be set to a constant
     * @param varScope The sJava file line number
     * @throws VariableException if an error occurs when initializing or
     * Changing the variable
     */
    public StringVariable(String name, String value, boolean isFinal,
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
        initializeVariable();
        this.value = new Value<String>(value);
    }

    /**
     * Returns the variable type
     * @return "String"
     */
    public String getType(){
        return "String";
    }

    /**
     *
     * @return A string representation of the variable's value
     */
    public String getValue(){
        if(this.value != null)
            return "\"" + this.value.data().toString() + "\"";
        return null;
    }
}
