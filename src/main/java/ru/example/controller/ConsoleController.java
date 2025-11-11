package ru.example.controller;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import ru.example.domain.Budget;
import ru.example.domain.FinancialReport;
import ru.example.domain.User;
import ru.example.service.AuthService;
import ru.example.service.CategoryService;
import ru.example.service.UserService;
import ru.example.service.WalletService;
import ru.example.util.fileio.FileServiceUtil;

public class ConsoleController {
  private final AuthService authService;
  private final UserService userService;
  private final CategoryService categoryService;
  private final WalletService walletService;
  private final Scanner scanner;

  public ConsoleController(
      AuthService authService,
      UserService userService,
      CategoryService categoryService,
      WalletService walletService) {
    this.authService = authService;
    this.userService = userService;
    this.categoryService = categoryService;
    this.walletService = walletService;
    this.scanner = new Scanner(System.in);
  }

  public void start() {
    loadAllUsers();
    initializeUser();

    System.out.println("╔════════════════════════════════════════╗");
    System.out.println("║   Добро пожаловать в систему учета    ║");
    System.out.println("║           личных финансов!            ║");
    System.out.println("╚════════════════════════════════════════╝");
    System.out.println("Для справки введите '10' или 'help' в главном меню");

    while (true) {
      showMenu();
      String choice = scanner.nextLine().trim();

      try {
        switch (choice) {
          case "1":
          case "категории":
            manageCategories();
            break;
          case "2":
          case "бюджеты":
            manageBudgets();
            break;
          case "3":
          case "доход":
            addIncome();
            break;
          case "4":
          case "расход":
            addExpense();
            break;
          case "5":
          case "отчет":
            showReport();
            break;
          case "6":
          case "уведомления":
            showAlerts();
            break;
          case "7":
          case "перевод":
            transferMoney();
            break;
          case "8":
          case "экспорт":
            exportToCsv();
            break;
          case "9":
          case "сменить":
            switchUser();
            break;
          case "10":
          case "help":
          case "помощь":
            showHelp();
            break;
          case "0":
          case "выход":
          case "exit":
            System.out.println("Выход из системы...");
            authService.logout();
            scanner.close();
            return;
          default:
            System.out.println("Неверная команда. Введите 'help' для справки.");
        }
      } catch (Exception e) {
        System.out.println("Ошибка: " + e.getMessage());
      }
    }
  }

  private void showHelp() {
    System.out.println("╔════════════════════════════════════════════════════════════╗");
    System.out.println("║                      СПРАВКА ПО КОМАНДАМ                   ║");
    System.out.println("╠════════════════════════════════════════════════════════════╣");
    System.out.println("║ Основные команды:                                          ║");
    System.out.println("║  1 / категории    - Управление категориями                 ║");
    System.out.println("║  2 / бюджеты      - Управление бюджетами                   ║");
    System.out.println("║  3 / доход         - Добавить доход                        ║");
    System.out.println("║  4 / расход        - Добавить расход                       ║");
    System.out.println("║  5 / отчет         - Показать финансовый отчет             ║");
    System.out.println("║  6 / уведомления   - Показать уведомления                  ║");
    System.out.println("║  7 / перевод       - Перевод денег другому пользователю    ║");
    System.out.println("║  8 / экспорт       - Экспорт данных в CSV                  ║");
    System.out.println("║  9 / сменить       - Сменить пользователя                  ║");
    System.out.println("║ 10 / help / помощь - Показать эту справку                  ║");
    System.out.println("║  0 / выход / exit  - Выход из системы                      ║");
    System.out.println("╠════════════════════════════════════════════════════════════╣");
    System.out.println("║ Примеры использования:                                     ║");
    System.out.println("║ - Добавление дохода: введите '3', выберите категорию,      ║");
    System.out.println("║   затем введите сумму                                      ║");
    System.out.println("║ - Просмотр отчета: введите '5' для детального отчета       ║");
    System.out.println("╚════════════════════════════════════════════════════════════╝");
  }

