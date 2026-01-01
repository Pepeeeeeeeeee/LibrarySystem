package be.kdg.programming3.exception;

public class BookProcessingException extends RuntimeException {
    public BookProcessingException(String message) {
        super(message);
    }

    public BookProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
