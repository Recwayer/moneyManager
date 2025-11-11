package ru.example.util.auth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class PasswordHasher {
  private PasswordHasher() {}

  public static String hash(String password) {
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      byte[] hashedBytes = md.digest(password.getBytes());
      return Base64.getEncoder().encodeToString(hashedBytes);
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("Ошибка шифрования пароля", e);
    }
  }

  public static boolean verify(String password, String hash) {
    return hash(password).equals(hash);
  }
}
