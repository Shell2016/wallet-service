package io.ylab.wallet.in.filter;

import io.ylab.wallet.domain.exception.AuthException;
import io.ylab.wallet.domain.security.JwtHandler;
import io.ylab.wallet.in.util.JsonHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

/**
 * Filter that verifies user jwt token if not public path.
 */
@RequiredArgsConstructor
public class AuthenticationFilter extends HttpFilter {

    private static final String BEARER_PREFIX = "Bearer ";
    private JwtHandler jwtHandler;

    /**
     * Initializes jwtHandler for verifying token.
     * Spring cannot manage filter like ordinary component and cannot automatically inject beans to it.
     *
     * @param config the <code>FilterConfig</code> object that contains configuration information for this filter
     *
     */
    @Override
    public void init(FilterConfig config) {
        var context =
                WebApplicationContextUtils.getRequiredWebApplicationContext(config.getServletContext());
        jwtHandler = context.getBean(JwtHandler.class);
    }

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