  private void loadAllUsers() {
    List<User> users = FileServiceUtil.loadAllUsers();
    userService.saveAllUsers(users);
  }

  private void initializeUser() {
    while (true) {
      try {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║           АВТОРИЗАЦИЯ                  ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║ 1. Вход в систему                      ║");
        System.out.println("║ 2. Регистрация нового пользователя     ║");
        System.out.println("║ 3. Помощь                              ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.print("Выберите действие: ");

        String choice = scanner.nextLine().trim();

        switch (choice) {
          case "1":
          case "вход":
            login();
            return;
          case "2":
          case "регистрация":
            register();
            return;
          case "3":
          case "help":
          case "помощь":
            showAuthHelp();
            break;
          default:
            System.out.println(" Неверный выбор. Введите '1' для входа или '2' для регистрации.");
        }
      } catch (Exception e) {
        System.out.println(" Ошибка: " + e.getMessage());
        System.out.println("Попробуйте снова.");
      }
    }
  }

  private void showAuthHelp() {
    System.out.println("╔════════════════════════════════════════╗");
    System.out.println("║         ПОМОЩЬ: АВТОРИЗАЦИЯ            ║");
    System.out.println("╠════════════════════════════════════════╣");
    System.out.println("║ Требования к учетным данным:           ║");
    System.out.println("║ - Имя пользователя: 3-20 символов      ║");
    System.out.println("║   (только буквы, цифры, подчеркивания) ║");
    System.out.println("║ - Пароль: минимум 4 символа            ║");
    System.out.println("║                                        ║");
    System.out.println("║ Примеры имен пользователей:            ║");
    System.out.println("║ - user_2025                            ║");
    System.out.println("║ - andrey                               ║");
    System.out.println("║ - user123                              ║");
    System.out.println("╚════════════════════════════════════════╝");
  }

  private void showMenu() {
    System.out.println("╔════════════════════════════════════════╗");
    System.out.println("║          ЛИЧНЫЕ ФИНАНСЫ                ║");
    System.out.println("╠════════════════════════════════════════╣");
    System.out.println(
        " Текущий пользователь: " + authService.getCurrentUser().getUsername() + " ");
    System.out.println("╠════════════════════════════════════════╣");
    System.out.println("║ 1.  Управление категориями             ║");
    System.out.println("║ 2.  Управление бюджетами               ║");
    System.out.println("║ 3.  Добавить доход                     ║");
    System.out.println("║ 4.  Добавить расход                    ║");
    System.out.println("║ 5.  Показать отчет                     ║");
    System.out.println("║ 6.  Показать уведомления               ║");
    System.out.println("║ 7.  Перевести деньги                   ║");
    System.out.println("║ 8.  Экспорт данных                     ║");
    System.out.println("║ 9.  Сменить пользователя               ║");
    System.out.println("║ 10. Помощь (help)                      ║");
    System.out.println("║ 0.  Выход                              ║");
    System.out.println("╚════════════════════════════════════════╝");
    System.out.print("Выберите действие: ");
  }

  private void login() {
    try {
      System.out.print("Имя пользователя: ");
      String username = scanner.nextLine().trim();

      System.out.print("Пароль: ");
      String password = scanner.nextLine().trim();

      User user = authService.login(username, password);
      System.out.println("Успешный вход! Добро пожаловать, " + user.getUsername());
    } catch (Exception e) {
      System.out.println("Ошибка входа: " + e.getMessage());
      throw e;
    }
  }

  private void register() {
    try {
      System.out.print("Имя пользователя: ");
      String username = scanner.nextLine().trim();

      System.out.print("Пароль: ");
      String password = scanner.nextLine().trim();

      authService.register(username, password);
      System.out.println("Регистрация успешна! Добро пожаловать, " + username);
    } catch (Exception e) {
      System.out.println("Ошибка регистрации: " + e.getMessage());
      throw e;
    }
  }

  private void manageBudgets() {
    while (true) {
      try {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║         УПРАВЛЕНИЕ БЮДЖЕТАМИ           ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║ 1. Показать все бюджеты                ║");
        System.out.println("║ 2. Установить бюджет                   ║");
        System.out.println("║ 3. Редактировать бюджет                ║");
        System.out.println("║ 4. Удалить бюджет                      ║");
        System.out.println("║ 5. Назад в главное меню                ║");
        System.out.println("║ 6. Помощь                              ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.print("Выберите действие: ");

        String choice = scanner.nextLine().trim();

        switch (choice) {
          case "1":
          case "показать":
            showBudgets();
            break;
          case "2":
          case "установить":
            setBudget();
            break;
          case "3":
          case "редактировать":
            updateBudget();
            break;
          case "4":
          case "удалить":
            removeBudget();
            break;
          case "5":
          case "назад":
            return;
          case "6":
          case "help":
          case "помощь":
            showBudgetsHelp();
            break;
          default:
            System.out.println(" Неверный выбор. Введите 'help' для справки.");
        }
      } catch (Exception e) {
        System.out.println(" Ошибка: " + e.getMessage());
      }
    }
  }

  private void showBudgetsHelp() {
    System.out.println("╔════════════════════════════════════════╗");
    System.out.println("║   ПОМОЩЬ: УПРАВЛЕНИЕ БЮДЖЕТАМИ         ║");
    System.out.println("╠════════════════════════════════════════╣");
    System.out.println("║ Бюджеты помогают контролировать        ║");
    System.out.println("║ расходы по категориям:                 ║");
    System.out.println("║                                        ║");
    System.out.println("║ Статусы бюджетов:                      ║");
    System.out.println("║ - [OK] - использовано менее 50%        ║");
    System.out.println("║ - [НОРМА] - использовано 50-79%        ║");
    System.out.println("║ - [ВНИМАНИЕ] - использовано 80-99%     ║");
    System.out.println("║ - [ПРЕВЫШЕН] - 100% и более            ║");
    System.out.println("║                                        ║");
    System.out.println("║ Требования к бюджетам:                 ║");
    System.out.println("║ - Лимит должен быть положительным      ║");
    System.out.println("║ - Бюджет можно установить только       ║");
    System.out.println("║   для категорий расходов               ║");
    System.out.println("║ - Лимит не может быть меньше уже       ║");
    System.out.println("║   потраченной суммы                    ║");
    System.out.println("║                                        ║");
    System.out.println("║ Совет: устанавливайте реалистичные     ║");
    System.out.println("║ бюджеты на основе предыдущих           ║");
    System.out.println("║ расходов                               ║");
    System.out.println("╚════════════════════════════════════════╝");
  }

  private void manageCategories() {
    while (true) {
      try {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║        УПРАВЛЕНИЕ КАТЕГОРИЯМИ          ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║ 1. Показать все категории              ║");
        System.out.println("║ 2. Добавить категорию                  ║");
        System.out.println("║ 3. Удалить категорию                   ║");
        System.out.println("║ 4. Назад в главное меню                ║");
        System.out.println("║ 5. Помощь                              ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.print("Выберите действие: ");

        String choice = scanner.nextLine().trim();

        switch (choice) {
          case "1":
          case "показать":
            showAllCategories();
            break;
          case "2":
          case "добавить":
            addCategory();
            break;
          case "3":
          case "удалить":
            removeCategory();
            break;
          case "4":
          case "назад":
            return;
          case "5":
          case "help":
          case "помощь":
            showCategoriesHelp();
            break;
          default:
            System.out.println(" Неверный выбор. Введите 'help' для справки.");
        }
      } catch (Exception e) {
        System.out.println(" Ошибка: " + e.getMessage());
      }
    }
  }

  private void showCategoriesHelp() {
    System.out.println("╔════════════════════════════════════════╗");
    System.out.println("║   ПОМОЩЬ: УПРАВЛЕНИЕ КАТЕГОРИЯМИ       ║");
    System.out.println("╠════════════════════════════════════════╣");
    System.out.println("║ Категории используются для:            ║");
    System.out.println("║ - Группировки доходов и расходов       ║");
    System.out.println("║ - Установки бюджетов                   ║");
    System.out.println("║ - Анализа финансов                     ║");
    System.out.println("║                                        ║");
    System.out.println("║ Системные категории (нельзя удалить):  ║");
    System.out.println("║ - Доходы: Зарплата, Премия, и др.      ║");
    System.out.println("║ - Расходы: Еда, Транспорт, и др.       ║");
    System.out.println("║                                        ║");
    System.out.println("║ Требования к названию категории:       ║");
    System.out.println("║ - Не более 50 символов                 ║");
    System.out.println("║ - Только буквы, цифры и пробелы        ║");
    System.out.println("╚════════════════════════════════════════╝");
  }

  private void showAllCategories() {
    try {
      List<String> categories = categoryService.getAllCategories();
      List<String> expenseCategories = categoryService.getExpenseCategories();
      List<String> incomeCategories = categoryService.getIncomeCategories();

      System.out.println("Все категории");
      System.out.println("Доступные категории (" + categories.size() + "):");
      categories.forEach(cat -> System.out.println("  - " + cat));

      System.out.println("\nКатегории расходов (" + expenseCategories.size() + "):");
      expenseCategories.forEach(cat -> System.out.println("  - " + cat));

      System.out.println("\nКатегории доходов (" + incomeCategories.size() + "):");
      incomeCategories.forEach(cat -> System.out.println("  - " + cat));

    } catch (Exception e) {
      System.out.println("Ошибка получения категорий: " + e.getMessage());
    }
  }

  private void addCategory() {
    try {
      System.out.print("Введите название новой категории: ");
      String category = scanner.nextLine().trim();

      if (category.isEmpty()) {
        System.out.println("Ошибка: категория не может быть пустой");
        return;
      }

      categoryService.addCategory(category);
      authService.getCurrentUser().addCategory(category);
      authService.saveCurrentUser();

      System.out.println("Категория '" + category + "' успешно добавлена!");

    } catch (Exception e) {
      System.out.println("Ошибка добавления категории: " + e.getMessage());
    }
  }

  private void removeCategory() {
    try {
      showAllCategories();
      System.out.print("Введите название категории для удаления: ");
      String category = scanner.nextLine().trim();

      if (category.isEmpty()) {
        System.out.println("Ошибка: категория не может быть пустой");
        return;
      }

      System.out.print("Вы уверены, что хотите удалить категорию '" + category + "'? (y/N): ");
      String confirmation = scanner.nextLine().trim();

      if (confirmation.equalsIgnoreCase("y")) {
        categoryService.removeCategory(category);
        authService.getCurrentUser().removeCategory(category);
        authService.saveCurrentUser();

        System.out.println("Категория '" + category + "' успешно удалена!");
      } else {
        System.out.println("Удаление отменено.");
      }

    } catch (Exception e) {
      System.out.println("Ошибка удаления категории: " + e.getMessage());
    }
  }

  private void addIncome() {
    try {
      List<String> incomeCategories = categoryService.getIncomeCategories();
      if (incomeCategories.isEmpty()) {
        System.out.println("Нет доступных категорий доходов. Сначала добавьте категории.");
        return;
      }

      String category = selectCategory("доходов", incomeCategories);
      if (category == null) {
        return;
      }

      System.out.print("Сумма: ");
      String amountInput = scanner.nextLine().trim();
      double amount = parseDoubleSafe(amountInput);
      if (amount <= 0) {
        System.out.println("Ошибка: сумма должна быть положительной");
        return;
      }

      walletService.addIncome(category, amount);
      System.out.println("Доход успешно добавлен!");
    } catch (Exception e) {
      System.out.println("Ошибка добавления дохода: " + e.getMessage());
    }
  }

  private void addExpense() {
    try {
      List<String> expenseCategories = categoryService.getExpenseCategories();
      if (expenseCategories.isEmpty()) {
        System.out.println("Нет доступных категорий расходов. Сначала добавьте категории.");
        return;
      }

      String category = selectCategory("расходов", expenseCategories);
      if (category == null) {
        return;
      }

      System.out.print("Сумма: ");
      String amountInput = scanner.nextLine().trim();
      double amount = parseDoubleSafe(amountInput);
      if (amount <= 0) {
        System.out.println("Ошибка: сумма должна быть положительной");
        return;
      }

      walletService.addExpense(category, amount);
      System.out.println("Расход успешно добавлен!");
    } catch (Exception e) {
      System.out.println("Ошибка добавления расхода: " + e.getMessage());
    }
  }

  private void setBudget() {
    try {
      List<String> expenseCategories = categoryService.getExpenseCategories();
      if (expenseCategories.isEmpty()) {
        System.out.println("Нет доступных категорий расходов для установки бюджета.");
        return;
      }

      List<String> availableCategories =
          expenseCategories.stream()
              .filter(cat -> !walletService.getBudgetCategories().contains(cat))
              .toList();

      if (availableCategories.isEmpty()) {
        System.out.println("Для всех категорий расходов уже установлены бюджеты.");
        System.out.println(
            "Используйте редактирование бюджета для изменения существующих лимитов.");
        return;
      }

      System.out.println("Доступные категории для установки бюджета:");
      for (int i = 0; i < availableCategories.size(); i++) {
        System.out.printf("%d. %s\n", i + 1, availableCategories.get(i));
      }

      System.out.print("Выберите категорию (номер): ");
      String choice = scanner.nextLine().trim();

      int index;
      try {
        index = Integer.parseInt(choice) - 1;
        if (index < 0 || index >= availableCategories.size()) {
          System.out.println("Неверный номер категории.");
          return;
        }
      } catch (NumberFormatException e) {
        System.out.println("Неверный формат номера.");
        return;
      }

      String category = availableCategories.get(index);
      showBudgetInfo(category);

      System.out.print("Лимит бюджета: ");
      String limitInput = scanner.nextLine().trim();
      double limit = parseDoubleSafe(limitInput);

      if (limit <= 0) {
        System.out.println("Ошибка: лимит должен быть положительным");
        return;
      }

      walletService.setBudget(category, limit);
      System.out.println("Бюджет успешно установлен для категории '" + category + "'!");

    } catch (Exception e) {
      System.out.println("Ошибка установки бюджета: " + e.getMessage());
    }
  }

  private void updateBudget() {
    try {
      List<String> budgetCategories = walletService.getBudgetCategories();
      if (budgetCategories.isEmpty()) {
        System.out.println("Нет бюджетов для редактирования.");
        return;
      }

      String category = selectCategory("бюджетов для редактирования", budgetCategories);
      if (category == null) {
        return;
      }

      FinancialReport report = walletService.getFinancialReport();
      var budgetOpt =
          report.getBudgets().stream()
              .filter(b -> b.getCategory().equalsIgnoreCase(category))
              .findFirst();

      if (budgetOpt.isEmpty()) {
        System.out.println("Бюджет для категории '" + category + "' не найден");
        return;
      }

      var currentBudget = budgetOpt.get();
      System.out.printf("Текущий лимит для '%s': %.2f\n", category, currentBudget.getLimit());
      System.out.printf("Уже потрачено: %.2f\n", currentBudget.getSpent());

      showBudgetInfo(category);

      System.out.print("Новый лимит бюджета: ");
      String newLimitInput = scanner.nextLine().trim();
      double newLimit = parseDoubleSafe(newLimitInput);

      if (newLimit <= 0) {
        System.out.println("Ошибка: лимит должен быть положительным");
        return;
      }

      walletService.updateBudget(category, newLimit);
      System.out.println("Бюджет успешно обновлен!");

    } catch (Exception e) {
      System.out.println("Ошибка обновления бюджета: " + e.getMessage());
    }
  }

  private void removeBudget() {
    try {
      List<String> budgetCategories = walletService.getBudgetCategories();
      if (budgetCategories.isEmpty()) {
        System.out.println("Нет бюджетов для удаления.");
        return;
      }

      String category = selectCategory("бюджетов для удаления", budgetCategories);
      if (category == null) {
        return;
      }

      System.out.print(
          "Вы уверены, что хотите удалить бюджет для категории '" + category + "'? (y/N): ");
      String confirmation = scanner.nextLine().trim();

      if (confirmation.equalsIgnoreCase("y")) {
        walletService.removeBudget(category);
        System.out.println("Бюджет успешно удален!");
      } else {
        System.out.println("Удаление отменено.");
      }

    } catch (Exception e) {
      System.out.println("Ошибка удаления бюджета: " + e.getMessage());
    }
  }

  private String selectCategory(String categoryType, List<String> categories) {
    System.out.println("\nДоступные категории " + categoryType + ":");
    for (int i = 0; i < categories.size(); i++) {
      System.out.printf("%d. %s\n", i + 1, categories.get(i));
    }

    System.out.println("0. Отмена");
    System.out.print("Выберите категорию (номер): ");

    String choice = scanner.nextLine().trim();

    try {
      int index = Integer.parseInt(choice);
      if (index == 0) {
        System.out.println("Отмена выбора категории.");
        return null;
      }
      if (index < 1 || index > categories.size()) {
        System.out.println("Неверный номер категории. Попробуйте снова.");
        return selectCategory(categoryType, categories);
      }
      return categories.get(index - 1);
    } catch (NumberFormatException e) {
      String inputCategory = choice.trim();
      if (categories.contains(inputCategory)) {
        return inputCategory;
      } else {
        System.out.println("Категория '" + inputCategory + "' не найдена в списке.");
        System.out.print("Хотите добавить эту категорию? (y/N): ");
        String addChoice = scanner.nextLine().trim();
        if (addChoice.equalsIgnoreCase("y")) {
          try {
            categoryService.addCategory(inputCategory);
            authService.getCurrentUser().addCategory(inputCategory);
            System.out.println("Категория '" + inputCategory + "' успешно добавлена!");
            return inputCategory;
          } catch (Exception ex) {
            System.out.println("Ошибка добавления категории: " + ex.getMessage());
            return selectCategory(categoryType, categories);
          }
        } else {
          return selectCategory(categoryType, categories);
        }
      }
    }
  }

  private void showBudgetInfo(String category) {
    try {
      FinancialReport report = walletService.getFinancialReport();
      double currentSpending =
          report.getExpenseByCategory().entrySet().stream()
              .filter(entry -> entry.getKey().equalsIgnoreCase(category))
              .mapToDouble(Map.Entry::getValue)
              .sum();

      if (currentSpending > 0) {
        System.out.printf("Текущие расходы по категории '%s': %.2f\n", category, currentSpending);
        System.out.printf("Рекомендуемый минимальный лимит: %.2f\n", currentSpending);
      } else {
        System.out.println("По этой категории еще нет расходов.");
      }
    } catch (Exception e) {
      System.out.println("Ошибка получения информации о расходах: " + e.getMessage());
    }
  }

  private void showReport() {
    try {
      FinancialReport report = walletService.getFinancialReport();
      System.out.println(report);
    } catch (Exception e) {
      System.out.println("Ошибка получения отчета: " + e.getMessage());
    }
  }

  private void showAlerts() {
    try {
      FinancialReport report = walletService.getFinancialReport();
      var alerts = report.getAlerts();

      if (alerts.isEmpty()) {
        System.out.println("Уведомлений нет.");
      } else {
        System.out.println("Уведомления");
        alerts.forEach(alert -> System.out.println("- " + alert.getMessage()));
      }
    } catch (Exception e) {
      System.out.println("Ошибка получения уведомлений: " + e.getMessage());
    }
  }

  private void transferMoney() {
    try {
      System.out.print("Имя пользователя получателя: ");
      String toUsername = scanner.nextLine().trim();

      if (toUsername.isEmpty()) {
        System.out.println("Ошибка: имя пользователя не может быть пустым");
        return;
      }

      System.out.print("Сумма перевода: ");
      String amountInput = scanner.nextLine().trim();
      double amount = parseDoubleSafe(amountInput);
      if (amount <= 0) {
        System.out.println("Ошибка: сумма должна быть положительной");
        return;
      }

      walletService.transfer(toUsername, amount);
      System.out.println("Перевод успешно выполнен!");
    } catch (Exception e) {
      System.out.println("Ошибка перевода: " + e.getMessage());
    }
  }

  private void showBudgets() {
    try {
      List<String> budgetCategories = walletService.getBudgetCategories();

      if (budgetCategories.isEmpty()) {
        System.out.println("У вас нет установленных бюджетов.");
        return;
      }

      System.out.println("\nВаши бюджеты");
      FinancialReport report = walletService.getFinancialReport();
      List<Budget> budgets = report.getBudgets();

      if (budgets.isEmpty()) {
        System.out.println("Бюджеты не найдены в отчете.");
        return;
      }

      System.out.println("Всего бюджетов: " + budgets.size());
      System.out.println();

      for (int i = 0; i < budgets.size(); i++) {
        Budget budget = budgets.get(i);
        String status = getBudgetStatus(budget);

        System.out.printf("%d. %s\n", i + 1, budget.getCategory());
        System.out.printf("   Лимит: %.2f\n", budget.getLimit());
        System.out.printf("   Потрачено: %.2f\n", budget.getSpent());
        System.out.printf("   Осталось: %.2f\n", budget.getRemaining());
        System.out.printf(
            "   Использовано: %.1f%% %s\n", budget.getUtilizationPercentage(), status);
        System.out.println();
      }
    } catch (Exception e) {
      System.out.println("Ошибка получения бюджетов: " + e.getMessage());
    }
  }

  private String getBudgetStatus(Budget budget) {
    double percentage = budget.getUtilizationPercentage();
    if (percentage >= 100) {
      return "[ПРЕВЫШЕН]";
    } else if (percentage >= 80) {
      return "[ВНИМАНИЕ]";
    } else if (percentage >= 50) {
      return "[НОРМА]";
    } else {
      return "[OK]";
    }
  }

  private void exportToCsv() {
    try {
      System.out.print("Имя файла для экспорта (например: report.csv): ");
      String filename = scanner.nextLine().trim();

      if (filename.isEmpty()) {
        System.out.println("Ошибка: имя файла не может быть пустым");
        return;
      }

      if (!filename.endsWith(".csv")) {
        filename += ".csv";
      }

      FileServiceUtil.exportToCsv(authService.getCurrentUser(), filename);
      System.out.println("Данные успешно экспортированы в файл: " + filename);
    } catch (Exception e) {
      System.out.println("Ошибка экспорта: " + e.getMessage());
    }
  }

  private void switchUser() {
    try {
      authService.logout();
      initializeUser();
    } catch (Exception e) {
      System.out.println("Ошибка смены пользователя: " + e.getMessage());
    }
  }

  private double parseDoubleSafe(String input) {
    try {
      return Double.parseDouble(input);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Неверный формат числа: " + input);
    }
  }
}
