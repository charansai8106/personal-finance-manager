package utils;

import java.time.LocalDate;
import java.util.Scanner;

/**
 * Utility class responsible for reading and validating user input from the
 * console. Centralizing input validation here ensures the rest of the
 * application never crashes due to bad user input (invalid numbers,
 * malformed dates, empty text, etc.).
 *
 * @author Personal Finance Manager Team
 * @version 1.0
 */
public final class InputValidator {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private InputValidator() {
    }

    /**
     * Repeatedly prompts the user until a valid integer is entered.
     *
     * @param scanner the {@link Scanner} used to read console input
     * @param prompt  the message displayed to the user
     * @return a valid integer entered by the user
     */
    public static int readInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a whole number.");
            }
        }
    }

    /**
     * Repeatedly prompts the user until an integer within the given
     * inclusive range is entered.
     *
     * @param scanner the {@link Scanner} used to read console input
     * @param prompt  the message displayed to the user
     * @param min     the minimum acceptable value (inclusive)
     * @param max     the maximum acceptable value (inclusive)
     * @return a valid integer within the given range
     */
    public static int readIntInRange(Scanner scanner, String prompt, int min, int max) {
        while (true) {
            int value = readInt(scanner, prompt);
            if (value >= min && value <= max) {
                return value;
            }
            System.out.println("Please enter a value between " + min + " and " + max + ".");
        }
    }

    /**
     * Repeatedly prompts the user until a valid positive double is entered.
     *
     * @param scanner the {@link Scanner} used to read console input
     * @param prompt  the message displayed to the user
     * @return a valid positive double entered by the user
     */
    public static double readPositiveDouble(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                double value = Double.parseDouble(input);
                if (value <= 0) {
                    System.out.println("Please enter an amount greater than zero.");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid numeric amount.");
            }
        }
    }

    /**
     * Repeatedly prompts the user until a non-empty string is entered.
     *
     * @param scanner the {@link Scanner} used to read console input
     * @param prompt  the message displayed to the user
     * @return a non-empty, trimmed string entered by the user
     */
    public static String readNonEmptyString(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Input cannot be empty. Please try again.");
        }
    }

    /**
     * Reads a string from the console, allowing it to be empty
     * (used for optional fields such as descriptions).
     *
     * @param scanner the {@link Scanner} used to read console input
     * @param prompt  the message displayed to the user
     * @return the string entered by the user, possibly empty
     */
    public static String readOptionalString(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    /**
     * Repeatedly prompts the user until a valid date in dd-MM-yyyy format is entered.
     *
     * @param scanner the {@link Scanner} used to read console input
     * @param prompt  the message displayed to the user
     * @return a valid {@link LocalDate} entered by the user
     */
    public static LocalDate readDate(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt + " (dd-MM-yyyy): ");
            String input = scanner.nextLine().trim();
            if (DateUtil.isValidDate(input)) {
                return DateUtil.parseDate(input);
            }
            System.out.println("Invalid date format. Please use dd-MM-yyyy (e.g. 25-12-2025).");
        }
    }

    /**
     * Repeatedly prompts the user until a "yes" or "no" answer is entered.
     *
     * @param scanner the {@link Scanner} used to read console input
     * @param prompt  the message displayed to the user
     * @return {@code true} if the user answered yes, {@code false} if no
     */
    public static boolean readYesNo(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt + " (Y/N): ");
            String input = scanner.nextLine().trim().toUpperCase();
            if (input.equals("Y") || input.equals("YES")) {
                return true;
            }
            if (input.equals("N") || input.equals("NO")) {
                return false;
            }
            System.out.println("Please enter Y or N.");
        }
    }
}
