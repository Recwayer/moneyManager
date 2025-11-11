package ru.example.repository;

import java.util.Optional;
import ru.example.domain.User;

public interface UserRepository {
  void save(User user);

  Optional<User> findByUsername(String username);

  boolean existsByUsername(String username);

  void delete(String username);

  void saveAll(Iterable<User> users);
}
