package logic;

import java.util.LinkedList;

import exceptions.MissingTruthValueException;

import util.Evaluation;
import util.TruthValue;

/**
 * A class for a logic variable inside a logical expression.
 *
 */
public class LogicVar implements LogicElement {

    // Static Fields

    public static final int PSEUDO = 0;
    public static final int LOGIC = 1;
    public static final int CODE = 2;
    public static final int BOOLEAN = 3;

    public static final String[] OPERATORS = {"not ", "~", "!", "-"};

    // Instance Fields

    private String value;
    private boolean hasNot;
    private int operatorType;

    // Constructors

    /**
     * Creates a new LogicVar object.
     *
     * @param value The logic variable to hold in this LogicVar object.
     * @param hasNot Whether or not the logic variable has a NOT operator attached to it.
     * @param operatorType The type of operator this LogicVar object has.
     */
    public LogicVar(String value, boolean hasNot, int operatorType) {
        this.value = value;
        this.hasNot = hasNot;
        this.operatorType = operatorType;
    }

    /**
     * Creates a new LogicVar object.
     *
     * @param value The logic variable to hold in this LogicVar object.
     * @param hasNot Whether or not the logic variable has a NOT operator attached to it.
     */
    public LogicVar(String value, boolean hasNot) {
        this(value, hasNot, LOGIC);
    }

    /**
     * Creates a new LogicVar object.
     *
     * @param value The logic variable to hold in this LogicVar object.
     * @param operatorType The type of operator this LogicVar object has.
     */
    public LogicVar(String value, int operatorType) {
        this(value, false, operatorType);
    }

    /**
     * Creates a new LogicVar object.
     *
     * @param value The logic variable to hold in this LogicVar object.
     */
    public LogicVar(String value) {
        this(value, false, LOGIC);
    }

    // Getters

    /**
     * Returns the variable this LogicVar object holds.
     * 
     * @return String
     */
    public String getValue() {
        return value;
    }

    /**
     * Returns whether or not this LogicVar object has a NOT operator attached to it.
     * 
     * @return boolean
     */
    public boolean hasNot() {
        return hasNot;
    }

    /**
     * Returns the NOT operator as a String depending on what operator type this LogicVar object has.
     * 
     * @return String
     */
    public String getNot() {
        return OPERATORS[operatorType];
    }

    /**
     * Returns the operator type this LogicVar object has.
     * 
     * @return String
     */
    public int getOperatorType() {
        return operatorType;
    }

    // Setter Methods

    /**
     * Sets the operator type this LogicVar object has.
     *
     * @param operatorType The type of operator
     */
    public void setOperatorType(int operatorType) {
        this.operatorType = operatorType;
    }

    // Evaluation Methods

    /**
     * Returns a LinkedList of Evaluation's that are evaluated for each TruthValue specified.
     * 
     * @param truthValues A LinkedList of TruthValue's to evaluate this LogicVar with.
     * @return LinkedList<Evaluation>
     */
    public LinkedList<Evaluation> getTruthValues(LinkedList<TruthValue> truthValues) {

        // Keep track of evaluations
        LinkedList<Evaluation> evaluations = new LinkedList<>();

        // Only make evaluations if this LogicVar object has a NOT operator
        // attached to it
        if (! hasNot())
            return evaluations;

        // Iterate through truthValues
        for (int i = 0; i < truthValues.size(); i++) {

            Evaluation evaluation = new Evaluation(
                "" + this,
                truthValues.get(i),
                evaluate(truthValues.get(i))
            );

            // Only add evaluation if it doesn't already exist
            if (! evaluations.contains(evaluation))
                evaluations.add(evaluation);
        }

        return evaluations;
    }

    /**
     * Evaluates this LogicVar object given a TruthValue to use to evaluate it.
     *
     * @param truthValue A TruthValue object to evaluate this LogicVar object.
     * @return boolean
     * @throws MissingTruthValueException When the truth value for this LogicVar does not exist.
     */
    public boolean evaluate(TruthValue truthValue) {

        // Check if the truthValue does not have the value held in this LogicVar object
        if (! truthValue.has(value))
            throw new MissingTruthValueException(
                String.format(
                    "Required truth value for the variable \"%s\".",
                    value
                )
            );

        // Check if this LogicVar object has a NOT operator attached to it
        if (hasNot())
            return ! truthValue.get(value);
        return truthValue.get(value);

    }

    // Overridden Methods

    public String toString() {

        if (hasNot())
            return getNot() + getValue();
            
        return getValue();
    }

    public boolean equals(Object object) {

        if (! (object instanceof LogicVar))
            return false;

        else {
            LogicVar logicVar = (LogicVar) object;

            return (
                value.equals(logicVar.value) &&
                hasNot == logicVar.hasNot
            );
        }
    }

}