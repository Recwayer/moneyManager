package ru.example.service;

import java.util.List;

public interface CategoryService {
  void addCategory(String category);

  void removeCategory(String category);

  List<String> getAllCategories();

  List<String> getExpenseCategories();

  List<String> getIncomeCategories();

  boolean categoryExists(String category);

  void validateCategory(String category);

  boolean isExpenseCategory(String category);

  boolean isIncomeCategory(String category);

  void setUserCategories(List<String> categories);

  List<String> getUserCategories();
}
