package rpg.utils;

import java.util.Scanner;

public class ConsoleHelpers {
    private static final Scanner scanner = new Scanner(System.in);

    public static void printHeader(String title) {
        System.out.println("\n=== " + title + " ===");
    }

    public static int getMenuChoice(String[] options) {
        for (int i = 0; i < options.length; i++) {
            System.out.printf("%d. %s%n", i+1, options[i]);
        }
        return getIntInput(1, options.length);
    }

    public static int getIntInput(int min, int max) {
        while (true) {
            try {
                int input = Integer.parseInt(scanner.nextLine());
                if (input >= min && input <= max) return input;
                System.out.printf("Please enter a number between %d and %d%n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
            }
        }
    }
}
