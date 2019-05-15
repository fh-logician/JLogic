package logic;

import java.util.LinkedList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import exceptions.InvalidExpressionException;
import exceptions.UnbalancedParenthesesException;

import logic.LogicElement;
import logic.LogicNode;
import logic.LogicVar;

import util.Evaluation;
import util.Expression;
import util.TruthValue;
import util.QuineMcCluskey;

/**
 * A class for a LogicTree that represents a logical expression
 */
public class LogicTree {

    // Static Fields

    public static final String[] INPUT_OPERATORS = {
        "NAND", "nand", "-*",
        "NOR", "nor", "-+",
        "OR", "or", "||", "+",
        "AND", "and", "&&", "*",
        "NOT ", "not ", "NOT", "not", "!", "-",
        "IFF", "iff", "<->",
        "IMPLIES", "implies", "->"
    };

    public static final String[] OUTPUT_OPERATORS = {
        "|", "|", "|",
        ":", ":", ":",
        "v", "v", "v", "v",
        "^", "^", "^", "^",
        "~", "~", "~", "~", "~", "~",
        "=", "=", "=",
        ">", ">", ">"
    };

    public static final int NONE = -1;

    public static final String validVars = "abcdefghijklmnopqrstuwxyz";

    // Instance Fields

    private String expression;
    private String[] variables;
    private LogicElement root;

    // Constructors

    /**
     * Creates a new LogicTree object.
     *
     * @param expression The expression to insert into the LogicTree object.
     */
    public LogicTree(String expression) {
        this.expression = expression;
        this.variables = new String[0];
        this.root = null;
        parse();
    }

    // Getters

    /**
     * Returns the expression this LogicTree object holds.
     *
     * @return String
     */
    public String getExpression() {
        return expression;
    }

    /**
     * Returns an array of variables in this LogicTree object.
     *
     * @return String[]
     */
    public String[] getVariables() {
        return variables;
    }

    /**
     * Returns the root LogicElement of this LogicTree object.
     *
     * @return LogicElement
     */
    public LogicElement getRoot() {
        return root;
    }

    // Evaluation Methods

    /**
     * Parses the expression held in this LogicTree object.
     */
    public void parse() {
        Expression exp = parseExpression(expression);

        if (exp.isSingle())
            this.root = (LogicVar) exp.getRoot();
        else
            this.root = exp.getRoot();
        
        this.expression = "" + this.root;
        this.variables = exp.getVariables();
    }

    /**
     * Returns a LinkedList of evaluations made from this LogicTree object
     *
     * @return LinkedList<Evaluation>
     */
    public LinkedList<Evaluation> getTruthValues() {

        // Create every possible truth combination for all variables
        LinkedList<TruthValue> truthValues = new LinkedList<TruthValue>();

        // Iterate through 2 ^ variableLength possible combinations
        for (int i = 0; i < (int) Math.pow(2, this.variables.length); i++) {

            String[] vars = new String[this.variables.length];
            boolean[] vals = new boolean[this.variables.length];

            // Iterate through all variables
            for (int j = 0; j < this.variables.length; j++) {

                // Get the power based off of the variable's index in the list
                int power = this.variables.length - j - 1;
                String variable = this.variables[j];

                // Get the truth value using the getTruthValue method
                vars[j] = variable;
                vals[j] = getTruthValue(i, power);
            }

            truthValues.add(new TruthValue(vars, vals));
        }

        // Create truth values for other operations
        // For example, if there is a "~a", then there will be a column designated to that.
        //              if there is a "~(a v b)", then there will be a column designated to that
        //                  as well as the "a v b" part.
        LinkedList<Evaluation> evaluations = new LinkedList<Evaluation>();

        LinkedList<Evaluation> rootEvaluations = root.getTruthValues(truthValues);

        // Add all the truth evaluations from the root
        for (int i = 0; i < rootEvaluations.size(); i++)
            if (! evaluations.contains(rootEvaluations.get(i)))
                evaluations.add(rootEvaluations.get(i));
        
        // Add all the truth values as evaluations
        for (int i = 0; i < truthValues.size(); i++) {
            TruthValue truthValue = truthValues.get(i);

            for (int j = 0; j < truthValue.getVariables().length; j++) {
                String variable = truthValue.getVariables()[j];

                Evaluation evaluation = new Evaluation(
                    variable,
                    new TruthValue(
                        new String[] { variable },
                        new boolean[] { truthValue.get(variable) }
                    ),
                    truthValue.get(variable)
                );

                evaluations.add(evaluation);
            }
        }

        return evaluations;
    }

