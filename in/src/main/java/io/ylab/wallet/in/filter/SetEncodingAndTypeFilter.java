package io.ylab.wallet.in.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Filter that sets UTF-8 encoding and 'application/json' content type.
 */
@WebFilter("/*")
public class SetEncodingAndTypeFilter extends HttpFilter {

    @Override
    public void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        req.setCharacterEncoding(StandardCharsets.UTF_8.name());
        res.setCharacterEncoding(StandardCharsets.UTF_8.name());
        res.setContentType("application/json");
        chain.doFilter(req, res);
    }
}
