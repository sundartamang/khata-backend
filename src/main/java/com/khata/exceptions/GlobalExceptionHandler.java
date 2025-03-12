package com.khata.exceptions;

import com.khata.auth.exceptions.EmailAlreadyExistsException;
import com.khata.payload.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;


/**
 * Global exception handler for the application that catches various exceptions
 * and provides a standardized error response.
 * <p>
 * This class handles the following exceptions:
 * <ul>
 *     <li>{@link ResourceNotFoundException} - Handles situations where a resource is not found in the system.</li>
 *     <li>{@link MethodArgumentNotValidException} - Handles validation errors in method arguments, returning
 *         field-specific error messages.</li>
 *     <li>{@link ApiException} - Handles custom API exceptions thrown by the application.</li>
 *     <li>{@link EmailAlreadyExistsException} - Handles cases where the email already exists in the system.</li>
 * </ul>
 * <p>
 * Each exception is caught and an appropriate HTTP status code is returned with a standardized error message
 * encapsulated in an {@link ApiResponse}.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles {@link ResourceNotFoundException} and returns a 404 status code with a standardized error response.
     *
     * @param ex the exception that was thrown
     * @return a {@link ResponseEntity} containing an {@link ApiResponse} with the error message and HTTP status
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> resourceNotFoundExceptionHandler(ResourceNotFoundException ex) {
        String message = ex.getMessage();
        ApiResponse apiResponse = new ApiResponse(message, false);
        return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles {@link MethodArgumentNotValidException} and returns a map of field errors with a 400 status code.
     * The field errors contain specific validation messages for each field that failed validation.
     *
     * @param ex the exception that was thrown
     * @return a {@link ResponseEntity} containing a map of field names and their corresponding validation error messages
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgsNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> responseError = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(
                (error) -> {
                    String fieldName = ((FieldError) error).getField();
                    String message = error.getDefaultMessage();
                    responseError.put(fieldName, message);
                });
        return new ResponseEntity<Map<String, String>>(responseError, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles {@link ApiException} and returns a 400 status code with a standardized error response.
     *
     * @param ex the exception that was thrown
     * @return a {@link ResponseEntity} containing an {@link ApiResponse} with the error message and HTTP status
     */
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse> handleApiException(ApiException ex) {
        String message = ex.getMessage();
        ApiResponse apiResponse = new ApiResponse(message, false);
        return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles {@link EmailAlreadyExistsException} and returns a 400 status code with a standardized error response.
     *
     * @param ex the exception that was thrown
     * @return a {@link ResponseEntity} containing an {@link ApiResponse} with the error message and HTTP status
     */
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiResponse> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
        String message = ex.getMessage();
        ApiResponse apiResponse = new ApiResponse(message, false);
        return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.BAD_REQUEST);
    }
}