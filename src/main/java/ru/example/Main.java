package ru.example;

import ru.example.controller.ConsoleController;
import ru.example.repository.UserRepository;
import ru.example.repository.impl.InMemoryUserRepository;
import ru.example.service.AuthService;
import ru.example.service.CategoryService;
import ru.example.service.UserService;
import ru.example.service.WalletService;
import ru.example.service.impl.AuthServiceImpl;
import ru.example.service.impl.CategoryServiceImpl;
import ru.example.service.impl.UserServiceImpl;
import ru.example.service.impl.WalletServiceImpl;

public class Main {
  public static void main(String[] args) {
    try {
      CategoryService categoryService = new CategoryServiceImpl();
      UserRepository userRepository = new InMemoryUserRepository();
      UserService userService = new UserServiceImpl(userRepository);
      AuthService authService = new AuthServiceImpl(userService, categoryService);
      WalletService walletService =
          new WalletServiceImpl(userService, categoryService, authService);

      ConsoleController controller =
          new ConsoleController(authService, userService, categoryService, walletService);
      controller.start();

    } catch (Exception e) {
      System.err.println("Критическая ошибка при запуске приложения: " + e.getMessage());
      e.printStackTrace();
    }
  }
}
