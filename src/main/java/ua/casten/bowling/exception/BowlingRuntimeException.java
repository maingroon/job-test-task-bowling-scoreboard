package ua.casten.bowling.exception;

public class BowlingRuntimeException extends RuntimeException {
    public BowlingRuntimeException(String message) {
        super(message);
    }

    public BowlingRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
