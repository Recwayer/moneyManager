package ru.example.service.impl;

import java.util.List;
import ru.example.domain.FinancialReport;
import ru.example.domain.Transaction;
import ru.example.domain.TransactionType;
import ru.example.domain.User;
import ru.example.service.AuthService;
import ru.example.service.CategoryService;
import ru.example.service.UserService;
import ru.example.service.WalletService;
import ru.example.util.fileio.FileServiceUtil;
import ru.example.util.validation.ValidatorUtil;

public class WalletServiceImpl implements WalletService {
  private final UserService userService;
  private final CategoryService categoryService;
  private final AuthService authService;

  public WalletServiceImpl(
      UserService userService, CategoryService categoryService, AuthService authService) {
    this.userService = userService;
    this.categoryService = categoryService;
    this.authService = authService;
  }

  @Override
  public void addIncome(String category, double amount) {
    authService.checkAuthentication();
    ValidatorUtil.validateAmount(amount);
    categoryService.validateCategory(category);

    if (!categoryService.isIncomeCategory(category)) {
      throw new IllegalArgumentException(
          "Категория '"
              + category
              + "' не является категорией дохода. "
              + "Доступные категории доходов: "
              + String.join(", ", categoryService.getIncomeCategories()));
    }

    User user = authService.getCurrentUser();
    Transaction income = new Transaction(TransactionType.INCOME, category, amount);
    user.getWallet().addTransaction(income);
    userService.saveUser(user);
    authService.saveCurrentUser();
  }

  @Override
  public void addExpense(String category, double amount) {
    authService.checkAuthentication();
    ValidatorUtil.validateAmount(amount);
    categoryService.validateCategory(category);

    if (!categoryService.isExpenseCategory(category)) {
      throw new IllegalArgumentException(
          "Категория '"
              + category
              + "' не является категорией расхода. "
              + "Доступные категории расходов: "
              + String.join(", ", categoryService.getExpenseCategories()));
    }

    User user = authService.getCurrentUser();
    Transaction expense = new Transaction(TransactionType.EXPENSE, category, amount);
    user.getWallet().addTransaction(expense);
    userService.saveUser(user);
    authService.saveCurrentUser();
  }

  @Override
  public void setBudget(String category, double limit) {
    authService.checkAuthentication();
    ValidatorUtil.validateAmount(limit);
    categoryService.validateCategory(category);

    if (!categoryService.isExpenseCategory(category)) {
      throw new IllegalArgumentException(
          "Бюджет можно установить только для категорий расходов. "
              + "Категория '"
              + category
              + "' не является категорией расхода.");
    }

    User user = authService.getCurrentUser();

    if (user.getWallet().hasBudgetForCategory(category)) {
      throw new IllegalArgumentException(
          "Бюджет для категории '"
              + category
              + "' уже установлен. Используйте обновление бюджета.");
    }

    validateBudgetLimit(category, limit, user);

    user.getWallet().setBudget(category, limit);
    userService.saveUser(user);
    authService.saveCurrentUser();
  }

  @Override
  public void updateBudget(String category, double newLimit) {
    authService.checkAuthentication();
    ValidatorUtil.validateAmount(newLimit);
    categoryService.validateCategory(category);

    User user = authService.getCurrentUser();

    if (!categoryService.isExpenseCategory(category)) {
      throw new IllegalArgumentException(
          "Бюджет можно установить только для категорий расходов. "
              + "Категория '"
              + category
              + "' не является категорией расхода.");
    }

    if (!user.getWallet().hasBudgetForCategory(category)) {
      throw new IllegalArgumentException(
          "Бюджет для категории '" + category + "' не найден. Сначала установите бюджет.");
    }

    validateBudgetLimit(category, newLimit, user);

    user.getWallet().updateBudget(category, newLimit);
    userService.saveUser(user);
    authService.saveCurrentUser();
  }

  private double calculateSpentForCategory(User user, String category) {
    return user.getWallet().getExpenseTransactions().stream()
        .filter(t -> t.getCategory().equalsIgnoreCase(category))
        .mapToDouble(Transaction::getAmount)
        .sum();
  }

  private void validateBudgetLimit(String category, double limit, User user) {
    double alreadySpent = calculateSpentForCategory(user, category);

    if (limit < alreadySpent) {
      throw new IllegalArgumentException(
          String.format(
              "Лимит бюджета (%.2f) не может быть меньше уже потраченной суммы (%.2f) по категории '%s'",
              limit, alreadySpent, category));
    }
  }

  public FinancialReport getFinancialReport() {
    authService.checkAuthentication();
    User user = authService.getCurrentUser();
    return new FinancialReport(user.getWallet());
  }

  public List<String> getBudgetCategories() {
    authService.checkAuthentication();
    User user = authService.getCurrentUser();
    return user.getWallet().getBudgetCategories();
  }

  public void removeBudget(String category) {
    authService.checkAuthentication();
    categoryService.validateCategory(category);

    User user = authService.getCurrentUser();

    if (!user.getWallet().hasBudgetForCategory(category)) {
      throw new IllegalArgumentException("Бюджет для категории '" + category + "' не найден");
    }

    user.getWallet().removeBudget(category);
    userService.saveUser(user);
    authService.saveCurrentUser();
  }

  public void transfer(String toUsername, double amount) {
    authService.checkAuthentication();
    ValidatorUtil.validateAmount(amount);

    User fromUser = authService.getCurrentUser();
    User toUser =
        userService
            .findUserByUsername(toUsername)
            .orElseThrow(() -> new IllegalArgumentException("Получатель не найден"));
    if (fromUser.equals(toUser)) {
      throw new IllegalArgumentException("Нельзя выполнить перевод себе");
    }

    if (fromUser.getWallet().getBalance() < amount) {
      throw new IllegalArgumentException("Недостаточно средств для перевода");
    }

    Transaction expense = new Transaction(TransactionType.EXPENSE, "Перевод", amount);
    fromUser.getWallet().addTransaction(expense);

    Transaction income = new Transaction(TransactionType.INCOME, "Перевод", amount);
    toUser.getWallet().addTransaction(income);

    userService.saveUser(fromUser);
    userService.saveUser(toUser);
    FileServiceUtil.saveUser(toUser);
    authService.saveCurrentUser();
  }
}
