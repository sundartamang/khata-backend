package khata_backend.com.authentication.config;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import khata_backend.com.authentication.user.service.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    /*
     * Service responsible for creating, parsing,
     * and validating JWT tokens.
     */
    private final JwtTokenService jwtTokenService;

    /*
     * Used to load the user from the database
     * using the username stored inside the JWT.
     */
    private final UserDetailsService userDetailsService;


    /*
     * This method runs once for every HTTP request.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        /*
         * Get the Authorization header.
         *
         * Expected format:
         *
         * Authorization: Bearer <JWT>
         */
        String authHeader = request.getHeader("Authorization");

        /*
         * If there is no Bearer token,
         * simply continue the request.
         *
         * This allows public endpoints such as
         * /api/auth/login to work normally.
         */
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        /*
         * Remove "Bearer " and keep only the JWT.
         *
         * Example:
         *
         * "Bearer eyJhbGciOiJIUzI1NiJ9..."
         *
         * becomes:
         *
         * "eyJhbGciOiJIUzI1NiJ9..."
         */
        String token = authHeader.substring(7);

        try {
            /*
             * Extract the username from the JWT.
             *
             * JwtTokenService should verify the JWT signature
             * while parsing the token.
             */
            String username = jwtTokenService.getUsernameFromToken(token);
            /*
             * Only authenticate the request if:
             *
             * 1. Username was successfully extracted.
             * 2. There is not already an authenticated user
             *    in the SecurityContext.
             */
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                /*
                 * Load the user from PostgreSQL.
                 *
                 * This gives us the user's:
                 * - username
                 * - password hash
                 * - roles
                 * - authorities
                 * - account status
                 */
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                /*
                 * Validate the JWT against the user.
                 *
                 * This should verify:
                 * - JWT signature
                 * - expiration
                 * - username/subject
                 */
                if (jwtTokenService.validateToken(token, userDetails)) {
                    /*
                     * Create an authenticated Spring Security object.
                     *
                     * Password is null because we don't need
                     * the password after successful login.
                     */
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());
                    /*
                     * Attach request details to the authentication.
                     *
                     * This can be useful for auditing/security purposes.
                     */
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    /*
                     * Store the authenticated user in Spring Security.
                     *
                     * From this point onward, Spring Security considers
                     * the request authenticated.
                     */
                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(authentication);
                }
            }

        } catch (ExpiredJwtException e) {
            /*
             * JWT has expired.
             *
             * We do not authenticate the request.
             */
            logger.debug("JWT token has expired");
        } catch (JwtException | IllegalArgumentException e) {
            /*
             * JWT is malformed, invalid, or cannot be parsed.
             *
             * Do not expose internal JWT details to the client.
             */
            logger.debug("Invalid JWT token");
        }

        /*
         * Continue the request through the remaining
         * Spring Security filters and eventually the controller.
         */
        filterChain.doFilter(request, response);
    }
}