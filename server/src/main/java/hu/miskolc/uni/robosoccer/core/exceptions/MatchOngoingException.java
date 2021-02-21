package hu.miskolc.uni.robosoccer.core.exceptions;

public class MatchOngoingException extends Exception {

    private static final String ERROR_MSG = "The match is already in progress!";

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
