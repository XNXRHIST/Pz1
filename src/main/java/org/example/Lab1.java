import java.util.Random;
import java.util.Scanner;

public class Lab1 {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                try {
                    System.out.print("Введіть розмір масиву: ");
                    int size = scanner.nextInt();

                    if (size <= 0) {
                        System.out.println("Розмір масиву повинен бути більшим за нуль.");
                        continue;
                    }

                    // Генерація та виведення масиву
                    int[] array = generateRandomArray(size);
                    printArray(array);

                    // Обчислення максимального елемента
                    int maxElement = findMaxElement(array);
                    System.out.println("Максимальний елемент масиву: " + formatNumber(maxElement, "yellow"));

                    // Обчислення добутку елементів між першим і останнім від’ємними елементами
                    long product = findProductBetweenNegatives(array);
                    System.out.println("Добуток елементів між першим і останнім від’ємними елементами: " + formatNumber(product, "yellow"));
                } catch (java.util.InputMismatchException e) {
                    System.out.println("Некоректно введена інформація. Спробуйте ще раз...");
                    scanner.next(); // Очищає некоректне введення
                    continue;
                }

                // Запит користувача про наступні дії
                System.out.println("Оберіть дію:");
                System.out.println("1. Зробити обчислення заново");
                System.out.println("2. Запустити юніт тести");
                System.out.println("3. Завершити програму");
                System.out.println("4. Запустити тест, що гарантовано провалиться");

                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        continue; // Повторити цикл і зробити обчислення заново
                    case 2:
                        runUnitTests(); // Запустити юніт тести
                        break;
                    case 3:
                        System.out.println("Завершення програми...");
                        return;
                    case 4:
                        runFailingUnitTest(); // Запустити спеціальний провальний тест
                        break;
                    default:
                        System.out.println("Невірний вибір, спробуйте ще раз.");
                }
            }
        } catch (Exception e) {
            System.out.println("Помилка: " + e.getMessage());
        }
    }

    // Метод для генерації випадкового масиву
    public static int[] generateRandomArray(int size) {
        Random random = new Random();
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(201) - 100; // Генерація числа від -100 до 100
        }
        return array;
    }

    // Метод для виведення масиву
    public static void printArray(int[] array) {
        System.out.print("Масив: ");
        for (int num : array) {
            System.out.print(formatNumber(num, "yellow") + " ");
        }
        System.out.println();
    }

    // Метод для формату чисел у певний колір
    public static String formatNumber(Object number, String color) {
        String colorCode = "";
        switch (color) {
            case "yellow":
                colorCode = "\u001B[33m"; // Жовтий
                break;
            case "green":
                colorCode = "\u001B[32m"; // Зелений
                break;
            case "red":
                colorCode = "\u001B[31m"; // Червоний
                break;
        }
        return colorCode + number + "\u001B[0m"; // Відновлюємо стандартний колір
    }

    // Метод для знаходження максимального елемента в масиві
    public static int findMaxElement(int[] array) {
        int max = array[0];
        for (int num : array) {
            if (num > max) {
                max = num;
            }
        }
        return max;
    }

    // Метод для обчислення добутку елементів між першим і останнім від’ємними елементами
    public static long findProductBetweenNegatives(int[] array) {
        int firstNegativeIndex = -1;
        int lastNegativeIndex = -1;

        // Знаходимо перший та останній від'ємний елемент
        for (int i = 0; i < array.length; i++) {
            if (array[i] < 0) {
                if (firstNegativeIndex == -1) {
                    firstNegativeIndex = i;
                }
                lastNegativeIndex = i;
            }
        }

        // Якщо від'ємних елементів немає або їх менше 2
        if (firstNegativeIndex == -1 || lastNegativeIndex == -1 || firstNegativeIndex == lastNegativeIndex) {
            throw new IllegalStateException("Неможливо обчислити добуток, оскільки не вистачає від'ємних елементів.");
        }

        // Обчислення добутку елементів між ними
        long product = 1;
        for (int i = firstNegativeIndex + 1; i < lastNegativeIndex; i++) {
            product *= array[i];
        }

        return product;
    }

    // Метод для запуску юніт тестів
    public static void runUnitTests() {
        System.out.println(formatNumber("Запуск юніт тестів...", "yellow"));
        try {
            // Використовуємо абсолютний шлях до mvn
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "E:\\apache-maven-3.9.9\\bin\\mvn.cmd", "test");

            processBuilder.inheritIO(); // Виводити результати тестів в консоль
            Process process = processBuilder.start();
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                System.out.println(formatNumber("Тести пройшли успішно!", "green"));
            } else {
                System.out.println(formatNumber("Тести не пройшли. Код помилки: " + exitCode, "red"));
            }

        } catch (Exception e) {
            System.out.println(formatNumber("Помилка при запуску тестів: " + e.getMessage(), "red"));
        }
    }

    // Метод для запуску тесту, що завжди провалюється
    public static void runFailingUnitTest() {
        System.out.println(formatNumber("Запуск тесту, що гарантовано провалиться...", "yellow"));
        try {
            // Використовуємо абсолютний шлях до mvn
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "E:\\apache-maven-3.9.9\\bin\\mvn.cmd", "test", "-Dtest=Lab1Test#failedTest");

            processBuilder.inheritIO(); // Виводити результати тестів в консоль
            Process process = processBuilder.start();
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                System.out.println(formatNumber("Тести пройшли успішно!", "green"));
            } else {
                System.out.println(formatNumber("Тести не пройшли. Код помилки: " + exitCode, "red"));
            }

        } catch (Exception e) {
            System.out.println(formatNumber("Помилка при запуску тестів: " + e.getMessage(), "red"));
        }
    }
}

