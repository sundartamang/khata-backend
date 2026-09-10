package khata_backend.com.exception;

/**
 * Base exception for application-specific runtime errors.
 *
 * All custom application exceptions should extend this class
 * either directly or indirectly.
 */
public class ApplicationException extends RuntimeException {

    /**
     * Creates an application exception with the specified message.
     *
     * @param message description of the error
     */
    public ApplicationException(String message) {
        super(message);
    }

    /**
     * Creates an application exception with the specified message and cause.
     *
     * @param message description of the error
     * @param cause underlying cause of the exception
     */
    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }
}