package ch.nova_omnia.lernello.security;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

/**
 * Utility class for JWT operations for generating and validating tokens.
 */
@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String jwtSecret;
    private SecretKey key;
    @Value("${jwt.expiration}")
    private int jwtExpirationMs;

    /**
     * Initializes the key for the JWT operations.
     */
    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Returns the expiration time of the JWT token.
     *
     * @return The expiration time.
     */
    public Duration getExpirationTime() {
        return Duration.ofMillis(jwtExpirationMs);
    }

    /**
     * Generates a JWT token for a user.
     *
     * @param username The username of the user.
     * @return The generated token.
     */
    public String generateToken(String username) {
        return Jwts.builder().setSubject(username).setIssuedAt(new Date()).setExpiration(new Date((new Date()).getTime() + jwtExpirationMs)).signWith(key, SignatureAlgorithm.HS256).compact();
    }

    /**
     * Extracts the username from a JWT token.
     *
     * @param token The token to extract the username from.
     * @return The username.
     */
    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * Validates a JWT token.
     *
     * @param token The token to validate.
     * @return Whether the token is valid.
     */
    public boolean validateJwtToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException e) {
            //TODO add logging here
        } catch (MalformedJwtException e) {
            //TODO add logging here
        } catch (ExpiredJwtException e) {
            //TODO add logging here
        } catch (UnsupportedJwtException e) {
            //TODO add logging here
        } catch (IllegalArgumentException e) {
            //TODO add logging here
        }
        return false;
    }
}
