package io.ylab.wallet.in.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.ylab.wallet.domain.dto.UserRequest;
import io.ylab.wallet.domain.dto.UserResponse;
import io.ylab.wallet.domain.service.UserService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Assertions.assertThatException;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserRegistrationControllerTest {

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
    private static final UserResponse USER_RESPONSE =
            UserResponse.builder()
                    .id(USER_ID1)
                    .username(USERNAME1)
                    .build();

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Mock
    private UserService userService;
    @InjectMocks
    private UserRegistrationController controller;
    private MockMvc mockMvc;

    @BeforeEach
    void init() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .alwaysExpect(content().contentType(MediaType.APPLICATION_JSON))
                .alwaysExpect(status().isCreated())
                .build();
    }

    @Test
    @DisplayName("createUser success")
    void createUserSuccess() throws Exception {
        when(userService.createUser(USER_REQUEST)).thenReturn(USER_RESPONSE);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(USER_REQUEST)))
                .andExpectAll(
                        jsonPath("$.id").value("1"),
                        jsonPath("$.username").value("user1")
                );
    }

    @Test
    @DisplayName("createUser should throw exception if user exists")
    void createUserIfExists() {
        when(userService.createUser(Mockito.any(UserRequest.class)))
                .thenThrow(new RuntimeException());

        assertThatException().isThrownBy(() -> mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(USER_REQUEST))));
    }

    @Test
    @DisplayName("createUser invalid input should throw exception")
    void createUserInvalidInput() {
        assertThatException().isThrownBy(() -> mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(USER_REQUEST_INVALID_PASSWORD))));
    }
}
