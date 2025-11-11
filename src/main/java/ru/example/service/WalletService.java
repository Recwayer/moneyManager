package ru.example.service;

import java.util.List;
import ru.example.domain.FinancialReport;

public interface WalletService {

  void addIncome(String category, double amount);

  void addExpense(String category, double amount);

  void setBudget(String category, double limit);

  void updateBudget(String category, double newLimit);

  void removeBudget(String category);

  List<String> getBudgetCategories();

  FinancialReport getFinancialReport();

  void transfer(String toUsername, double amount);
}
