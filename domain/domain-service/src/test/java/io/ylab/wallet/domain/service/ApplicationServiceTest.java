package io.ylab.wallet.domain.service;

import io.ylab.wallet.domain.port.input.controller.WalletController;
import io.ylab.wallet.domain.state.AuthorizedState;
import io.ylab.wallet.domain.state.StartState;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class ApplicationServiceTest {

    public static final String EXPECTED_OUTPUT = """
                Для завершения программы введите: exit
                Для выбора действия введите нужное число в консоль:
                1. Логин
                2. Регистрация нового пользователя""";
    private final WalletController controller = Mockito.mock(WalletController.class);
    private final UserService userService = Mockito.mock(UserService.class);

    private ApplicationService applicationService;

    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void init() {
        applicationService = new ApplicationService(controller, userService, transactionService);
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void testStateInitialization() {
        assertThat(applicationService.getState()).isInstanceOf(StartState.class);
    }

    @Test
    void testSetState() {
        applicationService.setState(AuthorizedState.class);
        assertThat(applicationService.getState()).isInstanceOf(AuthorizedState.class);
    }

    @Test
    void testConsoleOutput() {
        when(controller.getInput()).thenReturn("exit");
        applicationService.run();
        String printedOutput = outputStream.toString().trim();

        assertThat(EXPECTED_OUTPUT).isEqualTo(printedOutput);
    }

}