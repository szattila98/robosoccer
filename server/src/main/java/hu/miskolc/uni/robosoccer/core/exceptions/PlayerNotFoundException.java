package hu.miskolc.uni.robosoccer.core.exceptions;

/**
 * Thrown when a player with this id does not exist.
 *
 * @author Attila Szőke
 */
public class PlayerNotFoundException extends Exception {

    private static final String ERROR_MSG = "Player with this playerId is not in the match!";

    public PlayerNotFoundException() {
        super(ERROR_MSG);
    }

    public PlayerNotFoundException(String message) {
        super(message);
    }

    public PlayerNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public PlayerNotFoundException(Throwable cause) {
        super(cause);
    }

    public PlayerNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
