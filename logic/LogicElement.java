package logic;

import java.util.LinkedList;
import util.Evaluation;
import util.TruthValue;

/**
 * An interface used to allow for flexibility inside the parseExpression method in {@link LogicTree}.
 */
public interface LogicElement {

    public int getOperatorType();
    public void setOperatorType(int operatorType);
    public boolean hasNot();
    public String getNot();
    public LinkedList<Evaluation> getTruthValues(LinkedList<TruthValue> truthValues);
    public boolean evaluate(TruthValue truthValue);

}