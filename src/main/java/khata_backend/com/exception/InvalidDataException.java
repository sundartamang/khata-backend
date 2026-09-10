package khata_backend.com.exception;

/**
 * Exception thrown when supplied data is invalid.
 */
public class InvalidDataException extends ApplicationException {

    /**
     * Creates an invalid data exception with the specified message.
     *
     * @param message description of the validation error
     */
    public InvalidDataException(String message) {
        super(message);
    }

    /**
     * Creates an invalid data exception with the specified message and cause.
     *
     * @param message description of the validation error
     * @param cause underlying cause of the exception
     */
    public InvalidDataException(String message, Throwable cause) {
        super(message, cause);
    }
}