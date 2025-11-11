package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.example.domain.Transaction;
import ru.example.domain.TransactionType;
import ru.example.domain.Wallet;

class WalletTest {
  private Wallet wallet;

  @BeforeEach
  void setUp() {
    wallet = new Wallet("testuser");
  }

  @Test
  @DisplayName("Добавление дохода увеличивает баланс")
  void shouldIncreaseBalanceWhenAddingIncome() {
    Transaction income = new Transaction(TransactionType.INCOME, "Зарплата", 50000.0);

    wallet.addTransaction(income);

    assertEquals(50000.0, wallet.getBalance());
    assertEquals(1, wallet.getTransactions().size());
  }

  @Test
  @DisplayName("Добавление расхода уменьшает баланс")
  void shouldDecreaseBalanceWhenAddingExpense() {
    Transaction income = new Transaction(TransactionType.INCOME, "Зарплата", 50000.0);
    Transaction expense = new Transaction(TransactionType.EXPENSE, "Еда", 1500.0);

    wallet.addTransaction(income);
    wallet.addTransaction(expense);

    assertEquals(48500.0, wallet.getBalance());
    assertEquals(2, wallet.getTransactions().size());
  }

  @Test
  @DisplayName("Установка бюджета и отслеживание расходов")
  void shouldTrackSpendingAgainstBudget() {
    wallet.setBudget("Еда", 10000.0);
    Transaction expense1 = new Transaction(TransactionType.EXPENSE, "Еда", 3000.0);
    Transaction expense2 = new Transaction(TransactionType.EXPENSE, "Еда", 2000.0);

    wallet.addTransaction(expense1);
    wallet.addTransaction(expense2);

    var budget = wallet.getBudget("Еда").orElseThrow();
    assertEquals(5000.0, budget.getSpent());
    assertEquals(5000.0, budget.getRemaining());
    assertEquals(50.0, budget.getUtilizationPercentage());
  }

  @Test
  @DisplayName("Создание уведомлений при превышении бюджета")
  void shouldCreateAlertWhenBudgetExceeded() {
    wallet.setBudget("Еда", 1000.0);
    Transaction expense = new Transaction(TransactionType.EXPENSE, "Еда", 1200.0);

    wallet.addTransaction(expense);

    var alerts = wallet.getAlerts();
    assertEquals(2, alerts.size());
    assertTrue(alerts.get(0).getMessage().contains("превышен"));
  }
}
