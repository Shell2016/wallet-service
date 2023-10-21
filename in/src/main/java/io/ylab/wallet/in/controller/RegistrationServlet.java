package io.ylab.wallet.in.controller;

import io.ylab.wallet.domain.dto.UserRequest;
import io.ylab.wallet.domain.dto.UserResponse;
import io.ylab.wallet.domain.service.UserService;
import io.ylab.wallet.in.util.DependencyContainer;
import io.ylab.wallet.in.util.JsonHelper;
import io.ylab.wallet.in.validation.ValidationUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.IOException;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static jakarta.servlet.http.HttpServletResponse.SC_CREATED;

/**
 * Servlet for user registration.
 */
@WebServlet("/users")
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationServlet extends HttpServlet {

    private UserService userService;
    private JsonHelper jsonHelper;

    @Override
    public void init() {
        userService = DependencyContainer.getUserService();
        jsonHelper = DependencyContainer.getJsonHelper();
    }

    /**
     * Request must contain request body with fields: username (not empty), password(length >= 6).<br>
     * POST /users
     *
     * @param req  an {@link HttpServletRequest} object that contains the request the client has made of the servlet
     * @param resp an {@link HttpServletResponse} object that contains the response the servlet sends to the client
     * @throws IOException
     */
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String responseJson;
        try {
            UserRequest userRequest = jsonHelper.getRequestBody(req, UserRequest.class);
            ValidationUtils.validateUserCreateRequest(userRequest);
            UserResponse userResponse = userService.createUser(userRequest);
            responseJson = jsonHelper.buildJsonFromObject(userResponse);
            resp.setStatus(SC_CREATED);
        } catch (Exception e) {
            responseJson = jsonHelper.buildErrorResponse(e.getMessage(), SC_BAD_REQUEST);
            resp.setStatus(SC_BAD_REQUEST);
        }
        resp.getWriter().write(responseJson);
    }
}
