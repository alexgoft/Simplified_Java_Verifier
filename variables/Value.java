package oop.ex6.variables;

/**
 * A generic class for holding a value for a variable
 */
public class Value<T> {
    private T data;

    /**
     * The Constructor. Can receive any data type
     * @param data A data element
     */
    public Value(T data){
        this.data = data;
    }

    public T data(){
        return this.data;
    }
}
