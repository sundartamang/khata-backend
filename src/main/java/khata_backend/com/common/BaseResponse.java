package khata_backend.com.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents the standard successful API response structure.
 *
 * @param <T> type of the response data
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class BaseResponse<T> {

    /**
     * HTTP status code associated with the response.
     */
    private int code;

    /**
     * Human-readable response message.
     */
    private String message;

    /**
     * Response payload.
     */
    private T data;

    /**
     * Additional information associated with the response.
     */
    private Object details;
}