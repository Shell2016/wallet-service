package io.ylab.wallet.in.servlet;

import io.ylab.wallet.domain.dto.UserRequest;
import io.ylab.wallet.domain.dto.UserResponse;
import io.ylab.wallet.domain.service.UserService;
import io.ylab.wallet.in.controller.RegistrationServlet;
import io.ylab.wallet.in.util.JsonHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.PrintWriter;

import static org.mockito.Mockito.*;

class RegistrationServletTest {

    private static final String USER1_USERNAME = "user1";
    private static final String PASSWORD = "123456";
    private static final UserRequest USER_REQUEST_1 = UserRequest.builder()
            .username(USER1_USERNAME)
            .password(PASSWORD)
            .build();
    private static final UserRequest USER_REQUEST_2 = UserRequest.builder()
            .username(USER1_USERNAME)
            .password("123")
            .build();
    private static final long USER1_ID = 1L;
    private static final UserResponse USER_RESPONSE_1 = UserResponse.builder()
            .username(USER1_USERNAME)
            .id(USER1_ID)
            .build();
    private static final String MOCK_RESPONSE_STRING = "json response";
    private static final String INVALID_PASSWORD_ERROR_STRING = "Поле password должно быть не менее 6 символов!";
    private final UserService userService = mock(UserService.class);
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final PrintWriter writer = mock(PrintWriter.class);
    private final JsonHelper jsonHelper = mock(JsonHelper.class);
    private final RegistrationServlet registrationServlet = new RegistrationServlet(userService, jsonHelper);

    @Test
    @DisplayName("Create user success if valid request")
    void createUserValidRequest() throws IOException {
        when(jsonHelper.getRequestBody(request, UserRequest.class)).thenReturn(USER_REQUEST_1);
        when(userService.createUser(USER_REQUEST_1)).thenReturn(USER_RESPONSE_1);
        when(response.getWriter()).thenReturn(writer);
        when(jsonHelper.buildJsonFromObject(USER_RESPONSE_1)).thenReturn(MOCK_RESPONSE_STRING);

        registrationServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_CREATED);
        verify(writer).write(MOCK_RESPONSE_STRING);
    }

    @Test
    @DisplayName("Create user fails if user exists")
    void createUserIfUserExists() throws IOException {
        when(jsonHelper.getRequestBody(request, UserRequest.class)).thenReturn(USER_REQUEST_1);
        when(userService.createUser(USER_REQUEST_1)).thenThrow(new RuntimeException());
        when(response.getWriter()).thenReturn(writer);
        when(jsonHelper.buildErrorResponse(null, HttpServletResponse.SC_BAD_REQUEST))
                .thenReturn(MOCK_RESPONSE_STRING);

        registrationServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(writer).write(MOCK_RESPONSE_STRING);
    }

    @Test
    @DisplayName("Create user fails if invalid input password")
    void createUserIfInvalidInputPassword() throws IOException {
        when(jsonHelper.getRequestBody(request, UserRequest.class)).thenReturn(USER_REQUEST_2);
        when(response.getWriter()).thenReturn(writer);
        when(jsonHelper.buildErrorResponse(INVALID_PASSWORD_ERROR_STRING, HttpServletResponse.SC_BAD_REQUEST))
                .thenReturn(MOCK_RESPONSE_STRING);

        registrationServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(writer).write(MOCK_RESPONSE_STRING);
    }

    @Test
    @DisplayName("Create user fails if invalid input format (wrong fields/fieldnames)")
    void createUserIfInvalidInputFormat() throws IOException {
        when(jsonHelper.getRequestBody(request, UserRequest.class)).thenThrow(new RuntimeException());
        when(response.getWriter()).thenReturn(writer);
        when(jsonHelper.buildErrorResponse(null, HttpServletResponse.SC_BAD_REQUEST))
                .thenReturn(MOCK_RESPONSE_STRING);

        registrationServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(writer).write(MOCK_RESPONSE_STRING);
    }
}