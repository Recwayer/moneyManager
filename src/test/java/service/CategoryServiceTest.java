package service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.example.service.impl.CategoryServiceImpl;

class CategoryServiceTest {
  private CategoryServiceImpl categoryService;

  @BeforeEach
  void setUp() {
    categoryService = new CategoryServiceImpl();
  }

  @Test
  @DisplayName("Добавление новой категории")
  void shouldAddNewCategory() {
    categoryService.addCategory("НоваяКатегория");

    assertTrue(categoryService.categoryExists("НоваяКатегория"));
    assertTrue(categoryService.getAllCategories().contains("Новаякатегория"));
  }

  @Test
  @DisplayName("Удаление пользовательской категории")
  void shouldRemoveCustomCategory() {
    categoryService.addCategory("УдаляемаяКатегория");

    categoryService.removeCategory("УдаляемаяКатегория");

    assertFalse(categoryService.categoryExists("УдаляемаяКатегория"));
  }

  @Test
  @DisplayName("Получение категорий расходов")
  void shouldReturnExpenseCategories() {
    var expenseCategories = categoryService.getExpenseCategories();

    assertNotNull(expenseCategories);
    assertTrue(expenseCategories.contains("Еда"));
    assertTrue(expenseCategories.contains("Транспорт"));
  }

  @Test
  @DisplayName("Получение категорий доходов")
  void shouldReturnIncomeCategories() {
    var incomeCategories = categoryService.getIncomeCategories();

    assertNotNull(incomeCategories);
    assertTrue(incomeCategories.contains("Зарплата"));
    assertTrue(incomeCategories.contains("Премия"));
  }

  @Test
  @DisplayName("Проверка существования категории")
  void shouldCheckCategoryExistence() {
    assertTrue(categoryService.categoryExists("Еда"));
    assertTrue(categoryService.categoryExists("Зарплата"));
    assertFalse(categoryService.categoryExists("НесуществующаяКатегория"));
  }
}
