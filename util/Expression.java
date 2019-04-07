package util;

import java.util.LinkedList;

import logic.LogicElement;

/**
 * A class that holds information about an expression used in the parseExpression method.
 */
public class Expression {

    // Static Fields

    // Instance Fields

    private String[] variables;
    private LogicElement root;
    private boolean isSingle;

    // Constructors 

    /**
     * Creates a new Expression object.
     *
     * @param root The root LogicElement of the evaluated Expression
     * @param variables A LinkedList of variables that are in the expression
     * @param isSingle Whether or not this expression is a single variable expression
     */
    public Expression(LogicElement root, LinkedList<String> variables, boolean isSingle) {
        this.root = root;
        this.variables = new String[variables.size()];
        for (int i = 0; i < variables.size(); i++)
            this.variables[i] = variables.get(i);
        this.isSingle = isSingle;
    }
    
    // Getters

    /**
     * Returns the root LogicElement of this Expression object.
     *
     * @return LogicElement
     */
    public LogicElement getRoot() {
        return root;
    }

    /**
     * Returns an array of variables in this Expression object.
     *
     * @return String[]
     */
    public String[] getVariables() {
        return variables;
    }

    /**
     * Returns whether or not this Expression object is a single variable expression
     *
     * @return boolean
     */
    public boolean isSingle() {
        return isSingle;
    }
    
}
