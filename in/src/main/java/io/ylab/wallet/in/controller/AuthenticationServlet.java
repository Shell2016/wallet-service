package io.ylab.wallet.in.controller;

import io.ylab.wallet.domain.dto.UserRequest;
import io.ylab.wallet.domain.security.SecurityService;
import io.ylab.wallet.domain.security.TokenDetails;
import io.ylab.wallet.in.util.DependencyContainer;
import io.ylab.wallet.in.util.JsonHelper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.IOException;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static jakarta.servlet.http.HttpServletResponse.SC_OK;

/**
 * Servlet for user authentication.
 */
@WebServlet("/auth")
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationServlet extends HttpServlet {

    private SecurityService securityService;
    private JsonHelper jsonHelper;

    @Override
    public void init() {
        securityService = DependencyContainer.getSecurityService();
        jsonHelper = DependencyContainer.getJsonHelper();
    }

    /**
     * Request must contain request body with fields: username (not empty), password(length >= 6).<br>
     * POST /auth
     *
     * @param req  an {@link HttpServletRequest} object that contains the request the client has made of the servlet
     * @param resp an {@link HttpServletResponse} object that contains the response the servlet sends to the client
     * @throws IOException
     */
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        TokenDetails tokenDetails;
        try {
            UserRequest userRequest = jsonHelper.getRequestBody(req, UserRequest.class);
            tokenDetails = securityService.authenticate(userRequest.username(), userRequest.password());
        } catch (Exception e) {
            resp.setStatus(SC_BAD_REQUEST);
            resp.getWriter().write(jsonHelper.buildErrorResponse(e.getMessage(), SC_BAD_REQUEST));
            return;
        }
        resp.setStatus(SC_OK);
        resp.getWriter().write(jsonHelper.buildJsonFromObject(tokenDetails));
    }
}
