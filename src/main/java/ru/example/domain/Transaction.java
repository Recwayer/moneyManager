package ru.example.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction implements Serializable {
  private static final long serialVersionUID = 1L;

  private final String id;
  private final TransactionType type;
  private final String category;
  private final double amount;
  private final LocalDateTime date;

  public Transaction(TransactionType type, String category, double amount) {
    this.id = UUID.randomUUID().toString();
    this.type = type;
    this.category = category;
    this.amount = amount;
    this.date = LocalDateTime.now();
  }

  public String getId() {
    return id;
  }

  public TransactionType getType() {
    return type;
  }

  public String getCategory() {
    return category;
  }

  public double getAmount() {
    return amount;
  }

  public LocalDateTime getDate() {
    return date;
  }

  public boolean isIncome() {
    return type == TransactionType.INCOME;
  }

  public boolean isExpense() {
    return type == TransactionType.EXPENSE;
  }

  @Override
  public String toString() {
    return String.format("%s: %s - %.2f", type, category, amount);
  }
}
