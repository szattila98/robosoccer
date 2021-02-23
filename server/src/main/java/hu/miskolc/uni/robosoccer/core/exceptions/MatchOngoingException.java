package hu.miskolc.uni.robosoccer.core.exceptions;

/**
 * Thrown when somebody wants to change his ready status, but the match is already ongoing.
 *
 * @author Tamás Sólyom
 */
public class MatchOngoingException extends Exception {

    private static final String ERROR_MSG = "The match is already ongoing!";

    public MatchOngoingException() {
        super(ERROR_MSG);
    }

    public MatchOngoingException(String message) {
        super(message);
    }

    public MatchOngoingException(String message, Throwable cause) {
        super(message, cause);
    }

    public MatchOngoingException(Throwable cause) {
        super(cause);
    }

    public MatchOngoingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
