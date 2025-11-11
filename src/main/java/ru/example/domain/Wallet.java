package ru.example.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import ru.example.util.normalizer.CategoryNormalizer;

public class Wallet implements Serializable {
  private static final long serialVersionUID = 1L;

  private final String ownerUsername;
  private double balance;
  private final List<Transaction> transactions;
  private final Map<String, Budget> budgets;
  private final List<Alert> alerts;

  public Wallet(String ownerUsername) {
    this.ownerUsername = ownerUsername;
    this.balance = 0.0;
    this.transactions = new ArrayList<>();
    this.budgets = new ConcurrentHashMap<>();
    this.alerts = new ArrayList<>();
  }

  public void addTransaction(Transaction transaction) {
    String normalizedCategory = CategoryNormalizer.normalizeCategory(transaction.getCategory());
    Transaction normalizedTransaction =
        new Transaction(transaction.getType(), normalizedCategory, transaction.getAmount());

    transactions.add(normalizedTransaction);

    if (normalizedTransaction.isIncome()) {
      balance += normalizedTransaction.getAmount();
    } else if (normalizedTransaction.isExpense()) {
      balance -= normalizedTransaction.getAmount();
      updateBudgetSpending(normalizedCategory, normalizedTransaction.getAmount());
    }

    checkAlerts();
  }

  private void updateBudgetSpending(String category, double amount) {
    String normalizedKey = CategoryNormalizer.normalizeForComparison(category);
    Budget budget = budgets.get(normalizedKey);
    if (budget != null) {
      budget.addSpending(amount);
    }
  }

  private void checkAlerts() {
    alerts.clear();

    for (Budget budget : budgets.values()) {
      double utilization = budget.getUtilizationPercentage();
      if (utilization >= 100) {
        alerts.add(
            new Alert(
                AlertType.BUDGET_EXCEEDED,
                String.format(
                    "Бюджет категории '%s' превышен! Использовано: %.1f%%",
                    budget.getCategory(), utilization),
                LocalDateTime.now()));
      } else if (utilization >= 80) {
        alerts.add(
            new Alert(
                AlertType.BUDGET_WARNING,
                String.format(
                    "Бюджет категории '%s' почти исчерпан! Использовано: %.1f%%",
                    budget.getCategory(), utilization),
                LocalDateTime.now()));
      }
    }

    if (balance < 0) {
      alerts.add(
          new Alert(
              AlertType.NEGATIVE_BALANCE,
              "Внимание! Отрицательный баланс: " + balance,
              LocalDateTime.now()));
    }
  }

  public void setBudget(String category, double limit) {
    String normalizedCategory = CategoryNormalizer.normalizeCategory(category);
    String normalizedKey = CategoryNormalizer.normalizeForComparison(category);

    double alreadySpent = calculateSpentForCategory(normalizedCategory);

    Budget budget = new Budget(normalizedCategory, limit);
    budget.addSpending(alreadySpent);

    budgets.put(normalizedKey, budget);
    checkAlerts();
  }

  public void updateBudget(String category, double newLimit) {
    String normalizedKey = CategoryNormalizer.normalizeForComparison(category);
    Budget budget = budgets.get(normalizedKey);

    if (budget == null) {
      throw new IllegalArgumentException("Бюджет для категории '" + category + "' не найден");
    }

    budget.updateLimit(newLimit);
    checkAlerts();
  }

  public void removeBudget(String category) {
    String normalizedKey = CategoryNormalizer.normalizeForComparison(category);
    Budget removedBudget = budgets.remove(normalizedKey);
    if (removedBudget != null) {
      checkAlerts();
    }
  }

  public List<String> getBudgetCategories() {
    return budgets.values().stream().map(Budget::getCategory).sorted().collect(Collectors.toList());
  }

  public Optional<Budget> getBudget(String category) {
    String normalizedKey = CategoryNormalizer.normalizeForComparison(category);
    return Optional.ofNullable(budgets.get(normalizedKey));
  }

  public boolean hasBudgetForCategory(String category) {
    String normalizedKey = CategoryNormalizer.normalizeForComparison(category);
    return budgets.containsKey(normalizedKey);
  }

  private double calculateSpentForCategory(String normalizedCategory) {
    return transactions.stream()
        .filter(Transaction::isExpense)
        .filter(t -> t.getCategory().equals(normalizedCategory))
        .mapToDouble(Transaction::getAmount)
        .sum();
  }

  public String getOwnerUsername() {
    return ownerUsername;
  }

  public double getBalance() {
    return balance;
  }

  public List<Transaction> getTransactions() {
    return new ArrayList<>(transactions);
  }

  public Map<String, Budget> getBudgets() {
    Map<String, Budget> result = new HashMap<>();
    for (Budget budget : budgets.values()) {
      result.put(budget.getCategory(), budget);
    }
    return result;
  }

  public List<Alert> getAlerts() {
    return new ArrayList<>(alerts);
  }

  public List<Transaction> getIncomeTransactions() {
    return transactions.stream().filter(Transaction::isIncome).collect(Collectors.toList());
  }

  public List<Transaction> getExpenseTransactions() {
    return transactions.stream().filter(Transaction::isExpense).collect(Collectors.toList());
  }
}
