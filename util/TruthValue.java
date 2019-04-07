package util;

/**
 * A class that holds information about truth values of multiple variables.
 */ 
public class TruthValue {

    // Static Fields

    // Instance Fields

    private String[] variables;
    private boolean[] values;

    // Constructors

    /**
     * Creates a new TruthValue object
     *
     * @param variables An array of variables in this TruthValue object
     * @param values An array of matching boolean values in this TruthValue object
     */
    public TruthValue(String[] variables, boolean[] values) {
        this.variables = variables;
        this.values = values;
    }

    // Getters

    /**
     * Returns whether or not this TruthValue has the specified variable
     * 
     * @param variable The variable to search for in this TruthValue object
     * @return boolean
     */
    public boolean has(String variable) {
        for (int i = 0; i < variables.length; i++)
            if (variables[i].equalsIgnoreCase(variable))
                return true;
        
        return false;
    }

    /**
     * Returns the boolean value of this TruthValue object
     *
     * @param variable The variable to get the boolean value of
     * @return boolean
     */
    public boolean get(String variable) {
        for (int i = 0; i < variables.length; i++)
            if (variables[i].equalsIgnoreCase(variable))
                return values[i];
        
        return false;
    }

    /**
     * Returns an array of variables in this TruthValue object
     *
     * @return String[]
     */
    public String[] getVariables() {
        return variables;
    }

    // Overridden Methods

    public boolean equals(Object object) {
        if (! (object instanceof TruthValue))
            return false;
        
        else {
            TruthValue truthValue = (TruthValue) object;

            // Iterate through variables in this truth value; Check if the object does not have variable
            for (int i = 0; i < variables.length; i++) {
                if (! truthValue.has(variables[i]))
                    return false;
                else if (truthValue.get(variables[i]) != this.get(variables[i]))
                    return false;
            }

            return true;
        }
    }

}
