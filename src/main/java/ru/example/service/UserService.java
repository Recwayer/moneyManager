package ru.example.service;

import java.util.List;
import java.util.Optional;
import ru.example.domain.User;

public interface UserService {

  void saveUser(User user);

  Optional<User> findUserByUsername(String username);

  boolean userExists(String username);

  void deleteUser(String username);

  void saveAllUsers(List<User> list);
}
