package hu.miskolc.uni.robosoccer.core.exceptions;

/**
 * Thrown when the kick is not allowed.
 *
 * @author Tamás Sólyom
 */
public class KickNotAllowedException extends Exception {

    private static final String ERROR_MSG = "Kick is not allowed, the user does not control this player or nobody has the ball!";

    public KickNotAllowedException() {
        super(ERROR_MSG);
    }

    public KickNotAllowedException(String message) {
        super(message);
    }

    public KickNotAllowedException(String message, Throwable cause) {
        super(message, cause);
    }

    public KickNotAllowedException(Throwable cause) {
        super(cause);
    }

    public KickNotAllowedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
