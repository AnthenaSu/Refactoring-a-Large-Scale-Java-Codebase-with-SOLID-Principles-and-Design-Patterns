package dungeonmania.exceptions;

/** Thrown when a player attempts to interact with something they can't interact with. */
public class InvalidActionException extends Exception {
    public InvalidActionException(String message) {
        super(message);
    }
}
