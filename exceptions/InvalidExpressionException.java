package exceptions;

/**
 * An Exception that is thrown whenever there is an invalid expression given
 */
public class InvalidExpressionException extends RuntimeException {
    public static final long serialVersionUID = 90505L;

    public InvalidExpressionException(String message) {
        super(message);
    }
}
