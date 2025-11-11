package util;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.example.util.validation.ValidatorUtil;

public class ValidatorUtilTest {

  @Test
  @DisplayName("Валидация корректного имени пользователя")
  void shouldValidateCorrectUsername() {
    assertDoesNotThrow(() -> ValidatorUtil.validateUsername("valid_user123"));
  }

  @Test
  @DisplayName("Валидация имени пользователя с недопустимыми символами")
  void shouldThrowExceptionForInvalidUsername() {
    assertThrows(
        IllegalArgumentException.class, () -> ValidatorUtil.validateUsername("invalid user!"));
  }

  @Test
  @DisplayName("Валидация корректной суммы")
  void shouldValidateCorrectAmount() {
    assertDoesNotThrow(() -> ValidatorUtil.validateAmount(1000.0));
  }

  @Test
  @DisplayName("Валидация отрицательной суммы")
  void shouldThrowExceptionForNegativeAmount() {
    assertThrows(IllegalArgumentException.class, () -> ValidatorUtil.validateAmount(-100.0));
  }

  @Test
  @DisplayName("Валидация корректной категории")
  void shouldValidateCorrectCategory() {
    assertDoesNotThrow(() -> ValidatorUtil.validateCategory("Еда"));
  }

  @Test
  @DisplayName("Валидация пустой категории")
  void shouldThrowExceptionForEmptyCategory() {
    assertThrows(IllegalArgumentException.class, () -> ValidatorUtil.validateCategory(""));
  }
}
