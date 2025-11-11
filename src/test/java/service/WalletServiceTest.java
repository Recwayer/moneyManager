package service;

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

class WalletServiceTest {
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
  @DisplayName("Добавление дохода увеличивает баланс")
  void shouldIncreaseBalanceWhenAddingIncome() {
    walletService.addIncome("Зарплата", 50000.0);

    FinancialReport report = walletService.getFinancialReport();
    assertEquals(50000.0, report.getTotalIncome());
    assertEquals(50000.0, report.getBalance());
  }

  @Test
  @DisplayName("Добавление расхода уменьшает баланс")
  void shouldDecreaseBalanceWhenAddingExpense() {
    walletService.addIncome("Зарплата", 50000.0);

    walletService.addExpense("Еда", 1500.0);

    FinancialReport report = walletService.getFinancialReport();
    assertEquals(50000.0, report.getTotalIncome());
    assertEquals(1500.0, report.getTotalExpense());
    assertEquals(48500.0, report.getBalance());
  }

  @Test
  @DisplayName("Установка бюджета для категории")
  void shouldSetBudgetForCategory() {
    walletService.setBudget("Еда", 10000.0);

    FinancialReport report = walletService.getFinancialReport();
    assertEquals(1, report.getBudgets().size());
    assertEquals("Еда", report.getBudgets().get(0).getCategory());
    assertEquals(10000.0, report.getBudgets().get(0).getLimit());
  }

  @Test
  @DisplayName("Обновление бюджета")
  void shouldUpdateBudgetLimit() {
    walletService.setBudget("Еда", 10000.0);

    walletService.updateBudget("Еда", 15000.0);

    FinancialReport report = walletService.getFinancialReport();
    assertEquals(15000.0, report.getBudgets().get(0).getLimit());
  }

  @Test
  @DisplayName("Удаление бюджета")
  void shouldRemoveBudget() {
    walletService.setBudget("Еда", 10000.0);
    assertEquals(1, walletService.getBudgetCategories().size());

    walletService.removeBudget("Еда");

    assertEquals(0, walletService.getBudgetCategories().size());
  }
}
