package khata_backend.com.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a standardized API error response.
 *
 * @param <T> type of the error data
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ErrorResponse<T> {

    /**
     * HTTP status code associated with the error.
     */
    private int code;

    /**
     * Human-readable description of the error.
     */
    private String message;

    /**
     * Additional data associated with the error.
     */
    private T data;

    /**
     * Additional information about the error.
     */
    private Object details;
}