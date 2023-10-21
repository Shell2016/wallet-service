package io.ylab.wallet.domain.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.ylab.wallet.domain.config.PropertiesUtils;
import io.ylab.wallet.domain.exception.AuthException;
import io.ylab.wallet.domain.exception.NotAuthorizedException;

import java.util.Date;
import java.util.Objects;

/**
 * Utility class that validates received jwt token.
 */
public class JwtHandler {

    /**
     * Token secret from application.properties.
     */
    private static final String SECRET = PropertiesUtils.get("jwt.secret");

    private JwtHandler() {
    }

    /**
     * Verifies if token is not expired and compares id from claims with id from path.
     * Throws exceptions if token expired or if ids is different.
     *
     * @param token
     * @param userId
     */
    public static void verify(String token, String userId) {
        Claims claims = getClaimsFromToken(token);
        Date expiration = claims.getExpiration();
        if (expiration.before(new Date())) {
            throw new AuthException("Token expired!");
        }
        if (!Objects.equals(userId, claims.getSubject())) {
            throw new NotAuthorizedException("Вы не авторизованы! Доступ запрещен!");
        }
    }

    /**
     * Extracts claims from token.
     *
     * @param token to extract
     * @return extracted claims
     */
    private static Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET)))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
