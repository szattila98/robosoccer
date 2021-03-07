package hu.miskolc.uni.robosoccer.core.exceptions;

public class MatchNotGoingException extends Exception {

    private static final String ERROR_MSG = "The match is pending!";

    public MatchNotGoingException() {
        super(ERROR_MSG);
    }

    public MatchNotGoingException(String message) {
        super(message);
    }

    public MatchNotGoingException(String message, Throwable cause) {
        super(message, cause);
    }

    public MatchNotGoingException(Throwable cause) {
        super(cause);
    }

    public MatchNotGoingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }


}
