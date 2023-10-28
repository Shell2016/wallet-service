package io.ylab.wallet.domain.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.ylab.wallet.domain.exception.AuthException;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Validates received jwt token.
 */
@Setter
@Component
public class JwtHandler {

    /**
     * Token secret from application.yml.
     */
    @Value("${jwt.secret}")
    private String secret;
    /**
     * Verifies if token is not expired and compares id from claims with id from path.
     * Throws exceptions if token expired or if ids is different.
     *
     * @param token
     * @param userId
     */
    public void verify(String token, String userId) {
        Claims claims = verifyAndGetClaimsFromToken(token);
        if (!Objects.equals(userId, claims.getSubject())) {
            throw new AuthException("Вы не авторизованы! Доступ запрещен!");
        }
    }

    /**
     * Extracts claims from token and verifies key and expiration.
     *
     * @param token to extract
     * @return extracted claims
     */
    private Claims verifyAndGetClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret)))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
