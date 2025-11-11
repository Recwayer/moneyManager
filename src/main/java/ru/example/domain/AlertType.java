package ru.example.domain;

import java.io.Serializable;

public enum AlertType implements Serializable {
  BUDGET_EXCEEDED,
  BUDGET_WARNING,
  NEGATIVE_BALANCE,
}
