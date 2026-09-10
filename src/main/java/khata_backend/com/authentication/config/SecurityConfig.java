package khata_backend.com.authentication.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    /*
     * Endpoints that can be accessed without authentication.
     *
     * Example:
     * POST /api/auth/login
     * POST /api/auth/register
     */
    private static final String[] PUBLIC_ENDPOINTS = {
            "/api/auth/**"
    };

    /*
     * Our custom JWT filter.
     *
     * This filter checks the Authorization header,
     * validates the JWT, and authenticates the user.
     */
    private final JwtAuthenticationFilter jwtAuthenticationFilter;


    /*
     * Main Spring Security configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                /*
                 * Disable CSRF because this is a stateless REST API
                 * using JWT authentication.
                 */
                .csrf(AbstractHttpConfigurer::disable)
                /*
                 * JWT authentication is stateless.
                 *
                 * The server does not store authentication
                 * information inside an HTTP session.
                 */
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )
                /*
                 * Define which endpoints require authentication.
                 */
                .authorizeHttpRequests(auth -> auth
                        /*
                         * Authentication endpoints are public.
                         */
                        .requestMatchers(PUBLIC_ENDPOINTS)
                        .permitAll()

                        /*
                         * Every other endpoint requires
                         * an authenticated user.
                         */
                        .anyRequest()
                        .authenticated()
                )
                /*
                 * Run our JWT filter before Spring Security's
                 * username/password authentication filter.
                 */
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )
                .build();
    }


    /*
     * AuthenticationProvider is responsible for verifying
     * username/password credentials.
     *
     * DaoAuthenticationProvider uses:
     *
     * UserDetailsService
     *        ↓
     * Load user from database
     *        ↓
     * PasswordEncoder
     *        ↓
     * Verify password
     */
    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }


    /*
     * BCrypt is used to securely hash passwords.
     *
     * The password stored in PostgreSQL should NEVER
     * be the user's plain-text password.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }


    /*
     * AuthenticationManager is the main entry point
     * for username/password authentication.
     *
     * AuthService will use this during login.
     *
     * AuthenticationManager
     *        ↓
     * AuthenticationProvider
     *        ↓
     * UserDetailsService + PasswordEncoder
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
