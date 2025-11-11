package ru.example.service.impl;

import static ru.example.util.normalizer.CategoryNormalizer.normalizeCategory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import ru.example.service.CategoryService;
import ru.example.util.validation.ValidatorUtil;

public class CategoryServiceImpl implements CategoryService {
  private List<String> userCategories;
  private final List<String> predefinedExpenseCategories;
  private final List<String> predefinedIncomeCategories;

  public CategoryServiceImpl() {
    this.userCategories = new ArrayList<>();
    this.predefinedExpenseCategories = initializePredefinedExpenseCategories();
    this.predefinedIncomeCategories = initializePredefinedIncomeCategories();
  }

  public CategoryServiceImpl(List<String> userCategories) {
    this.userCategories = new ArrayList<>(userCategories);
    this.predefinedExpenseCategories = initializePredefinedExpenseCategories();
    this.predefinedIncomeCategories = initializePredefinedIncomeCategories();
  }

  private List<String> initializePredefinedExpenseCategories() {
    return Arrays.asList(
        "Еда", "Транспорт", "Жилье", "Развлечения", "Здоровье", "Одежда", "Образование");
  }

  private List<String> initializePredefinedIncomeCategories() {
    return Arrays.asList("Зарплата", "Премия", "Инвестиции", "Подарок", "Прочее");
  }

  @Override
  public void setUserCategories(List<String> categories) {
    this.userCategories = new ArrayList<>(categories);
  }

  @Override
  public List<String> getUserCategories() {
    return new ArrayList<>(userCategories);
  }

  @Override
  public void addCategory(String category) {
    ValidatorUtil.validateCategory(category);
    String normalizedCategory = normalizeCategory(category);

    if (categoryExists(normalizedCategory)) {
      throw new IllegalArgumentException("Категория '" + category + "' уже существует");
    }

    userCategories.add(normalizedCategory);
  }

  @Override
  public void removeCategory(String category) {
    ValidatorUtil.validateCategory(category);
    String normalizedCategory = normalizeCategory(category);

    if (!categoryExists(normalizedCategory)) {
      throw new IllegalArgumentException("Категория '" + category + "' не найдена");
    }

    if (isPredefinedCategory(normalizedCategory)) {
      throw new IllegalArgumentException("Нельзя удалять системные категории");
    }

    userCategories.remove(normalizedCategory);
  }

  @Override
  public List<String> getAllCategories() {
    Set<String> allCategories = new TreeSet<>();
    allCategories.addAll(predefinedExpenseCategories);
    allCategories.addAll(predefinedIncomeCategories);
    allCategories.addAll(userCategories);
    return new ArrayList<>(allCategories);
  }

  @Override
  public List<String> getExpenseCategories() {
    Set<String> expenseCategories = new TreeSet<>();
    expenseCategories.addAll(predefinedExpenseCategories);
    expenseCategories.addAll(userCategories);
    return new ArrayList<>(expenseCategories);
  }

  @Override
  public List<String> getIncomeCategories() {
    Set<String> incomeCategories = new TreeSet<>();
    incomeCategories.addAll(predefinedIncomeCategories);
    incomeCategories.addAll(userCategories);
    return new ArrayList<>(incomeCategories);
  }

  @Override
  public boolean categoryExists(String category) {
    String normalizedCategory = normalizeCategory(category);
    return predefinedExpenseCategories.contains(normalizedCategory)
        || predefinedIncomeCategories.contains(normalizedCategory)
        || userCategories.contains(normalizedCategory);
  }

  @Override
  public boolean isExpenseCategory(String category) {
    String normalizedCategory = normalizeCategory(category);
    return predefinedExpenseCategories.contains(normalizedCategory)
        || userCategories.contains(normalizedCategory);
  }

  @Override
  public boolean isIncomeCategory(String category) {
    String normalizedCategory = normalizeCategory(category);
    return predefinedIncomeCategories.contains(normalizedCategory)
        || userCategories.contains(normalizedCategory);
  }

  @Override
  public void validateCategory(String category) {
    ValidatorUtil.validateCategory(category);
    String normalizedCategory = normalizeCategory(category);

    if (!categoryExists(normalizedCategory)) {
      throw new IllegalArgumentException(
          "Категория '"
              + category
              + "' не найдена. Доступные категории: "
              + String.join(", ", getAllCategories()));
    }
  }

  private boolean isPredefinedCategory(String category) {
    return predefinedExpenseCategories.contains(category)
        || predefinedIncomeCategories.contains(category);
  }
}
