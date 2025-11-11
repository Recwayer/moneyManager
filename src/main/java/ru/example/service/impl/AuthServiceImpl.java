package ru.example.service.impl;

import java.util.ArrayList;
import ru.example.domain.User;
import ru.example.service.AuthService;
import ru.example.service.CategoryService;
import ru.example.service.UserService;
import ru.example.util.auth.PasswordHasher;
import ru.example.util.fileio.FileServiceUtil;
import ru.example.util.validation.ValidatorUtil;

public class AuthServiceImpl implements AuthService {
  private final UserService userService;
  private final CategoryService categoryService;
  private User currentUser;

  public AuthServiceImpl(UserService userService, CategoryService categoryService) {
    this.userService = userService;
    this.categoryService = categoryService;
  }

  @Override
  public User register(String username, String password) {
    ValidatorUtil.validateUsername(username);
    ValidatorUtil.validatePassword(password);

    if (userService.userExists(username)) {
      throw new IllegalArgumentException("Пользователь с таким именем уже существует");
    }

    String passwordHash = PasswordHasher.hash(password);
    User user = new User(username, passwordHash);
    userService.saveUser(user);
    this.currentUser = user;
    categoryService.setUserCategories(user.getCustomCategories());
    FileServiceUtil.saveUser(user);
    return user;
  }

  @Override
  public User login(String username, String password) {
    ValidatorUtil.validateUsername(username);
    ValidatorUtil.validatePassword(password);

    User user =
        userService
            .findUserByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));

    if (!PasswordHasher.verify(password, user.getPasswordHash())) {
      throw new IllegalArgumentException("Неверный пароль");
    }

    this.currentUser = user;
    categoryService.setUserCategories(user.getCustomCategories());
    return user;
  }

  @Override
  public void logout() {
    if (currentUser != null) {
      FileServiceUtil.saveUser(currentUser);
    }
    currentUser = null;
    categoryService.setUserCategories(new ArrayList<>());
  }

  @Override
  public User getCurrentUser() {
    if (currentUser == null) {
      throw new IllegalStateException("Пользователь не авторизован");
    }
    return currentUser;
  }

  @Override
  public void saveCurrentUser() {
    if (currentUser != null) {
      FileServiceUtil.saveUser(currentUser);
      currentUser.getCustomCategories().clear();
      currentUser.getCustomCategories().addAll(categoryService.getUserCategories());
    }
  }

  @Override
  public void checkAuthentication() {
    if (currentUser == null) {
      throw new IllegalStateException("Пользователь не авторизован");
    }
  }
}
