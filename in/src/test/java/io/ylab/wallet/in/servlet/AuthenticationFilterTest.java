package io.ylab.wallet.in.servlet;

import io.ylab.wallet.in.filter.AuthenticationFilter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static org.mockito.Mockito.*;

class AuthenticationFilterTest {

    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final FilterChain chain = mock(FilterChain.class);
    private final PrintWriter writer = mock(PrintWriter.class);
    private final AuthenticationFilter filter = new AuthenticationFilter();

    @Test
    @DisplayName("should pass request to chain if public path")
    void doFilterShouldPassRequestToChain() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/auth");

        filter.doFilter(request, response, chain);

        verify(chain).doFilter(request, response);
    }

    @Test
    @DisplayName("should not pass request to chain if not public path")
    void doFilterShouldNotPassRequestToChain() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/users/2/balance");
        when(response.getWriter()).thenReturn(writer);

        filter.doFilter(request, response, chain);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verifyNoInteractions(chain);
    }
}
