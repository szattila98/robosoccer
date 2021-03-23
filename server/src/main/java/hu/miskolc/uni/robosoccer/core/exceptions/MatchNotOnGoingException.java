package hu.miskolc.uni.robosoccer.core.exceptions;

/**
 * Thrown when the match has not started and a user wants to take some action.
 *
 * @author Tamás Sólyom
 */
public class MatchNotOnGoingException extends Exception {

    private static final String ERROR_MSG = "The match is pending!";

    public MatchNotOnGoingException() {
        super(ERROR_MSG);
    }

    public MatchNotOnGoingException(String message) {
        super(message);
    }

    public MatchNotOnGoingException(String message, Throwable cause) {
        super(message, cause);
    }

    public MatchNotOnGoingException(Throwable cause) {
        super(cause);
    }

    public MatchNotOnGoingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }


}
