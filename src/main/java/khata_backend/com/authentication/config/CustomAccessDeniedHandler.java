package khata_backend.com.authentication.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import khata_backend.com.common.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Handles requests where the user is authenticated but lacks required authorities/roles.
 * Returns our standard BaseResponse instead of Spring's default error page.
 */
@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        BaseResponse<Void> baseResponse = BaseResponse.<Void>builder()
                .code(HttpStatus.FORBIDDEN.value())
                .message("You do not have permission to access this resource")
                .build();

        objectMapper.writeValue(response.getOutputStream(), baseResponse);
    }
}
