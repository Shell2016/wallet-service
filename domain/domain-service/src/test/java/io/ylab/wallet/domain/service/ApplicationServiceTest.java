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

@DisplayName("Application Service Test")
class ApplicationServiceTest {

    private static final String EXPECTED_OUTPUT = """
                Для завершения программы введите: exit
                
                Для выбора действия введите нужное число в консоль:
                1. Логин
                2. Регистрация нового пользователя""";
    private final WalletController controller = Mockito.mock(WalletController.class);
    private final UserService userService = Mockito.mock(UserService.class);
    private final TransactionService transactionService = Mockito.mock(TransactionService.class);
    private final AuditService auditService = Mockito.mock(AuditService.class);
    private final AccountService accountService = Mockito.mock(AccountService.class);

    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    private final ApplicationService applicationService =
            new ApplicationService(controller, userService, accountService, transactionService, auditService);

    @BeforeEach
    void init() {
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    @DisplayName("state initialization")
    void testStateInitialization() {
        assertThat(applicationService.getState()).isInstanceOf(StartState.class);
    }

    @Test
    @DisplayName("setState")
    void testSetState() {
        applicationService.setState(AuthorizedState.class);
        assertThat(applicationService.getState()).isInstanceOf(AuthorizedState.class);
    }

    @Test
    @DisplayName("check console output")
    void testConsole() {
        when(controller.getInput()).thenReturn("exit");
        applicationService.run();
        String printedOutput = outputStream.toString().trim();

        assertThat(printedOutput).isEqualTo(EXPECTED_OUTPUT);
    }
}
