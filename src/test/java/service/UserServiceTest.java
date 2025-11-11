package service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.example.domain.User;
import ru.example.repository.impl.InMemoryUserRepository;
import ru.example.service.impl.UserServiceImpl;
import ru.example.util.auth.PasswordHasher;

public class UserServiceTest {
  private UserServiceImpl userService;
  private InMemoryUserRepository userRepository;

  @BeforeEach
  void setUp() {
    userRepository = new InMemoryUserRepository();
    userService = new UserServiceImpl(userRepository);
  }

  @Test
  @DisplayName("Сохранение пользователя")
  void shouldSaveUser() {
    User user = new User("testuser", PasswordHasher.hash("password123"));

    userService.saveUser(user);

    Optional<User> savedUser = userService.findUserByUsername("testuser");
    assertTrue(savedUser.isPresent());
    assertEquals("testuser", savedUser.get().getUsername());
  }

  @Test
  @DisplayName("Поиск пользователя по имени пользователя")
  void shouldFindUserByUsername() {
    User user = new User("existinguser", PasswordHasher.hash("password123"));
    userService.saveUser(user);

    Optional<User> foundUser = userService.findUserByUsername("existinguser");

    assertTrue(foundUser.isPresent());
    assertEquals("existinguser", foundUser.get().getUsername());
  }

  @Test
  @DisplayName("Поиск несуществующего пользователя")
  void shouldReturnEmptyForNonExistentUser() {
    Optional<User> foundUser = userService.findUserByUsername("nonexistent");

    assertFalse(foundUser.isPresent());
  }

  @Test
  @DisplayName("Проверка существования пользователя")
  void shouldCheckUserExistence() {
    User user = new User("existinguser", PasswordHasher.hash("password123"));
    userService.saveUser(user);

    assertTrue(userService.userExists("existinguser"));
    assertFalse(userService.userExists("nonexistent"));
  }

  @Test
  @DisplayName("Удаление пользователя")
  void shouldDeleteUser() {
    User user = new User("usertodelete", PasswordHasher.hash("password123"));
    userService.saveUser(user);
    assertTrue(userService.userExists("usertodelete"));

    userService.deleteUser("usertodelete");

    assertFalse(userService.userExists("usertodelete"));
  }

  @Test
  @DisplayName("Сохранение нескольких пользователей")
  void shouldSaveAllUsers() {
    User user1 = new User("user1", PasswordHasher.hash("pass1"));
    User user2 = new User("user2", PasswordHasher.hash("pass2"));
    List<User> users = List.of(user1, user2);

    userService.saveAllUsers(users);

    assertTrue(userService.userExists("user1"));
    assertTrue(userService.userExists("user2"));
  }

  @Test
  @DisplayName("Нормализация имени пользователя при сохранении")
  void shouldNormalizeUsernameWhenSaving() {
    User user = new User("TestUser", PasswordHasher.hash("password123"));

    userService.saveUser(user);

    assertTrue(userService.userExists("testuser"));
    assertTrue(userService.userExists("TESTUSER"));
  }

  @Test
  @DisplayName("Нормализация имени пользователя при поиске")
  void shouldNormalizeUsernameWhenFinding() {
    User user = new User("testuser", PasswordHasher.hash("password123"));
    userService.saveUser(user);

    Optional<User> foundUser1 = userService.findUserByUsername("TESTUSER");
    Optional<User> foundUser2 = userService.findUserByUsername("TestUser");

    assertTrue(foundUser1.isPresent());
    assertTrue(foundUser2.isPresent());
    assertEquals("testuser", foundUser1.get().getUsername());
  }

  @Test
  @DisplayName("Пользователи с разным регистром считаются одинаковыми")
  void shouldTreatUsersWithDifferentCaseAsSame() {
    User user1 = new User("TestUser", PasswordHasher.hash("password123"));
    userService.saveUser(user1);

    assertTrue(userService.userExists("testuser"));
    assertTrue(userService.userExists("TESTUSER"));
    assertTrue(userService.userExists("TestUser"));
  }
}
