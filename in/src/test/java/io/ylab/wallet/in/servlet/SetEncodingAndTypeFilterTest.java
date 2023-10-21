package io.ylab.wallet.in.servlet;

import io.ylab.wallet.in.filter.SetEncodingAndTypeFilter;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.*;

class SetEncodingAndTypeFilterTest {

    private final SetEncodingAndTypeFilter filter = new SetEncodingAndTypeFilter();

    @Test
    @DisplayName("doFilter should set character encoding and content type")
    void doFilter() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        filter.doFilter(request, response, chain);

        verify(request).setCharacterEncoding(StandardCharsets.UTF_8.name());
        verify(response).setCharacterEncoding(StandardCharsets.UTF_8.name());
        verify(response).setContentType("application/json");
        verify(chain).doFilter(request, response);
    }
}