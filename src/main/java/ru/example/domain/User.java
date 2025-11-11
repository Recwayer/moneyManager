package ru.example.domain;

import static ru.example.util.normalizer.CategoryNormalizer.normalizeCategory;
import static ru.example.util.normalizer.UserNormalizer.normalizeUsername;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User implements Serializable {
  private static final long serialVersionUID = 1L;

  private final String username;
  private final String passwordHash;
  private final LocalDateTime createdAt;
  private Wallet wallet;
  private final List<String> customCategories;

  public User(String username, String passwordHash) {
    this.username = username;
    this.passwordHash = passwordHash;
    this.createdAt = LocalDateTime.now();
    this.wallet = new Wallet(username);
    this.customCategories = new ArrayList<>();
    initializeDefaultCategories();
  }

  public User(
      String username,
      String passwordHash,
      LocalDateTime createdAt,
      Wallet wallet,
      List<String> customCategories) {
    this.username = username;
    this.passwordHash = passwordHash;
    this.createdAt = createdAt;
    this.wallet = wallet;
    this.customCategories =
        customCategories != null ? new ArrayList<>(customCategories) : new ArrayList<>();
  }

  private void initializeDefaultCategories() {
    addPredefinedCategory("Еда");
    addPredefinedCategory("Транспорт");
    addPredefinedCategory("Жилье");
    addPredefinedCategory("Развлечения");
    addPredefinedCategory("Здоровье");
    addPredefinedCategory("Одежда");
    addPredefinedCategory("Образование");
    addPredefinedCategory("Зарплата");
    addPredefinedCategory("Премия");
    addPredefinedCategory("Инвестиции");
    addPredefinedCategory("Подарок");
    addPredefinedCategory("Прочее");
  }

  private void addPredefinedCategory(String category) {
    if (!customCategories.contains(category)) {
      customCategories.add(category);
    }
  }

  public List<String> getCustomCategories() {
    return new ArrayList<>(customCategories);
  }

  public void addCategory(String category) {
    if (category == null || category.trim().isEmpty()) {
      throw new IllegalArgumentException("Категория не может быть пустой");
    }
    String normalized = normalizeCategory(category);
    if (!customCategories.contains(normalized)) {
      customCategories.add(normalized);
    }
  }

  public void removeCategory(String category) {
    if (category == null || category.trim().isEmpty()) {
      throw new IllegalArgumentException("Категория не может быть пустой");
    }
    String normalized = normalizeCategory(category);
    customCategories.remove(normalized);
  }

  public boolean hasCategory(String category) {
    String normalized = normalizeCategory(category);
    return customCategories.contains(normalized);
  }

  public String getUsername() {
    return username;
  }

  public String getPasswordHash() {
    return passwordHash;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public Wallet getWallet() {
    return wallet;
  }

  public void setWallet(Wallet wallet) {
    this.wallet = wallet;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    User user = (User) o;
    return Objects.equals(normalizeUsername(username), normalizeUsername(user.username));
  }

  @Override
  public int hashCode() {
    return Objects.hash(normalizeUsername(username));
  }
}
