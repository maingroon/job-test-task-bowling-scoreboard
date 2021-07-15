package ua.casten.bowling.exception;

public class BowlingException extends Exception {
    public BowlingException(String message) {
        super(message);
    }

    public BowlingException(String message, Throwable cause) {
        super(message, cause);
    }
}
