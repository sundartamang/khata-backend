package khata_backend.com.authentication.user.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtTokenService {

    /*
     * JWT expiration time in milliseconds.
     *
     * Value comes from application.properties:
     *
     * jwt.expiration=18000000
     *
     * 18,000,000 ms = 5 hours
     */
    private final long jwtExpiration;


    /*
     * JWT secret key.
     *
     * IMPORTANT:
     * Never hard-code the secret directly in Java code.
     *
     * The value should come from application.properties,
     * an environment variable, or a secrets manager.
     */
    private final String secret;


    /*
     * Inject the JWT secret and expiry time from application.properties.
     *
     */
    public JwtTokenService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long jwtExpiration
    ) {
        this.secret = secret;
        this.jwtExpiration = jwtExpiration;
    }


    /*
     * Get username from JWT.
     *
     * The username is stored as the JWT "subject".
     */
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }


    /*
     * Get expiration date from JWT.
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }


    /*
     * Generic method for retrieving any claim from the JWT.
     *
     * Example:
     *
     * getClaimFromToken(token, Claims::getSubject)
     * getClaimFromToken(token, Claims::getExpiration)
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }


    /*
     * Generate JWT for an authenticated user.
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return generateToken(
                claims,
                userDetails.getUsername()
        );
    }


    /*
     * Validate JWT against the user.
     *
     * Token is considered valid when:
     *
     * 1. Username in token matches the user.
     * 2. Token has not expired.
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        String username = getUsernameFromToken(token);
        return username.equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }


    /*
     * Create the actual JWT.
     */
    private String generateToken(Map<String, Object> claims, String subject) {
        Date issuedAt = new Date();
        Date expiration = new Date(
                issuedAt.getTime() + jwtExpiration
        );

        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(issuedAt)
                .expiration(expiration)
                .signWith(getSigningKey())
                .compact();
    }


    /*
     * Parse and verify the JWT.
     *
     * verifyWith() verifies the token signature using
     * our secret key.
     *
     * If the token is:
     *
     * - malformed
     * - tampered with
     * - signed with another key
     *
     * JJWT will throw a JwtException.
     */
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


    /*
     * Check whether the JWT has expired.
     */
    private boolean isTokenExpired(String token) {
        Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }


    /*
     * Convert the configured secret into a SecretKey.
     *
     * The secret is expected to be Base64 encoded.
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = java.util.Base64.getDecoder().decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}