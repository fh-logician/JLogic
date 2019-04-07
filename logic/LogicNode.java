package logic;

import java.util.LinkedList;

import logic.LogicVar;

import util.Evaluation;
import util.TruthValue;

public class LogicNode implements LogicElement {

    // Static Fields

    public static final int NOT = 0;
    public static final int AND = 1;
    public static final int OR = 2;
    public static final int IMPLIES = 3;
    public static final int BICONDITIONAL = 4;
    public static final int NAND = 5;
    public static final int NOR = 6;

    public static final int PSEUDO = 0;
    public static final int LOGIC = 1;
    public static final int CODE = 2;

    public static final String[][] OPERATORS = {
        {"not ", "and", "or", "implies", "iff", "nand", "nor"},
        {"~", "^", "v", "->", "<->", "|", "â¬‡"},
        {"!", "&&", "||", "->", "<->", "|", "â¬‡"}
    };

    // Instance Fields

    private LogicElement left;
    private LogicElement right;
    private int operator;
    private int operatorType;
    private boolean hasNot;
    
    // Constructors

    /**
     * Creates a new LogicNode object.
     *
     * @param left The LogicElement that is on the left side of the expression.
     * @param operator The operator that this LogicNode object holds.
     * @param right The LogicElement that is on the right side of the expression.
     * @param hasNot Whether or not the entire LogicNode has a NOT operator attached to it.
     * @param operatorType The type of operator this LogicNode has.
     */
    public LogicNode(LogicElement left, int operator, LogicElement right, boolean hasNot, int operatorType) {
        this.left = left;
        this.operator = operator;
        this.right = right;
        this.hasNot = hasNot;
        if (operatorType < 0 || operatorType > 2)
            operatorType = LOGIC;
        this.operatorType = operatorType;
    }

    /**
     * Creates a new LogicNode object.
     *
     * @param left The LogicElement that is on the left side of the expression.
     * @param operator The operator that this LogicNode object holds.
     * @param right The LogicElement that is on the right side of the expression.
     * @param hasNot Whether or not the entire LogicNode has a NOT operator attached to it.
     */
    public LogicNode(LogicElement left, int operator, LogicElement right, boolean hasNot) {
        this(left, operator, right, hasNot, LOGIC);
    }

    /**
     * Creates a new LogicNode object.
     *
     * @param left The LogicElement that is on the left side of the expression.
     * @param operator The operator that this LogicNode object holds.
     * @param right The LogicElement that is on the right side of the expression.
     * @param operatorType The type of operator this LogicNode has.
     */
    public LogicNode(LogicElement left, int operator, LogicElement right, int operatorType) {
        this(left, operator, right, false, operatorType);
    }

    /**
     * Creates a new LogicNode object.
     *
     * @param left The LogicElement that is on the left side of the expression.
     * @param operator The operator that this LogicNode object holds.
     * @param right The LogicElement that is on the right side of the expression.
     */
    public LogicNode(LogicElement left, int operator, LogicElement right) {
        this(left, operator, right, false, LOGIC);
    }

    // Getters

    /**
     * Returns the LogicElement on the left side of the expression.
     *
     * @return LogicElement
     */
    public LogicElement getLeft() {
        return left;
    }

    /**
     * Returns the LogicElement on the right side of the expression.
     *
     * @return LogicElement
     */
    public LogicElement getRight() {
        return right;
    }

    /**
     * Returns the operator integer that this LogicNode object holds.
     *
     * @return int
     */
    public int getOperatorInt() {
        return operator;
    }

    /**
     * Returns the type of operator that this LogicNode object has.
     *
     * @return int
     */
    public int getOperatorType() {
        return operatorType;
    }

    /**
     * Returns the operator of this LogicNode object as a String.
     *
     * @return String
     */
    public String getOperator() {
        return OPERATORS[operatorType][operator];
    }

    /**
     * Returns whether or not this LogicNode object has a NOT operator attached to it.
     *
     * @return boolean
     */
    public boolean hasNot() {
        return hasNot;
    }

    /**
     * Returns the NOT operator of this LogicNode object as a String.
     * 
     * @return String
     */
    public String getNot() {
        return OPERATORS[operatorType][NOT];
    }

    // Evaluation Methods

    /**
     * Creates the truth value evaluations for this LogicNode object.
     * 
     * @param truthValues A LinkedList of TruthValue objects to use to evaluate this LogicNode object with.
     * @return LinkedList<Evaluation>
     */
    public LinkedList<Evaluation> getTruthValues(LinkedList<TruthValue> truthValues) {

        // Keep track of evaluations
        LinkedList<Evaluation> evaluations = new LinkedList<>();

        // Get all left evaluations
        LinkedList<Evaluation> leftEvaluations = getLeft().getTruthValues(truthValues);
        //  Then add all evaluations as long as they don't exist in the evaluations already
        for (int i = 0; i < leftEvaluations.size(); i++)
            if (! evaluations.contains(leftEvaluations.get(i)))
                evaluations.add(leftEvaluations.get(i));

        // Get all right evaluations
        LinkedList<Evaluation> rightEvaluations = getRight().getTruthValues(truthValues);
        //  Then add all evaluations as long as they don't exist in the evaluations already
        for (int i = 0; i < rightEvaluations.size(); i++)
            if (! evaluations.contains(rightEvaluations.get(i)))
                evaluations.add(rightEvaluations.get(i));

        // Get self evaluation
        //  Iterate through all truth values in the truthValues LinkedList
        //  Only add evaluations that don't already exist
        for (int i = 0; i < truthValues.size(); i++) {

            Evaluation evaluation = new Evaluation(
                "" + this,
                truthValues.get(i),
                evaluate(truthValues.get(i))
            );

            if (! evaluations.contains(evaluation))
                evaluations.add(evaluation);
        }

        return evaluations;
    }

    /**
     * Evaluates this LogicNode object with the specified truth value
     *
     * @param truthValue The TruthValue object to use to evaluate the left and right sides of this LogicNode object.
     * @return boolean
     */
    public boolean evaluate(TruthValue truthValue) {

        // Get left and right evaluations
        boolean left = getLeft().evaluate(truthValue);
        boolean right = getRight().evaluate(truthValue);

        boolean value = false;

        if (operator == AND)
            value = left && right;
        else if (operator == OR)
            value = left || right;
        else if (operator == IMPLIES)
            value = ! left || right;
        else if (operator == BICONDITIONAL)
            value = left == right;
        else if (operator == NAND)
            value = ! (left && right);
        else if (operator == NOR)
            value = ! (left || right);
        
        if (hasNot())
            return ! value;
        return value;
    }

    // Overridden Methods

    public String toString() {

        // Get left and right String representations
        String left = "" + this.left;
        String right = "" + this.right;

        // Check if the left or right do not have NOT operators attached to it
        if (this.left instanceof LogicNode && ! this.left.hasNot())
            left = "(" + left + ")";
        if (this.right instanceof LogicNode && ! this.right.hasNot())
            right = "(" + right + ")";
        
        // Return the String representations of this LogicNode object
        if (hasNot())
            return String.format(
                "%s(%s %s %s)",
                getNot(), left, getOperator(), right
            );
        
        return String.format(
            "%s %s %s",
            left, getOperator(), right
        );
    }

    public boolean equals(Object object) {

        if (! (object instanceof LogicNode))
            return false;
        
        else {
            LogicNode logicNode = (LogicNode) object;

            return (
                left.equals(logicNode.left) &&
                right.equals(logicNode.right) &&
                operator == logicNode.operator &&
                hasNot == logicNode.hasNot
            );
        }
    }
    
}
