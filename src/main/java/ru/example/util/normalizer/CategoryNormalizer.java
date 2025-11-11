package ru.example.util.normalizer;

public class CategoryNormalizer {

  private CategoryNormalizer() {}

  public static String normalizeCategory(String category) {
    if (category == null || category.trim().isEmpty()) {
      return category;
    }

    String trimmed = category.trim();

    if (trimmed.length() == 1) {
      return trimmed.toUpperCase();
    } else {
      return trimmed.substring(0, 1).toUpperCase() + trimmed.substring(1).toLowerCase();
    }
  }

  public static String normalizeForComparison(String category) {
    return category != null ? category.toLowerCase().trim() : null;
  }
}
