package util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.example.util.auth.PasswordHasher;

class PasswordHasherTest {

  @Test
  @DisplayName("Хеширование пароля должно возвращать не-null значение")
  void shouldReturnNonNullHash() {
    String hash = PasswordHasher.hash("password123");

    assertNotNull(hash);
    assertFalse(hash.isEmpty());
  }

  @Test
  @DisplayName("Верификация корректного пароля")
  void shouldVerifyCorrectPassword() {
    String password = "password123";
    String hash = PasswordHasher.hash(password);

    assertTrue(PasswordHasher.verify(password, hash));
  }

  @Test
  @DisplayName("Верификация некорректного пароля")
  void shouldNotVerifyIncorrectPassword() {
    String correctPassword = "password123";
    String wrongPassword = "wrongpassword";
    String hash = PasswordHasher.hash(correctPassword);

    assertFalse(PasswordHasher.verify(wrongPassword, hash));
  }
}
