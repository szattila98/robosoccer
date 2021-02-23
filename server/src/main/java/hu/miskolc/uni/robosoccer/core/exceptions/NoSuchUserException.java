package hu.miskolc.uni.robosoccer.core.exceptions;

/**
 * Thrown when there is no user with this sessionId in the joined users list.
 *
 * @author Attila Szőke
 */
public class NoSuchUserException extends Exception {

    private static final String ERROR_MSG = "User with this sessionId is not in match!";

    public NoSuchUserException() {
        super(ERROR_MSG);
    }

    public NoSuchUserException(String message) {
        super(message);
    }

    public NoSuchUserException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchUserException(Throwable cause) {
        super(cause);
    }

    public NoSuchUserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
