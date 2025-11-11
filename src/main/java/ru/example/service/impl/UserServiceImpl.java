package ru.example.service.impl;

import static ru.example.util.normalizer.UserNormalizer.normalizeUsername;

import java.util.List;
import java.util.Optional;
import ru.example.domain.User;
import ru.example.repository.UserRepository;
import ru.example.service.UserService;

public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;

  public UserServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public void saveUser(User user) {
    String normalizedUsername = normalizeUsername(user.getUsername());
    User normalizedUser =
        new User(
            normalizedUsername,
            user.getPasswordHash(),
            user.getCreatedAt(),
            user.getWallet(),
            user.getCustomCategories());
    userRepository.save(normalizedUser);
  }

  @Override
  public Optional<User> findUserByUsername(String username) {
    String normalizedUsername = normalizeUsername(username);
    return userRepository.findByUsername(normalizedUsername);
  }

  @Override
  public boolean userExists(String username) {
    String normalizedUsername = normalizeUsername(username);
    return userRepository.existsByUsername(normalizedUsername);
  }

  @Override
  public void deleteUser(String username) {
    String normalizedUsername = normalizeUsername(username);
    userRepository.delete(normalizedUsername);
  }

  @Override
  public void saveAllUsers(List<User> list) {
    userRepository.saveAll(list);
  }
}
