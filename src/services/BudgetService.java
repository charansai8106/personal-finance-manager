package services;

import models.Budget;
import storage.FileManager;
import utils.CurrencyFormatter;
import utils.DateUtil;
import utils.InputValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Provides business logic for setting and tracking a user's monthly budget,
 * including remaining budget calculations and overspending warnings.
 *
 * @author Personal Finance Manager Team
 * @version 1.0
 */
public class BudgetService {

    /** Handles reading/writing budget data to disk. */
    private final FileManager fileManager;

    /** In-memory list of all budget records for all users. */
    private final List<Budget> budgets;

    /** Counter used to generate unique budget IDs. */
    private int nextId;

    /**
     * Constructs a new BudgetService, loading any previously saved
     * budget records from disk.
     *
     * @param fileManager the file manager used for persistence
     */
    public BudgetService(FileManager fileManager) {
        this.fileManager = fileManager;
        this.budgets = new ArrayList<>(fileManager.loadBudgets());
        this.nextId = calculateNextId();
    }

    /**
     * Calculates the next available unique ID based on existing records.
     *
     * @return the next unique budget ID to use
     */
    private int calculateNextId() {
        int max = 0;
        for (Budget budget : budgets) {
            if (budget.getBudgetId() > max) {
                max = budget.getBudgetId();
            }
        }
        return max + 1;
    }

    /**
     * Prompts the user to set (or update) their budget for a given month and year.
     *
     * @param scanner  the {@link Scanner} used to read console input
     * @param username the username of the currently logged-in user
     */
    public void setMonthlyBudget(Scanner scanner, String username) {
        System.out.println("\n--- Set Monthly Budget ---");
        int month = InputValidator.readIntInRange(scanner, "Enter month (1-12): ", 1, 12);
        int year = InputValidator.readIntInRange(scanner, "Enter year (e.g. 2026): ", 2000, 2100);
        double limit = InputValidator.readPositiveDouble(scanner, "Enter monthly budget amount: ");

        Budget existing = findBudget(username, month, year);
        if (existing != null) {
            existing.setMonthlyLimit(limit);
            System.out.println("Budget updated for " + DateUtil.getMonthName(month) + " " + year + ".");
        } else {
            Budget budget = new Budget(nextId++, username, month, year, limit);
            budgets.add(budget);
            System.out.println("Budget set for " + DateUtil.getMonthName(month) + " " + year + ".");
        }
        fileManager.saveBudgets(budgets);
    }

    /**
     * Finds the budget record for a given user, month and year.
     *
     * @param username the username to search for
     * @param month    the month to search for (1-12)
     * @param year     the year to search for
     * @return the matching {@link Budget}, or {@code null} if none is set
     */
    public Budget findBudget(String username, int month, int year) {
        for (Budget budget : budgets) {
            if (budget.getUsername().equalsIgnoreCase(username)
                    && budget.getMonth() == month
                    && budget.getYear() == year) {
                return budget;
            }
        }
        return null;
    }

    /**
     * Displays the remaining budget for a given month, along with a warning
     * if spending is close to or has exceeded the limit.
     *
     * @param scanner       the {@link Scanner} used to read console input
     * @param username      the username of the currently logged-in user
     * @param expenseService the expense service used to calculate spending
     */
    public void showRemainingBudget(Scanner scanner, String username, ExpenseService expenseService) {
        System.out.println("\n--- Remaining Budget ---");
        int month = InputValidator.readIntInRange(scanner, "Enter month (1-12): ", 1, 12);
        int year = InputValidator.readIntInRange(scanner, "Enter year (e.g. 2026): ", 2000, 2100);

        Budget budget = findBudget(username, month, year);
        if (budget == null) {
            System.out.println("No budget has been set for " + DateUtil.getMonthName(month) + " " + year + ".");
            return;
        }

        double spent = expenseService.getMonthlyExpenseTotal(username, month, year);
        double remaining = budget.getMonthlyLimit() - spent;

        System.out.println("Budget for " + DateUtil.getMonthName(month) + " " + year + ": " + CurrencyFormatter.format(budget.getMonthlyLimit()));
        System.out.println("Total Spent: " + CurrencyFormatter.format(spent));
        System.out.println("Remaining: " + CurrencyFormatter.format(remaining));

        printBudgetStatus(budget.getMonthlyLimit(), spent);
    }

    /**
     * Prints a warning or exceeded message based on how much of the budget
     * has been used.
     *
     * @param limit the monthly budget limit
     * @param spent the total amount spent so far
     */
    public void printBudgetStatus(double limit, double spent) {
        double usedPercentage = (spent / limit) * 100;
        if (spent > limit) {
            System.out.println("BUDGET EXCEEDED! You have spent " + CurrencyFormatter.format(spent - limit) + " more than your budget.");
        } else if (usedPercentage >= 80) {
            System.out.println("WARNING: You have used " + String.format("%.1f", usedPercentage) + "% of your monthly budget!");
        } else {
            System.out.println("You are within your budget. Keep it up!");
        }
    }
}
