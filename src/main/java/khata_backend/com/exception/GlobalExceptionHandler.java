package khata_backend.com.exception;

import khata_backend.com.common.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for the application.
 *
 * <p>Handles application-specific and common exceptions in one place
 * and converts them into consistent API responses.</p>
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles duplicate resource exceptions.
     *
     * <p>Returns HTTP 409 (Conflict) when an attempt is made to create
     * a resource that already exists.</p>
     *
     * @param ex the duplicate resource exception
     * @return a response containing the conflict status and error message
     */
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<BaseResponse<Void>> handleDuplicateResourceException(
            DuplicateResourceException ex) {

        log.warn("DuplicateResourceException: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(BaseResponse.<Void>builder()
                        .code(HttpStatus.CONFLICT.value())
                        .message(ex.getMessage())
                        .build());
    }

    /**
     * Handles resource not found exceptions.
     *
     * <p>Returns HTTP 404 (Not Found) when the requested resource
     * does not exist.</p>
     *
     * @param ex the resource not found exception
     * @return a response containing the not found status and error message
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<BaseResponse<Void>> handleResourceNotFoundException(
            ResourceNotFoundException ex) {

        log.warn("ResourceNotFoundException: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(BaseResponse.<Void>builder()
                        .code(HttpStatus.NOT_FOUND.value())
                        .message(ex.getMessage())
                        .build());
    }

    /**
     * Handles general application exceptions.
     *
     * <p>Returns HTTP 400 (Bad Request) when an application-specific
     * error occurs that does not have a more specific exception handler.</p>
     *
     * @param ex the application exception
     * @return a response containing the bad request status and error message
     */
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<BaseResponse<Void>> handleApplicationException(
            ApplicationException ex) {

        log.warn("ApplicationException: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(BaseResponse.<Void>builder()
                        .code(HttpStatus.BAD_REQUEST.value())
                        .message(ex.getMessage())
                        .build());
    }

    /**
     * Handles invalid data exceptions.
     *
     * <p>Returns HTTP 400 (Bad Request) when supplied data is invalid.</p>
     *
     * @param ex the invalid data exception
     * @return a response containing the bad request status and error message
     */
    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<BaseResponse<Void>> handleInvalidDataException(
            InvalidDataException ex) {

        log.warn("InvalidDataException: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(BaseResponse.<Void>builder()
                        .code(HttpStatus.BAD_REQUEST.value())
                        .message(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<BaseResponse<Void>> handleAuthenticationFailedException(AuthenticationFailedException ex) {
        log.warn("AuthenticationFailedException: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(BaseResponse.<Void>builder()
                        .code(HttpStatus.UNAUTHORIZED.value())
                        .message(ex.getMessage())
                        .build());
    }

    /**
     * Handles validation errors from request body validation.
     *
     * <p>Collects all field-level validation errors and returns them
     * as a map where the key is the field name and the value is
     * the validation error message.</p>
     *
     * @param ex the validation exception containing field errors
     * @return a response containing the validation errors
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<Map<String, String>>> handleValidationException(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        log.warn("Validation error: {}", errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(BaseResponse.<Map<String, String>>builder()
                        .code(HttpStatus.BAD_REQUEST.value())
                        .message("Validation failed")
                        .data(errors)
                        .build());
    }

    /**
     * Handles unexpected exceptions that are not handled by other handlers.
     *
     * <p>Returns HTTP 500 (Internal Server Error) with a generic message
     * to avoid exposing internal application details to the client.</p>
     *
     * @param ex the unexpected exception
     * @return a response containing the internal server error status
     *         and a generic error message
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<Void>> handleGenericException(
            Exception ex) {

        log.error("Unhandled exception: ", ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(BaseResponse.<Void>builder()
                        .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message("An unexpected error occurred. Please try again later.")
                        .build());
    }
}