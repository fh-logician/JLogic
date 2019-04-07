package exceptions;

/**
 * An Exception that is thrown whenever there are unbalanced parentheses in an expression.
 */
public class UnbalancedParenthesesException extends RuntimeException {
    public static final long serialVersionUID = 211605L;

    public UnbalancedParenthesesException(String message) {
        super(message);
    }
}
