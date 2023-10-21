package io.ylab.wallet.in.servlet;

import io.ylab.wallet.domain.dto.UserRequest;
import io.ylab.wallet.domain.security.SecurityService;
import io.ylab.wallet.domain.security.TokenDetails;
import io.ylab.wallet.in.controller.AuthenticationServlet;
import io.ylab.wallet.in.util.JsonHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.PrintWriter;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static org.mockito.Mockito.*;

class AuthenticationServletTest {

    private static final String USER1_USERNAME = "user1";
    private static final String PASSWORD = "123456";
    private static final UserRequest USER_REQUEST_1 = UserRequest.builder()
            .username(USER1_USERNAME)
            .password(PASSWORD)
            .build();
    private static final String MOCK_RESPONSE_STRING = "json response";
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final PrintWriter writer = mock(PrintWriter.class);
    private final JsonHelper jsonHelper = mock(JsonHelper.class);
    private final SecurityService securityService = mock(SecurityService.class);

    private final AuthenticationServlet authenticationServlet = new AuthenticationServlet(securityService, jsonHelper);

    @Test
    @DisplayName("Authentication success")
    void successfulAuthentication() throws IOException {
        when(jsonHelper.getRequestBody(request, UserRequest.class)).thenReturn(USER_REQUEST_1);
        when(securityService.authenticate(USER_REQUEST_1.username(), USER_REQUEST_1.password()))
                .thenReturn(TokenDetails.builder().build());
        when(response.getWriter()).thenReturn(writer);
        when(jsonHelper.buildJsonFromObject(any(TokenDetails.class))).thenReturn(MOCK_RESPONSE_STRING);

        authenticationServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(writer).write(MOCK_RESPONSE_STRING);
    }

    @Test
    @DisplayName("Authentication should fail if invalid credentials")
    void invalidCredentials() throws IOException {
        when(jsonHelper.getRequestBody(request, UserRequest.class)).thenReturn(USER_REQUEST_1);
        when(securityService.authenticate(USER_REQUEST_1.username(), USER_REQUEST_1.password()))
                .thenThrow(new RuntimeException());
        when(response.getWriter()).thenReturn(writer);
        when(jsonHelper.buildErrorResponse(null, SC_BAD_REQUEST))
                .thenReturn(MOCK_RESPONSE_STRING);

        authenticationServlet.doPost(request, response);

        verify(response).setStatus(SC_BAD_REQUEST);
        verify(writer).write(MOCK_RESPONSE_STRING);
    }

    @Test
    @DisplayName("Authentication should fail if invalid input format")
    void invalidInputFormat() throws IOException {
        when(jsonHelper.getRequestBody(request, UserRequest.class)).thenThrow(new RuntimeException());
        when(response.getWriter()).thenReturn(writer);
        when(jsonHelper.buildErrorResponse(null, SC_BAD_REQUEST)).thenReturn(MOCK_RESPONSE_STRING);

        authenticationServlet.doPost(request, response);

        verify(response).setStatus(SC_BAD_REQUEST);
        verify(writer).write(MOCK_RESPONSE_STRING);
    }
}
