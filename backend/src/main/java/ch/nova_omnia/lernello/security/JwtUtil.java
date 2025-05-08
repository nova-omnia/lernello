package ch.nova_omnia.lernello.security;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;
import java.util.UUID;

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
    @Value("${jwt.refreshToken.expiration}")
    private int jwtRefreshExpirationMs;

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
     * Returns the expiration time of the refresh token.
     *
     * @return The expiration time.
     */
    public Duration getRefreshExpirationTime() {
        return Duration.ofMillis(jwtRefreshExpirationMs);
    }

    /**
     * Generates a JWT token for a user.
     *
     * @param username The username of the user.
     * @return The generated token.
     */
    public String generateToken(UUID uuid) {
        return Jwts.builder().setSubject(uuid.toString()).setIssuedAt(new Date()).setExpiration(new Date((new Date()).getTime() + jwtExpirationMs)).signWith(key, SignatureAlgorithm.HS256).compact();
    }

    public String generateRefreshToken(UUID jitUuid) {
        return Jwts.builder().setSubject(jitUuid.toString()).setIssuedAt(new Date()).setExpiration(new Date((new Date()).getTime() + jwtRefreshExpirationMs)).signWith(key, SignatureAlgorithm.HS256).compact();
    }

    /**
     * Extracts the userId from a JWT token.
     *
     * @param token The token to extract the userId from.
     * @return The userId.
     */
    public UUID getUserIdFromToken(String token) {
        String uuidString = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
        return UUID.fromString(uuidString);
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
