package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.example.domain.Budget;

class BudgetTest {

  @Test
  @DisplayName("Создание бюджета с начальными значениями")
  void shouldCreateBudgetWithInitialValues() {
    Budget budget = new Budget("Еда", 10000.0);

    assertEquals("Еда", budget.getCategory());
    assertEquals(10000.0, budget.getLimit());
    assertEquals(0.0, budget.getSpent());
    assertEquals(10000.0, budget.getRemaining());
    assertEquals(0.0, budget.getUtilizationPercentage());
  }

  @Test
  @DisplayName("Добавление расходов к бюджету")
  void shouldAddSpendingToBudget() {
    Budget budget = new Budget("Еда", 10000.0);

    budget.addSpending(3000.0);
    budget.addSpending(2000.0);

    assertEquals(5000.0, budget.getSpent());
    assertEquals(5000.0, budget.getRemaining());
    assertEquals(50.0, budget.getUtilizationPercentage());
  }

  @Test
  @DisplayName("Обновление лимита бюджета")
  void shouldUpdateBudgetLimit() {
    Budget budget = new Budget("Еда", 10000.0);
    budget.addSpending(3000.0);

    budget.updateLimit(15000.0);

    assertEquals(15000.0, budget.getLimit());
    assertEquals(3000.0, budget.getSpent());
    assertEquals(12000.0, budget.getRemaining());
  }
}
