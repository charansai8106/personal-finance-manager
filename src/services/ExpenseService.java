package services;

import models.Expense;
import models.ExpenseCategory;
import storage.FileManager;
import utils.CurrencyFormatter;
import utils.DateUtil;
import utils.InputValidator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

/**
 * Provides all business logic related to managing a user's expense
 * records: adding, updating, deleting, viewing and searching.
 *
 * @author Personal Finance Manager Team
 * @version 1.0
 */
public class ExpenseService {

    /** Handles reading/writing expense data to disk. */
    private final FileManager fileManager;

    /** In-memory list of all expense records for all users. */
    private final List<Expense> expenses;

    /** Counter used to generate unique expense IDs. */
    private int nextId;

    /**
     * Constructs a new ExpenseService, loading any previously saved
     * expense records from disk.
     *
     * @param fileManager the file manager used for persistence
     */
    public ExpenseService(FileManager fileManager) {
        this.fileManager = fileManager;
        this.expenses = new ArrayList<>(fileManager.loadExpenses());
        this.nextId = calculateNextId();
    }

    /**
     * Calculates the next available unique ID based on existing records.
     *
     * @return the next unique expense ID to use
     */
    private int calculateNextId() {
        int max = 0;
        for (Expense expense : expenses) {
            if (expense.getId() > max) {
                max = expense.getId();
            }
        }
        return max + 1;
    }

    /**
     * Displays all available expense categories to the console.
     */
    private void displayCategories() {
        System.out.println("Available Categories:");
        ExpenseCategory[] categories = ExpenseCategory.values();
        for (int i = 0; i < categories.length; i++) {
            System.out.println((i + 1) + ". " + categories[i]);
        }
    }

    /**
     * Prompts the user to choose a valid expense category from the menu.
     *
     * @param scanner the {@link Scanner} used to read console input
     * @return the selected {@link ExpenseCategory}
     */
    private ExpenseCategory chooseCategory(Scanner scanner) {
        displayCategories();
        ExpenseCategory[] categories = ExpenseCategory.values();
        int choice = InputValidator.readIntInRange(scanner, "Choose a category (1-" + categories.length + "): ", 1, categories.length);
        return categories[choice - 1];
    }

    /**
     * Prompts the user for expense details and adds a new expense record.
     *
     * @param scanner  the {@link Scanner} used to read console input
     * @param username the username of the currently logged-in user
     */
    public void addExpense(Scanner scanner, String username) {
        System.out.println("\n--- Add Expense ---");
        ExpenseCategory category = chooseCategory(scanner);
        double amount = InputValidator.readPositiveDouble(scanner, "Enter amount: ");
        LocalDate date = InputValidator.readDate(scanner, "Enter date of expense");
        String description = InputValidator.readOptionalString(scanner, "Enter description (optional): ");

        Expense expense = new Expense(nextId++, username, category, amount, date, description);
        expenses.add(expense);
        fileManager.saveExpenses(expenses);

        System.out.println("Expense added successfully! (ID: " + expense.getId() + ")");
    }

    /**
     * Prompts the user to select one of their expense records and update its details.
     *
     * @param scanner  the {@link Scanner} used to read console input
     * @param username the username of the currently logged-in user
     */
    public void updateExpense(Scanner scanner, String username) {
        System.out.println("\n--- Update Expense ---");
        viewExpenses(username);
        int id = InputValidator.readInt(scanner, "Enter the Expense ID to update: ");

        Expense expense = findExpenseByIdAndUser(id, username);
        if (expense == null) {
            System.out.println("No expense record found with that ID.");
            return;
        }

        ExpenseCategory category = chooseCategory(scanner);
        double amount = InputValidator.readPositiveDouble(scanner, "Enter new amount: ");
        LocalDate date = InputValidator.readDate(scanner, "Enter new date");
        String description = InputValidator.readOptionalString(scanner, "Enter new description (optional): ");

        expense.setCategory(category);
        expense.setAmount(amount);
        expense.setDate(date);
        expense.setDescription(description);

        fileManager.saveExpenses(expenses);
        System.out.println("Expense updated successfully!");
    }

    /**
     * Prompts the user to select one of their expense records and delete it.
     *
     * @param scanner  the {@link Scanner} used to read console input
     * @param username the username of the currently logged-in user
     */
    public void deleteExpense(Scanner scanner, String username) {
        System.out.println("\n--- Delete Expense ---");
        viewExpenses(username);
        int id = InputValidator.readInt(scanner, "Enter the Expense ID to delete: ");

        Expense expense = findExpenseByIdAndUser(id, username);
        if (expense == null) {
            System.out.println("No expense record found with that ID.");
            return;
        }

        boolean confirm = InputValidator.readYesNo(scanner, "Are you sure you want to delete this expense?");
        if (confirm) {
            expenses.remove(expense);
            fileManager.saveExpenses(expenses);
            System.out.println("Expense deleted successfully!");
        } else {
            System.out.println("Delete cancelled.");
        }
    }

