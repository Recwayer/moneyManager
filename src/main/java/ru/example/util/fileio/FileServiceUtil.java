package ru.example.util.fileio;

import static ru.example.util.normalizer.UserNormalizer.normalizeUsername;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import ru.example.domain.User;

public class FileServiceUtil {
  private static final String DATA_DIR = "data";
  private static final String USER_FILE_PREFIX = "user_";

  static {
    try {
      Files.createDirectories(Paths.get(DATA_DIR));
    } catch (IOException e) {
      System.err.println("Ошибка создания директории данных: " + e.getMessage());
    }
  }

  private FileServiceUtil() {}

  public static void saveUser(User user) {
    String normalizedUsername = normalizeUsername(user.getUsername());
    String filename = DATA_DIR + "/" + USER_FILE_PREFIX + normalizedUsername + ".dat";
    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
      oos.writeObject(user);
      System.out.println("Данные пользователя " + user.getUsername() + " сохранены");
    } catch (IOException e) {
      System.err.println("Ошибка сохранения пользователя: " + e.getMessage());
    }
  }

  public static List<User> loadAllUsers() {
    List<User> users = new ArrayList<>();
    try {
      Path dataDir = Paths.get(DATA_DIR);
      if (!Files.exists(dataDir)) {
        return users;
      }

      List<Path> userFiles =
          Files.list(dataDir)
              .filter(path -> path.getFileName().toString().startsWith(USER_FILE_PREFIX))
              .filter(path -> path.getFileName().toString().endsWith(".dat"))
              .toList();

      for (Path userFile : userFiles) {
        try (ObjectInputStream ois =
            new ObjectInputStream(new FileInputStream(userFile.toFile()))) {
          User user = (User) ois.readObject();
          users.add(user);
        } catch (IOException | ClassNotFoundException e) {
          System.err.println(
              "Ошибка загрузки пользователя из файла " + userFile + ": " + e.getMessage());
        }
      }

      System.out.println("Всего загружено пользователей: " + users.size());
    } catch (IOException e) {
      System.err.println("Ошибка чтения директории данных: " + e.getMessage());
    }
    return users;
  }

  public static void exportToCsv(User user, String filename) {
    try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
      writer.println("Тип,Категория,Сумма,Дата");

      for (var transaction : user.getWallet().getTransactions()) {
        String type = transaction.isIncome() ? "Доход" : "Расход";
        writer.printf(
            "%s,%s,%.2f,%s\n",
            type, transaction.getCategory(), transaction.getAmount(), transaction.getDate());
      }
      System.out.println("Данные экспортированы в " + filename);
    } catch (IOException e) {
      System.err.println("Ошибка экспорта в CSV: " + e.getMessage());
    }
  }
}
