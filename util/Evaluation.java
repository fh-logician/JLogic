package util;

/**
 * A class used to keep track of evaluations for a logical expression.
 */
public class Evaluation implements Comparable {

    // Static Fields

    // Instance Fields

    private String expression;
    private TruthValue truthValue;
    private boolean value;

    // Constructors

    /**
     * Creates a new Evaluation object.
     *
     * @param expression The expression that this Evaluation object holds
     * @param truthValue The truthValue that is used in this Evaluation
     * @param value The boolean value this Evaluation evaluations to
     */
    public Evaluation(String expression, TruthValue truthValue, boolean value) {
        this.expression = expression;
        this.truthValue = truthValue;
        this.value = value;
    }

    // Getters

    /**
     * Returns the expression of this Evaluation object holds
     *
     * @return String
     */
    public String getExpression() {
        return expression;
    }

    /**
     * Returns the truth value this Evaluation object uses
     *
     * @return TruthValue
     */
    public TruthValue getTruthValue() {
        return truthValue;
    }

    /**
     * Returns the boolean value this Evaluation object evaluates to
     *
     * @return boolean
     */
    public boolean getValue() {
        return value;
    }
    
    // Overridden Methods

    public boolean equals(Object object) {

        if (! (object instanceof Evaluation))
            return false;
        
        else {
            Evaluation evaluation = (Evaluation) object;

            return (
                expression.equals(evaluation.expression) &&
                truthValue.equals(evaluation.truthValue) &&
                value == evaluation.value
            );
        }
    }

    public int compareTo(Object object) {

        Evaluation evaluation = (Evaluation) object;
        if (expression.length() == evaluation.expression.length())
            return expression.compareTo(evaluation.expression);
        return expression.length() - evaluation.expression.length();

    }
    
}
