package khata_backend.com.exception;

/**
 * Thrown when attempting to create a resource that already exists
 * (e.g., duplicate email during registration).
 */
public class DuplicateResourceException extends ApplicationException {
    public DuplicateResourceException(String message) {
        super(message);
    }
}
