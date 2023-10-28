package io.ylab.wallet.in.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.ylab.wallet.domain.dto.UserRequest;
import io.ylab.wallet.domain.security.SecurityService;
import io.ylab.wallet.domain.security.TokenDetails;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Assertions.assertThatException;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    private static final String USERNAME1 = "user1";
    private static final String PASSWORD = "123456";
    private static final UserRequest USER_REQUEST =
            UserRequest.builder()
                    .username(USERNAME1)
                    .password(PASSWORD)
                    .build();
    private static final UserRequest USER_REQUEST_INVALID_PASSWORD =
            UserRequest.builder()
                    .username(USERNAME1)
                    .password("")
                    .build();
    private static final long USER_ID1 = 1L;
    private static final TokenDetails TOKEN_DETAILS = TokenDetails.builder()
            .token("jwt token")
            .userId(USER_ID1)
            .build();

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Mock
    private SecurityService securityService;
    @InjectMocks
    private AuthenticationController controller;
    private MockMvc mockMvc;

    @BeforeEach
    void init() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .alwaysExpect(content().contentType(APPLICATION_JSON))
                .alwaysExpect(status().isOk())
                .build();
    }

    @Test
    @DisplayName("authenticate should throw exception if invalid credentials")
    void authenticateInvalidCredentials() {
        when(securityService.authenticate(USERNAME1, PASSWORD)).thenThrow(new RuntimeException());

        assertThatException().isThrownBy(
                () -> mockMvc.perform(post("/auth")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(USER_REQUEST))));
    }

    @Test
    @DisplayName("authenticate invalid input")
    void authenticateInvalidInput() {
        assertThatException().isThrownBy(
                () -> mockMvc.perform(post("/auth")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(USER_REQUEST_INVALID_PASSWORD))));
    }

    @Test
    @DisplayName("authenticate success")
    void authenticateSuccess() throws Exception {
        when(securityService.authenticate(USERNAME1, PASSWORD)).thenReturn(TOKEN_DETAILS);

        mockMvc.perform(post("/auth")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(USER_REQUEST)))
                .andExpectAll(
                        jsonPath("$.user_id").value("1"),
                        jsonPath("$.token").value("jwt token")
                );
    }
}
