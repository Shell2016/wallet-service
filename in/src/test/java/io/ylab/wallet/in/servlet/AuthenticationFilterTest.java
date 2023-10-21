package io.ylab.wallet.in.servlet;

import io.ylab.wallet.in.filter.AuthenticationFilter;
import io.ylab.wallet.in.util.JsonHelper;
import io.ylab.wallet.in.util.UrlPath;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.io.PrintWriter;

import static org.mockito.Mockito.*;

class AuthenticationFilterTest {

    private static final String VALID_TOKEN_STRING_FOR_USER24 =
            "Bearer eyJhbGciOiJIUzM4NCJ9.eyJ1c2VybmFtZSI6InVzZXIyIiwiaXNzIjoie" +
            "WxhYiIsInN1YiI6IjI0IiwiaWF0IjoxNjk4MDc0NzI0LCJqdGkiOiJiZjRlNTgzMS1iZjF" +
            "jLTQwNzktODZjMC04YTFmOTQ4OTNiZmUiLCJleHAiOjE2OTk4NzQ3MjR9.LgimFTNVW4veqWW" +
            "wQhgH99RUt5Uv15iNP3W0Jwh7DCYTQBYa5KS-Tqa4ZcIalHxv";
    private static final String AUTHORIZATION_ERROR_MESSAGE = "Вы не авторизованы! Доступ запрещен!";
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final FilterChain chain = mock(FilterChain.class);
    private final PrintWriter writer = mock(PrintWriter.class);
    private final JsonHelper jsonHelper = mock(JsonHelper.class);
    private final AuthenticationFilter filter = new AuthenticationFilter(jsonHelper);

    @Test
    @DisplayName("should pass request to chain if public path")
    void doFilterShouldPassRequestToChain() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn(UrlPath.AUTH);

        filter.doFilter(request, response, chain);

        verify(chain).doFilter(request, response);
    }

    @Test
    @DisplayName("should not pass request to chain if not public path")
    void doFilterShouldNotPassRequestToChain() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/test");
        when(response.getWriter()).thenReturn(writer);

        filter.doFilter(request, response, chain);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verifyNoInteractions(chain);
    }

    @Test
    @DisplayName("should pass request to chain if valid token and path")
    void doFilterShouldPassRequestToChainIfValidTokenAndPath() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/users/24/balance");
        when(request.getPathInfo()).thenReturn("/24/balance");
        when(request.getHeader("Authorization")).thenReturn(VALID_TOKEN_STRING_FOR_USER24);

        filter.doFilter(request, response, chain);

        verify(chain).doFilter(request, response);
    }

    @Test
    @DisplayName("should not pass request to chain if id in token and path differs")
    void doFilterShouldNotPassRequestToChainIfValidTokenAndInvalidPath() throws ServletException, IOException {
        when(response.getWriter()).thenReturn(writer);
        when(request.getRequestURI()).thenReturn("/users/23/balance");
        when(request.getPathInfo()).thenReturn("/23/balance");
        when(request.getHeader("Authorization")).thenReturn(VALID_TOKEN_STRING_FOR_USER24);

        filter.doFilter(request, response, chain);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(jsonHelper).buildErrorResponse(
                AUTHORIZATION_ERROR_MESSAGE,
                HttpServletResponse.SC_UNAUTHORIZED);
        verifyNoInteractions(chain);
    }
}
