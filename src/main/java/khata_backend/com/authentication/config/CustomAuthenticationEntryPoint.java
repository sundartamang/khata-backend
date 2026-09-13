package khata_backend.com.authentication.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import khata_backend.com.common.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Handles unauthenticated requests (e.g. missing or invalid JWT).
 * Returns our standard BaseResponse instead of Spring's default error page.
 */
@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        BaseResponse<Void> baseResponse = BaseResponse.<Void>builder()
                .code(HttpStatus.UNAUTHORIZED.value())
                .message("Full authentication is required to access this resource")
                .build();

        objectMapper.writeValue(response.getOutputStream(), baseResponse);
    }
}

