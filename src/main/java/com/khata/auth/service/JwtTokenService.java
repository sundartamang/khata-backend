package com.khata.auth.service;

import io.jsonwebtoken.io.Decoders;

import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

@Service
public class JwtTokenService {

    // Token validity in seconds
    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60 * 1000;

    // Securely generate a secret key (recommend external configuration for production)
    private String secretKey = "";

    public JwtTokenService(){
        try{
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
            SecretKey sk = keyGen.generateKey();
            secretKey =  Base64.getEncoder().encodeToString(sk.getEncoded());
        }catch (NoSuchAlgorithmException e){
            throw new RuntimeException(e);
        }
    }

    // Retrieve username from JWT token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    //get roles from token
    public List<String> getRolesFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return claims.get("roles", List.class);
    }

    // Retrieve expiration date from JWT token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    // Generate token for user
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        Collection<? extends GrantedAuthority> roles = userDetails.getAuthorities();
        claims.put("roles", roles.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
        return buildToken(claims, userDetails.getUsername());
    }

    // Validate token
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !hasTokenExpired(token));
    }

    // Generate token logic
    private String buildToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
                .signWith(decodeSecretKey())
                .compact();
    }

    // Retrieve any information from token using the secret key
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(decodeSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Check if the token has expired
    private Boolean hasTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private SecretKey decodeSecretKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

