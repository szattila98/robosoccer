package hu.miskolc.uni.robosoccer.core.exceptions;

/**
 * Thrown when the match is full, in this case two players joined.
 *
 * @author Attila Szőke!
 */
public class MatchFullException extends Exception {

    private static final String ERROR_MSG = "The match is full!";

    public MatchFullException() {
        super(ERROR_MSG);
    }

    public MatchFullException(String message) {
        super(message);
    }

    public MatchFullException(String message, Throwable cause) {
        super(message, cause);
    }

    public MatchFullException(Throwable cause) {
        super(cause);
    }

    public MatchFullException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
