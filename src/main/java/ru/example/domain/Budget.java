package ru.example.domain;

import java.io.Serializable;

public class Budget implements Serializable {
  private static final long serialVersionUID = 1L;

  private final String category;
  private double limit;
  private double spent;

  public Budget(String category, double limit) {
    this.category = category;
    this.limit = limit;
    this.spent = 0.0;
  }

  public void addSpending(double amount) {
    this.spent += amount;
  }

  public void updateLimit(double newLimit) {
    this.limit = newLimit;
  }

  public String getCategory() {
    return category;
  }

  public double getLimit() {
    return limit;
  }

  public double getSpent() {
    return spent;
  }

  public double getRemaining() {
    return limit - spent;
  }

  public double getUtilizationPercentage() {
    return limit > 0 ? (spent / limit) * 100 : 0;
  }

  @Override
  public String toString() {
    return String.format(
        "Budget{category='%s', limit=%.2f, spent=%.2f, remaining=%.2f}",
        category, limit, spent, getRemaining());
  }
}
