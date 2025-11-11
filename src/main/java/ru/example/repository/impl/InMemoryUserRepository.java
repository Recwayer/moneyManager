package ru.example.repository.impl;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import ru.example.domain.User;
import ru.example.repository.UserRepository;

public class InMemoryUserRepository implements UserRepository {
  private final Map<String, User> users = new ConcurrentHashMap<>();

  @Override
  public void save(User user) {
    users.put(user.getUsername(), user);
  }

  @Override
  public Optional<User> findByUsername(String username) {
    return Optional.ofNullable(users.get(username));
  }

  @Override
  public boolean existsByUsername(String username) {
    return users.containsKey(username);
  }

  @Override
  public void delete(String username) {
    users.remove(username);
  }

  public void saveAll(Iterable<User> iterable) {
    iterable.forEach(this::save);
  }
}
