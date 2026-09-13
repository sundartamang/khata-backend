package khata_backend.com.common;

import org.springframework.http.ResponseEntity;

/**
 * Utility class for building standardized API responses.
 */
public final class ResponseBuilder {

    private ResponseBuilder() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Builds a ResponseEntity from a BaseResponse.
     *
     * @param response the API response
     * @param <T> type of the response data
     * @return ResponseEntity containing the response
     */
    public static <T> ResponseEntity<BaseResponse<T>> build(
            BaseResponse<T> response
    ) {
        return ResponseEntity
                .status(response.getCode())
                .body(response);
    }

}