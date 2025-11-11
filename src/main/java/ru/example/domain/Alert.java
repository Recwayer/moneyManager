package ru.example.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Alert implements Serializable {
  private static final long serialVersionUID = 1L;

  private final AlertType type;
  private final String message;
  private final LocalDateTime createdAt;

  public Alert(AlertType type, String message, LocalDateTime createdAt) {
    this.type = type;
    this.message = message;
    this.createdAt = createdAt;
  }

  public AlertType getType() {
    return type;
  }

  public String getMessage() {
    return message;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  @Override
  public String toString() {
    return String.format("[%s] %s (%s)", type, message, createdAt);
  }
}
