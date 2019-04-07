package exceptions;

/**
 * An Exception that is thrown whenever there is a variable in an expression
 * that doesn't exist in a {@link TruthValue} object
 */
public class MissingTruthValueException extends RuntimeException {
    public static final long serialVersionUID = 13202205L;

    public MissingTruthValueException(String message) {
        super(message);
    }
}
