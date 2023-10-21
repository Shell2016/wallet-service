package io.ylab.wallet.in.filter;

import io.ylab.wallet.domain.exception.NotAuthorizedException;
import io.ylab.wallet.domain.security.JwtHandler;
import io.ylab.wallet.in.util.*;
import io.ylab.wallet.in.validation.ValidationUtils;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Set;

import static io.ylab.wallet.in.util.UrlPath.*;
import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

/**
 * Filter that verifies user jwt token if not public path.
 */
@WebFilter("/*")
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationFilter extends HttpFilter {

    private static final Set<String> PUBLIC_PATHS = Set.of(AUTH, REGISTRATION);
    private static final String BEARER_PREFIX = "Bearer ";
    private JsonHelper jsonHelper;

    @Override
    public void init() {
        jsonHelper = DependencyContainer.getJsonHelper();
    }

    @Override
    public void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        if (PUBLIC_PATHS.contains(req.getRequestURI())) {
            chain.doFilter(req, res);
            return;
        }
        try {
            String token = getTokenFromRequest(req);
            long id = getIdFromRequestPath(req);
            JwtHandler.verify(token, String.valueOf(id));
        } catch (Exception e) {
            res.setContentType("application/json");
            res.setCharacterEncoding(StandardCharsets.UTF_8.name());
            res.setStatus(SC_UNAUTHORIZED);
            res.getWriter().write(jsonHelper.buildErrorResponse(e.getMessage(), SC_UNAUTHORIZED));
            return;
        }
        chain.doFilter(req, res);
    }

    private long getIdFromRequestPath(HttpServletRequest request) {
        ValidationUtils.validatePath(request);
        String path = request.getRequestURI();
        String[] pathParts = path.split("/");
        String idString = pathParts[2];
        return Long.parseLong(idString);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null) {
            throw new NotAuthorizedException("Вы не авторизованы! Доступ запрещен!");
        }
        return authorizationHeader.substring(BEARER_PREFIX.length());
    }
}
