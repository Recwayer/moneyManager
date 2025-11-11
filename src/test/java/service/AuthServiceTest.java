package service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.example.domain.User;
import ru.example.repository.impl.InMemoryUserRepository;
import ru.example.service.impl.AuthServiceImpl;
import ru.example.service.impl.CategoryServiceImpl;
import ru.example.service.impl.UserServiceImpl;

class AuthServiceTest {
  private AuthServiceImpl authService;
  private UserServiceImpl userService;

  @BeforeEach
  void setUp() {
    CategoryServiceImpl categoryService = new CategoryServiceImpl();
    userService = new UserServiceImpl(new InMemoryUserRepository());
    authService = new AuthServiceImpl(userService, categoryService);
  }

  @Test
  @DisplayName("Успешная регистрация пользователя")
  void shouldRegisterUserSuccessfully() {
    User user = authService.register("testuser", "password123");

    assertNotNull(user);
    assertEquals("testuser", user.getUsername());
    assertTrue(userService.userExists("testuser"));
  }

  @Test
  @DisplayName("Регистрация с существующим именем пользователя должна выбрасывать исключение")
  void shouldThrowExceptionWhenRegisteringExistingUser() {
    authService.register("existinguser", "password123");

    assertThrows(
        IllegalArgumentException.class,
        () -> authService.register("existinguser", "differentpassword"));
  }

  @Test
  @DisplayName("Успешный вход пользователя")
  void shouldLoginUserSuccessfully() {
    authService.register("loginuser", "password123");

    User user = authService.login("loginuser", "password123");

    assertNotNull(user);
    assertEquals("loginuser", user.getUsername());
  }

  @Test
  @DisplayName("Вход с неверным паролем должен выбрасывать исключение")
  void shouldThrowExceptionWhenLoginWithWrongPassword() {
    authService.register("user", "correctpassword");

    assertThrows(IllegalArgumentException.class, () -> authService.login("user", "wrongpassword"));
  }

  @Test
  @DisplayName("Вход с несуществующим пользователем должен выбрасывать исключение")
  void shouldThrowExceptionWhenLoginWithNonExistentUser() {
    assertThrows(
        IllegalArgumentException.class, () -> authService.login("nonexistent", "password"));
  }
}