    public boolean[] getExpressionTruths() {

        // Get the evaluations and keep track of each value
        LinkedList<Evaluation> evaluations = getTruthValues();
        LinkedList<Boolean> values = new LinkedList<>();
        for (int i = 0; i < evaluations.size(); i++) {
            Evaluation evaluation = evaluations.get(i);
            if (evaluation.getExpression().equals(this.toString()))
                values.add(evaluation.getValue());
        }

        // Turn the values into an array
        boolean[] valuesArray = new boolean[values.size()];
        for (int i = 0; i < values.size(); i++)
            valuesArray[i] = values.get(i);
        return valuesArray;
        
    }

    public String[] makeTable() {

        // Keep track of lines and temporary results
        LinkedList<String> lines = new LinkedList<String>();
        String result = "";

        // Setup truth table
        LinkedList<Evaluation> evaluations = getTruthValues();
        HashMap<String, LinkedList<Boolean>> tableMap = new HashMap<String, LinkedList<Boolean>>();

        // Add all expressions as a LinkedList of boolean values
        for (int i = 0; i < evaluations.size(); i++) {
            if (! tableMap.containsKey(evaluations.get(i).getExpression()))
                tableMap.put(evaluations.get(i).getExpression(), new LinkedList<Boolean>());
            
            tableMap.get(evaluations.get(i).getExpression()).add(evaluations.get(i).getValue());
        }

        // Create a key set and sort the set by length of expression or value of expression
        int count = 0;
        int length = tableMap.size();
        LinkedList<String> keySet = new LinkedList<String>(tableMap.keySet());

        Collections.sort(keySet, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                if (s1.length() == s2.length())
                    return s1.compareTo(s2);
                return s1.length() - s2.length();
            }
        });

        // Add column labels to the table
        for (int i = 0; i < keySet.size(); i++) {
            String key = keySet.get(i);
            String line = "| " + center(key, key.length());
            if (count > 0)
                line = " " + line;
            if (count == length - 1)
                line += " |";
            result += line;
            count += 1;
        }
        lines.add(result);
        result = "";

        // Add label split line
        count = 0;
        for (int i = 0; i < keySet.size(); i++) {
            String key = keySet.get(i);
            String line = "+" + center("-", key.length() + 1, "-");
            if (count > 0)
                line = "-" + line;
            if (count == length - 1)
                line += "-+";
            result += line;
            count += 1;
        }
        lines.add(result);
        result = "";

        // Add truth values as part of the main table
        int maxTruths = (int) Math.pow(2, this.variables.length);

        for (int i = 0; i < maxTruths; i++) {
            count = 0;
            for (int j = 0; j < keySet.size(); j++) {
                String key = keySet.get(j);

                boolean value = (boolean) tableMap.get(key).get(i);
                String boolValue = value ? "T": "F";

                String line = "| " + center(boolValue, key.length());
                if (count > 0)
                    line = " " + line;
                if (count == length - 1)
                    line += " |";
                result += line;
                count += 1;
            }
            lines.add(result);
            result = "";
        }

        String[] newLines = new String[lines.size()];
        for (int i = 0; i < lines.size(); i++)
            newLines[i] = lines.get(i);
        
        return newLines;
    }

    /**
     * Returns a truth table as a String for this LogicTree object
     *
     * @return String
     */
    public String makeTableAsString() {
        String[] lines = makeTable();
        String result = "";
        for (int i = 0; i < lines.length; i++)
            result += lines[i] + "\n";
        return result;
    }

    public String simplify() {

        // Get the operator type
        int operatorType = this.root.getOperatorType();

        // Get the solver data
        //  First we need to get the indexes where the Node is True and invert the number
        //  For example: if the expression is True at indexes 0, 1, and 3, then the new list would be
        //      4, 6, 7
        LinkedList<Integer> trueAt = new LinkedList<>();
        boolean[] truths = getExpressionTruths();
        for (int value = 0; value < truths.length; value++)
            if (truths[value])
                trueAt.add(truths.length - value - 1);
        
        // Turn the trueAt into an array
        int[] trueAtArray = new int[trueAt.size()];
        for (int i = 0; i < trueAt.size(); i++)
            trueAtArray[i] = trueAt.get(i);
        
        // Create a QuineMcCluskey object
        QuineMcCluskey qm = new QuineMcCluskey(this.variables, trueAtArray);
        String function = qm.getFunction();

        // Replace AND, NOT, and OR with proper symbols
        if (operatorType == LogicNode.PSEUDO)
            function = function.replace("AND", "and").replace("OR", "or").replace("NOT ", "not ");
        else if (operatorType == LogicNode.LOGIC)
            function = function.replace("AND", "^").replace("OR", "v").replace("NOT ", "~");
        else if (operatorType == LogicNode.CODE)
            function = function.replace("AND", "&&").replace("OR", "||").replace("NOT ", "!");
        else if (operatorType == LogicNode.BOOLEAN)
            function = function.replace("AND", "*").replace("OR", "+").replace("NOT ", "-");
        
        System.out.println(function);
        
        // Check if function is always true or always false
        if (function.equals("1"))
            return "Always True";
        else if (function.equals("0"))
            return "Always False";
        
        // Function is not always true or always false
        return (new LogicTree(function)).toString();
    }

    public void printTable() {
        String[] lines = makeTable();
        String result = "";

        for (int i = 0; i < lines.length; i++)
            result += lines[i] + "\n";
        
        System.out.print(result);
    }

    // Overridden Methods

    public String toString() {
        return expression;
    }

    public boolean equals(Object object) {
        if (! (object instanceof LogicTree))
            return false;
        
        else {
            LogicTree logicTree = (LogicTree) object;

            return root == logicTree.getRoot();
        }
    }

    // Static Methods

    /**
     * Returns the boolean truth value for a specific variable given the index and power
     * 
     * @param value The index for the variable to
     * @param power The power value of the variable.
     * @return boolean
     */
    public static boolean getTruthValue(int value, int power) {
        power = (int) Math.pow(2, power);
        return ((value / power) % 2) == 0;
    }

    /**
     * Returns whether or not the specified object is in the specified array.
     *
     * @param object The object to search for
     * @param array The array to search in
     * @return boolean
     */
    public static boolean valueIn(Object object, Object[] array) {
        for (Object obj: array)
            if (obj.equals(object))
                return true;
        return false;
    }

    /**
     * Returns a String of text centered across the specified length
     *
     * @param text The text to center
     * @param length The length of the resulting String
     * @param fillString The String to fill the centered text with
     * @return String
     */
    public static String center(String text, int length, String fillString) {

        // Return the text if length is less than the text length
        if (text.length() >= length)
            return text;
        
        // Get the amount of characters on both the left and right sides
        int leftAmt = (length - text.length()) / 2;
        int rightAmt = length - text.length() - leftAmt;

        // Setup left and right strings
        String leftString = "";
        String rightString = "";
    
        for (int i = 0; i < leftAmt; i++)
            leftString += fillString;
        for (int i = 0; i < rightAmt; i++)
            rightString += fillString;
        
        return leftString + text + rightString;
    }

    /**
     * Returns a String of text centered across the specified length
     *
     * @param text The text to center
     * @param length The length of the resulting String
     * @return String
     */
    public static String center(String text, int length) {
        return center(text, length, " ");
    }

    /**
     * Returns whether or not the specified expression is valid
     *
     * @param expression The expression to validate
     * @return boolean
     */
    public static boolean isExpressionValid(String expression) {

        // Remove all spaces, parentheses, and NOT operators from the expression
        expression = expression.replace("(", "").replace(")", "").replace(" ", "").replace("~", "");

        // Iterate through operators and split by it
        // Find if there are variables containing multiple characters
        for (int i = 0; i < LogicNode.OPERATORS[LogicNode.LOGIC].length; i++) {
            String[] tokenSplit = expression.split(LogicNode.OPERATORS[LogicNode.LOGIC][i]);
            if (tokenSplit.length > 1) {

                // Go through each index of the tokenSplit array
                // And check if the item has length > 1
                for (int j = 0; j < tokenSplit.length; j++) {

                    // Check if length of item is greater than 1
                    if (tokenSplit[j].length() > 1) {

                        // Check if item has an operator in last or first index
                        if (valueIn(tokenSplit[j].charAt(0), LogicNode.OPERATORS[LogicNode.LOGIC]) || valueIn(tokenSplit[j].charAt(tokenSplit[j].length() - 1), LogicNode.OPERATORS[LogicNode.LOGIC]))
                            return false;
                        
                        // Check if item has any operators
                        else {
                            LinkedList<String> operators = new LinkedList<>();

                            for (String operator: LogicNode.OPERATORS[LogicNode.LOGIC])
                                if (tokenSplit[j].indexOf(operator) != -1)
                                    operators.add(operator);
                            
                            if (operators.size() == 0)
                                return false;
                        }

                        // Recursive call
                        return isExpressionValid(tokenSplit[j]);
                    }
                }
            }

            // Check if length of item is 0
            else if (tokenSplit.length == 0)
                return false;
        }

        return true;
    }

    /**
     * Parses a logical expression.
     *
     * @param expression The logical expression to parse
     * @param hasNot Whether or not the logical expression has a NOT operator attached to it
     * @param operatorType The type of operator to apply to all subexpressions.
     * @return Expression
     */
    public static Expression parseExpression(String expression, boolean hasNot, int operatorType) {

        // Remove all spaces from the expression
        expression = expression.replace(" ", "");

        // Go through all operators and replace expressions
        for (int i = 0; i < INPUT_OPERATORS.length; i++) {

            // Find first operator that can be replaced and set operatorType to the proper type
            if (expression.contains(INPUT_OPERATORS[i]) && operatorType == NONE) {

                // Iterate through operatorTypes
                int[] operatorTypes = {LogicNode.PSEUDO, LogicNode.LOGIC, LogicNode.CODE};
                boolean doBreak = false;
                for (int opType: operatorTypes) {

                    for (String operator: LogicNode.OPERATORS[opType])
                        if (INPUT_OPERATORS[i].equals(operator)) {
                            operatorType = opType;
                            doBreak = true;
                            break;
                        }

                    if (doBreak)
                        break;
                
                }

                if (operatorType == NONE)
                    operatorType = LogicNode.LOGIC;

            }
            
            // Standardize all operators to LOGIC type
            expression = expression.replace(INPUT_OPERATORS[i], OUTPUT_OPERATORS[i]);
        }

        // Setup required variables
        LogicElement left = null;
        int operator = NONE;
        LogicElement right = null;
        LinkedList<String> expVariables = new LinkedList<String>();

        int parentDepth = 0;
        int last = 0;

        boolean charHasNot = false;
        boolean tempHasNot = false;

        // Determine whether or not expression is valid
        if (! isExpressionValid(expression))
            throw new InvalidExpressionException("That is an invalid expression.");
        
        // Loop through and find any operators as expressions
        for (int i = 0; i < expression.length(); i++) {
            char chr = expression.charAt(i);

            // Check for open parenthesis
            if (chr == '(') {
                if (parentDepth == 0)
                    last = i + 1;
                parentDepth++;
            }

            // Check for close parenthesis
            else if (chr == ')') {
                parentDepth--;

                // Parse expression if parentheses depth reaches 0
                if (parentDepth == 0) {

                    // Check if there is a ~ (NOT) operator directly in front of the parenthesis
                    if (last - 1 > 0) {
                        if (expression.charAt(last - 2) == '~')
                            tempHasNot = true;
                    }

                    Expression exp = parseExpression(expression.substring(last, i), tempHasNot, operatorType);
                    if (i == expression.length() - 1 && last == 0)
                        hasNot = tempHasNot;
                    tempHasNot = false;

                    // Check if there is no operator; Must be left side
                    if (operator == NONE)
                        left = exp.getRoot();
                    else
                        right = exp.getRoot();
                }
            }

            // No parenthesis depth anymore
            if (parentDepth == 0) {

                // Check for operator only if within a parenthesis
                if ("^v>-|:".indexOf(chr) != -1) {

                    // Check if operator does not exist yet
                    if (operator == NONE) {
                        if (chr == '^')
                            operator = LogicNode.AND;
                        else if (chr == 'v')
                            operator = LogicNode.OR;
                        else if (chr == '>')
                            operator = LogicNode.IMPLIES;
                        else if (chr == '-')
                            operator = LogicNode.BICONDITIONAL;
                        else if (chr == '|')
                            operator = LogicNode.NAND;
                        else if (chr == ':')
                            operator = LogicNode.NOR;
                    }

                    // Operator exists; String of logical expressions exists
                    // Make the left, operator, right into the left expression
                    else {

                        left = new LogicNode(
                            left,
                            operator,
                            right,
                            hasNot,
                            operatorType
                        );

                        if (chr == '^')
                            operator = LogicNode.AND;
                        else if (chr == 'v')
                            operator = LogicNode.OR;
                        else if (chr == '>')
                            operator = LogicNode.IMPLIES;
                        else if (chr == '-')
                            operator = LogicNode.BICONDITIONAL;
                        else if (chr == '|')
                            operator = LogicNode.NAND;
                        else if (chr == ':')
                            operator = LogicNode.NOR;
                    
                        right = null;
                        hasNot = false;
                    }
                }
            }

            // Check if the value is an integer; We can't have those
            if ("0123456789".indexOf(chr) != -1)
                throw new InvalidExpressionException("You cannot use a number as a logical variable.");
            
            // Check for variable only if not within parentheses
            if (validVars.indexOf(chr) != -1 && chr != 'v') {

                // See if there is a ~ (NOT) operator directly in front of the variable
                if (i > 0) {
                    if (expression.charAt(i - 1) == '~')
                        charHasNot = true;
                    else
                        charHasNot = false;
                }

                // Check if there is no operator; Must be left side
                if (operator == NONE) {
                    left = new LogicVar(
                        "" + chr,
                        charHasNot
                    );
                }

                // There is an operator; Must be right side
                else {
                    right = new LogicVar(
                        "" + chr,
                        charHasNot
                    );
                }

                charHasNot = false;

                // Add variables if not in list already
                if (expVariables.indexOf("" + chr) == -1)
                    expVariables.add("" + chr);
            }
        }

        // Check if there are an unbalanced amount of parentheses
        if (parentDepth != 0)
            throw new UnbalancedParenthesesException("You have a missing parenthesis somewhere.");
        
        // Sort the variables
        expVariables.sort(null);

        // Check if the expression is a single expression wrapped in parentheses
        if (operator == NONE && right == null && expVariables.size() > 1) {
            hasNot = ((LogicNode) left).hasNot();
            operator = ((LogicNode) left).getOperatorInt();
            operatorType = ((LogicNode) left).getOperatorType();
            right = ((LogicNode) left).getRight();
            left = ((LogicNode) left).getLeft();
        }

        if (left != null && operator == NONE && right == null) {
            return new Expression(
                left,
                expVariables,
                true
            );
        }

        return new Expression(
            new LogicNode(
                left, operator, right, hasNot, operatorType
            ),
            expVariables,
            false
        );

    }

    /**
     * Parses a logical expression.
     *
     * @param expression The logical expression to parse
     * @return Expression
     */
    public static Expression parseExpression(String expression) {
        return parseExpression(expression, false, NONE);
    }
}