package io.ylab.wallet.in.filter;

import io.ylab.wallet.domain.exception.AuthException;
import io.ylab.wallet.domain.security.JwtHandler;
import io.ylab.wallet.in.util.JsonHelper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

/**
 * Filter that verifies user jwt token if not public path.
 */
@RequiredArgsConstructor
@Component
public class AuthenticationFilter extends HttpFilter {

    private static final String BEARER_PREFIX = "Bearer ";
    private final JwtHandler jwtHandler;

    @Override
    public void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        if (!req.getRequestURI().startsWith("/users/")) {
            chain.doFilter(req, res);
            return;
        }
        try {
            String token = getTokenFromRequest(req);
            long id = getIdFromRequestPath(req);
            jwtHandler.verify(token, String.valueOf(id));
        } catch (Exception e) {
            res.setContentType("application/json");
            res.setCharacterEncoding(StandardCharsets.UTF_8.name());
            res.setStatus(SC_UNAUTHORIZED);
            res.getWriter().write(JsonHelper.buildErrorResponse(e.getMessage(), SC_UNAUTHORIZED));
            return;
        }
        chain.doFilter(req, res);
    }

    private long getIdFromRequestPath(HttpServletRequest request) {
        String path = request.getRequestURI();
        String[] pathParts = path.split("/");
        String idString = pathParts[2];
        return Long.parseLong(idString);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null) {
            throw new AuthException("Вы не авторизованы! Доступ запрещен!");
        }
        return authorizationHeader.substring(BEARER_PREFIX.length());
    }
}
