package io.ylab.wallet.domain.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.ylab.wallet.domain.config.PropertiesUtils;
import io.ylab.wallet.domain.entity.User;
import io.ylab.wallet.domain.exception.AuthException;
import io.ylab.wallet.domain.port.output.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;

import java.util.*;

/**
 * Server that authenticates and returns to user {@link TokenDetails} if username and password are correct.
 */
@RequiredArgsConstructor
public class SecurityService {

    private final UserRepository userRepository;

    private String secret = PropertiesUtils.get("jwt.secret");
    private Integer expirationInMinutes = Integer.parseInt(PropertiesUtils.get("jwt.expiration"));
    private String issuer = PropertiesUtils.get("jwt.issuer");

    /**
     * Generates jwt token.
     *
     * @param user retrieved from database.
     * @return TokenDetails
     */
    private TokenDetails generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", user.getUsername());
        return generateToken(claims, String.valueOf(user.getId()));
    }

    private TokenDetails generateToken(Map<String, Object> claims, String subject) {
        long expirationInMillis = expirationInMinutes * 1000 * 60L;
        Date expiresAt = new Date(new Date().getTime() + expirationInMillis);
        return generateToken(expiresAt, claims, subject);
    }

    private TokenDetails generateToken(Date expiresAt, Map<String, Object> claims, String subject) {
        Date createdAt = new Date();
        String token = Jwts.builder()
                .claims(claims)
                .issuer(issuer)
                .subject(subject)
                .issuedAt(createdAt)
                .id(UUID.randomUUID().toString())
                .expiration(expiresAt)
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret)))
                .compact();
        return TokenDetails.builder()
                .token(token)
                .issuedAt(createdAt)
                .expiresAt(expiresAt)
                .build();
    }

    /**
     * Retrieves user with given username from database and verifies password.
     *
     * @param username to verify
     * @param password to verify
     * @return TokenDetails if credentials are correct.
     */
    public TokenDetails authenticate(String username, String password) {
        return userRepository.getByUsername(username)
                .filter(user -> BCrypt.checkpw(password, user.getPassword()))
                .map(user -> generateToken(user).toBuilder()
                        .userId(user.getId())
                        .build())
                .orElseThrow(() -> new AuthException("Неправильный логин или пароль!"));
    }
}
