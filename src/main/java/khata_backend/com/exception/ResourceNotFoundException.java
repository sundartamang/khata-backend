package khata_backend.com.exception;
/**
 * Thrown when a requested resource cannot be found in the system.
 */
public class ResourceNotFoundException extends ApplicationException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
