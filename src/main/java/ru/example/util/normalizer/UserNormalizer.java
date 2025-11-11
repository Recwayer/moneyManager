package ru.example.util.normalizer;

public class UserNormalizer {

  private UserNormalizer() {}

  public static String normalizeUsername(String username) {
    return username != null ? username.toLowerCase() : null;
  }
}
