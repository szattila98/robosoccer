package hu.miskolc.uni.robosoccer.core.exceptions;

public class UserNotReadyException extends Exception {

    private static final String ERROR_MSG = "Not every user is ready!";

    public UserNotReadyException() {
        super(ERROR_MSG);
    }

    public UserNotReadyException(String message) {
        super(message);
    }

    public UserNotReadyException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNotReadyException(Throwable cause) {
        super(cause);
    }

    public UserNotReadyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
