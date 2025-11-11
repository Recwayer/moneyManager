package integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.example.domain.FinancialReport;
import ru.example.repository.impl.InMemoryUserRepository;
import ru.example.service.impl.AuthServiceImpl;
import ru.example.service.impl.CategoryServiceImpl;
import ru.example.service.impl.UserServiceImpl;
import ru.example.service.impl.WalletServiceImpl;

class FinanceIntegrationTest {
  private WalletServiceImpl walletService;
  private AuthServiceImpl authService;
  private UserServiceImpl userService;
  private CategoryServiceImpl categoryService;

  @BeforeEach
  void setUp() {
    userService = new UserServiceImpl(new InMemoryUserRepository());
    categoryService = new CategoryServiceImpl();
    authService = new AuthServiceImpl(userService, categoryService);
    walletService = new WalletServiceImpl(userService, categoryService, authService);

    authService.register("testuser", "password");
  }

  @Test
  @DisplayName("Полный цикл управления финансами")
  void shouldHandleCompleteFinanceManagementCycle() {
    walletService.addIncome("Зарплата", 50000.0);
    walletService.addIncome("Премия", 10000.0);

    walletService.setBudget("Еда", 15000.0);
    walletService.setBudget("Транспорт", 5000.0);

    walletService.addExpense("Еда", 8000.0);
    walletService.addExpense("Транспорт", 2000.0);
    walletService.addExpense("Еда", 4000.0);

    FinancialReport report = walletService.getFinancialReport();

    assertEquals(60000.0, report.getTotalIncome());
    assertEquals(14000.0, report.getTotalExpense());
    assertEquals(46000.0, report.getBalance());
    assertEquals(2, report.getBudgets().size());

    var foodBudget =
        report.getBudgets().stream()
            .filter(b -> b.getCategory().equals("Еда"))
            .findFirst()
            .orElseThrow();

    assertEquals(12000.0, foodBudget.getSpent());
    assertEquals(3000.0, foodBudget.getRemaining());
    assertEquals(80.0, foodBudget.getUtilizationPercentage());
  }
}
