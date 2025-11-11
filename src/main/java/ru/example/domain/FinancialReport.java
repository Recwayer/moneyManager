package ru.example.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FinancialReport {
  private final double totalIncome;
  private final double totalExpense;
  private final double balance;
  private final Map<String, Double> incomeByCategory;
  private final Map<String, Double> expenseByCategory;
  private final List<Budget> budgets;
  private final List<Alert> alerts;

  public FinancialReport(Wallet wallet) {
    this.totalIncome = calculateTotalIncome(wallet);
    this.totalExpense = calculateTotalExpense(wallet);
    this.balance = wallet.getBalance();
    this.incomeByCategory = calculateIncomeByCategory(wallet);
    this.expenseByCategory = calculateExpenseByCategory(wallet);
    this.budgets = new ArrayList<>(wallet.getBudgets().values());
    this.alerts = wallet.getAlerts();
  }

  private double calculateTotalIncome(Wallet wallet) {
    return wallet.getIncomeTransactions().stream().mapToDouble(Transaction::getAmount).sum();
  }

  private double calculateTotalExpense(Wallet wallet) {
    return wallet.getExpenseTransactions().stream().mapToDouble(Transaction::getAmount).sum();
  }

  private Map<String, Double> calculateIncomeByCategory(Wallet wallet) {
    return wallet.getIncomeTransactions().stream()
        .collect(
            Collectors.groupingBy(
                Transaction::getCategory, Collectors.summingDouble(Transaction::getAmount)));
  }

  private Map<String, Double> calculateExpenseByCategory(Wallet wallet) {
    return wallet.getExpenseTransactions().stream()
        .collect(
            Collectors.groupingBy(
                Transaction::getCategory, Collectors.summingDouble(Transaction::getAmount)));
  }

  public double getTotalIncome() {
    return totalIncome;
  }

  public double getTotalExpense() {
    return totalExpense;
  }

  public double getBalance() {
    return balance;
  }

  public Map<String, Double> getIncomeByCategory() {
    return incomeByCategory;
  }

  public Map<String, Double> getExpenseByCategory() {
    return expenseByCategory;
  }

  public List<Budget> getBudgets() {
    return budgets;
  }

  public List<Alert> getAlerts() {
    return alerts;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Финансовый отчёт\n");
    sb.append(String.format("Общий доход: %.2f\n", totalIncome));
    sb.append(String.format("Общий расход: %.2f\n", totalExpense));
    sb.append(String.format("Баланс: %.2f\n\n", balance));

    sb.append("Доходы по категориям:\n");
    incomeByCategory.forEach(
        (category, amount) -> sb.append(String.format("  %s: %.2f\n", category, amount)));

    sb.append("\nРасходы по категориям:\n");
    expenseByCategory.forEach(
        (category, amount) -> sb.append(String.format("  %s: %.2f\n", category, amount)));

    if (!budgets.isEmpty()) {
      sb.append("\nБюджеты\n");
      budgets.forEach(
          budget ->
              sb.append(
                  String.format(
                      "  %s: Лимит %.2f, Потрачено %.2f, Осталось %.2f (%.1f%%)\n",
                      budget.getCategory(),
                      budget.getLimit(),
                      budget.getSpent(),
                      budget.getRemaining(),
                      budget.getUtilizationPercentage())));
    }

    if (!alerts.isEmpty()) {
      sb.append("\nУведомления\n");
      alerts.forEach(alert -> sb.append("  • ").append(alert.getMessage()).append("\n"));
    }

    return sb.toString();
  }
}