    /**
     * Displays all expense records belonging to the given user, sorted by date.
     *
     * @param username the username of the currently logged-in user
     */
    public void viewExpenses(String username) {
        List<Expense> userExpenses = getExpensesByUser(username);
        if (userExpenses.isEmpty()) {
            System.out.println("No expense records found.");
            return;
        }
        Collections.sort(userExpenses);
        printExpenseList(userExpenses);
    }

    /**
     * Searches the given user's expense records by a keyword found in the
     * category or description fields.
     *
     * @param scanner  the {@link Scanner} used to read console input
     * @param username the username of the currently logged-in user
     */
    public void searchExpense(Scanner scanner, String username) {
        System.out.println("\n--- Search Expense ---");
        String keyword = InputValidator.readNonEmptyString(scanner, "Enter keyword to search (category/description): ");
        List<Expense> results = new ArrayList<>();
        for (Expense expense : getExpensesByUser(username)) {
            if (expense.getCategory().name().toLowerCase().contains(keyword.toLowerCase())
                    || expense.getDescription().toLowerCase().contains(keyword.toLowerCase())) {
                results.add(expense);
            }
        }
        printExpenseList(results);
    }

    /**
     * Sorts and displays the given user's expenses by amount (highest first).
     *
     * @param username the username of the currently logged-in user
     */
    public void sortByAmount(String username) {
        List<Expense> userExpenses = getExpensesByUser(username);
        userExpenses.sort(Comparator.comparingDouble(Expense::getAmount).reversed());
        printExpenseList(userExpenses);
    }

    /**
     * Sorts and displays the given user's expenses by category name (alphabetically).
     *
     * @param username the username of the currently logged-in user
     */
    public void sortByCategory(String username) {
        List<Expense> userExpenses = getExpensesByUser(username);
        userExpenses.sort(Comparator.comparing(expense -> expense.getCategory().name()));
        printExpenseList(userExpenses);
    }

    /**
     * Sorts and displays the given user's expenses by date (oldest first).
     *
     * @param username the username of the currently logged-in user
     */
    public void sortByDate(String username) {
        List<Expense> userExpenses = getExpensesByUser(username);
        Collections.sort(userExpenses);
        printExpenseList(userExpenses);
    }

    /**
     * Returns all expense records belonging to the given user.
     *
     * @param username the username to filter by
     * @return a list of the user's expense records
     */
    public List<Expense> getExpensesByUser(String username) {
        List<Expense> result = new ArrayList<>();
        for (Expense expense : expenses) {
            if (expense.getUsername().equalsIgnoreCase(username)) {
                result.add(expense);
            }
        }
        return result;
    }

    /**
     * Calculates the total expenses for the given user.
     *
     * @param username the username to calculate totals for
     * @return the sum of all expense amounts for that user
     */
    public double getTotalExpense(String username) {
        double total = 0;
        for (Expense expense : getExpensesByUser(username)) {
            total += expense.getAmount();
        }
        return total;
    }

    /**
     * Calculates the total expenses for the given user within a specific month and year.
     *
     * @param username the username to calculate totals for
     * @param month    the month to filter by (1-12)
     * @param year     the year to filter by
     * @return the sum of matching expense amounts
     */
    public double getMonthlyExpenseTotal(String username, int month, int year) {
        double total = 0;
        for (Expense expense : getExpensesByUser(username)) {
            if (expense.getDate().getMonthValue() == month && expense.getDate().getYear() == year) {
                total += expense.getAmount();
            }
        }
        return total;
    }

    /**
     * Prints a formatted list of expense records to the console.
     *
     * @param list the list of expense records to print
     */
    public void printExpenseList(List<Expense> list) {
        if (list.isEmpty()) {
            System.out.println("No matching expense records found.");
            return;
        }
        System.out.println("\n-----------------------------------------------------------------------");
        System.out.printf("%-5s %-15s %-15s %-12s %-20s%n", "ID", "Category", "Amount", "Date", "Description");
        System.out.println("-----------------------------------------------------------------------");
        for (Expense expense : list) {
            System.out.printf("%-5d %-15s %-15s %-12s %-20s%n",
                    expense.getId(), expense.getCategory(), CurrencyFormatter.format(expense.getAmount()),
                    DateUtil.formatDate(expense.getDate()), expense.getDescription());
        }
        System.out.println("-----------------------------------------------------------------------");
    }

    /**
     * Finds an expense record by ID that belongs to the given user.
     *
     * @param id       the expense ID to search for
     * @param username the username that must own the record
     * @return the matching {@link Expense}, or {@code null} if not found
     */
    private Expense findExpenseByIdAndUser(int id, String username) {
        for (Expense expense : expenses) {
            if (expense.getId() == id && expense.getUsername().equalsIgnoreCase(username)) {
                return expense;
            }
        }
        return null;
    }
}
