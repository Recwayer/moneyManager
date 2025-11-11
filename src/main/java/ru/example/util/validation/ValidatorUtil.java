package ru.example.util.validation;

public class ValidatorUtil {

  private ValidatorUtil() {}

  public static void validateTransaction(String category, double amount) {
    validateCategory(category);
    validateAmount(amount);
  }

  public static void validateCategory(String category) {
    if (category == null || category.trim().isEmpty()) {
      throw new IllegalArgumentException("Категория не может быть пустой");
    }
    if (category.length() > 50) {
      throw new IllegalArgumentException("Категория слишком длинная (максимум 50 символов)");
    }
    if (!category.matches("[a-zA-Zа-яА-Я0-9\\s]+")) {
      throw new IllegalArgumentException("Категория может содержать только буквы, цифры и пробелы");
    }
  }

  public static void validateAmount(double amount) {
    if (amount <= 0) {
      throw new IllegalArgumentException("Сумма должна быть положительной");
    }
    if (amount > 1_000_000_000) {
      throw new IllegalArgumentException("Сумма слишком большая");
    }
  }

  public static void validateUsername(String username) {
    if (username == null || username.trim().isEmpty()) {
      throw new IllegalArgumentException("Имя пользователя не может быть пустым");
    }
    if (username.length() < 3 || username.length() > 20) {
      throw new IllegalArgumentException("Имя пользователя должно быть от 3 до 20 символов");
    }
    if (!username.matches("[a-zA-Z0-9_]+")) {
      throw new IllegalArgumentException(
          "Имя пользователя может содержать только буквы, цифры и подчеркивания");
    }
  }

  public static void validatePassword(String password) {
    if (password == null || password.trim().isEmpty()) {
      throw new IllegalArgumentException("Пароль не может быть пустым");
    }
    if (password.length() < 4) {
      throw new IllegalArgumentException("Пароль должен быть не менее 4 символов");
    }
  }
}
