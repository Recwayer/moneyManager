package ru.example.service;

import ru.example.domain.User;

public interface AuthService {

  User register(String username, String password);

  User login(String username, String password);

  void logout();

  User getCurrentUser();

  void saveCurrentUser();

  void checkAuthentication();
}
