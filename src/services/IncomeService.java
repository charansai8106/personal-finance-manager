package services;

import models.Income;
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
 * Provides all business logic related to managing a user's income
 * records: adding, updating, deleting, viewing and searching.
 *
 * @author Personal Finance Manager Team
 * @version 1.0
 */
public class IncomeService {

    /** Handles reading/writing income data to disk. */
    private final FileManager fileManager;

    /** In-memory list of all income records for all users. */
    private final List<Income> incomes;

    /** Counter used to generate unique income IDs. */
    private int nextId;

    /**
     * Constructs a new IncomeService, loading any previously saved
     * income records from disk.
     *
     * @param fileManager the file manager used for persistence
     */
    public IncomeService(FileManager fileManager) {
        this.fileManager = fileManager;
        this.incomes = new ArrayList<>(fileManager.loadIncomes());
        this.nextId = calculateNextId();
    }

    /**
     * Calculates the next available unique ID based on existing records.
     *
     * @return the next unique income ID to use
     */
    private int calculateNextId() {
        int max = 0;
        for (Income income : incomes) {
            if (income.getId() > max) {
                max = income.getId();
            }
        }
        return max + 1;
    }

    /**
     * Prompts the user for income details and adds a new income record.
     *
     * @param scanner  the {@link Scanner} used to read console input
     * @param username the username of the currently logged-in user
     */
    public void addIncome(Scanner scanner, String username) {
        System.out.println("\n--- Add Income ---");
        String source = InputValidator.readNonEmptyString(scanner, "Enter income source (e.g. Salary): ");
        double amount = InputValidator.readPositiveDouble(scanner, "Enter amount: ");
        LocalDate date = InputValidator.readDate(scanner, "Enter date of income");
        String description = InputValidator.readOptionalString(scanner, "Enter description (optional): ");

        Income income = new Income(nextId++, username, source, amount, date, description);
        incomes.add(income);
        fileManager.saveIncomes(incomes);

        System.out.println("Income added successfully! (ID: " + income.getId() + ")");
    }

    /**
     * Prompts the user to select one of their income records and update its details.
     *
     * @param scanner  the {@link Scanner} used to read console input
     * @param username the username of the currently logged-in user
     */
    public void updateIncome(Scanner scanner, String username) {
        System.out.println("\n--- Update Income ---");
        viewIncomes(username);
        int id = InputValidator.readInt(scanner, "Enter the Income ID to update: ");

        Income income = findIncomeByIdAndUser(id, username);
        if (income == null) {
            System.out.println("No income record found with that ID.");
            return;
        }

        String source = InputValidator.readNonEmptyString(scanner, "Enter new source: ");
        double amount = InputValidator.readPositiveDouble(scanner, "Enter new amount: ");
        LocalDate date = InputValidator.readDate(scanner, "Enter new date");
        String description = InputValidator.readOptionalString(scanner, "Enter new description (optional): ");

        income.setSource(source);
        income.setAmount(amount);
        income.setDate(date);
        income.setDescription(description);

        fileManager.saveIncomes(incomes);
        System.out.println("Income updated successfully!");
    }

    /**
     * Prompts the user to select one of their income records and delete it.
     *
     * @param scanner  the {@link Scanner} used to read console input
     * @param username the username of the currently logged-in user
     */
    public void deleteIncome(Scanner scanner, String username) {
        System.out.println("\n--- Delete Income ---");
        viewIncomes(username);
        int id = InputValidator.readInt(scanner, "Enter the Income ID to delete: ");

        Income income = findIncomeByIdAndUser(id, username);
        if (income == null) {
            System.out.println("No income record found with that ID.");
            return;
        }

        boolean confirm = InputValidator.readYesNo(scanner, "Are you sure you want to delete this income?");
        if (confirm) {
            incomes.remove(income);
            fileManager.saveIncomes(incomes);
            System.out.println("Income deleted successfully!");
        } else {
            System.out.println("Delete cancelled.");
        }
    }

    /**
     * Displays all income records belonging to the given user, sorted by date.
     *
     * @param username the username of the currently logged-in user
     */
    public void viewIncomes(String username) {
        List<Income> userIncomes = getIncomesByUser(username);
        if (userIncomes.isEmpty()) {
            System.out.println("No income records found.");
            return;
        }
        Collections.sort(userIncomes);
        System.out.println("\n---------------------------------------------------------------");
        System.out.printf("%-5s %-15s %-15s %-12s %-20s%n", "ID", "Source", "Amount", "Date", "Description");
        System.out.println("---------------------------------------------------------------");
        for (Income income : userIncomes) {
            System.out.printf("%-5d %-15s %-15s %-12s %-20s%n",
                    income.getId(), income.getSource(), CurrencyFormatter.format(income.getAmount()),
                    DateUtil.formatDate(income.getDate()), income.getDescription());
        }
        System.out.println("---------------------------------------------------------------");
    }

    /**
     * Searches the given user's income records by a keyword found in the
     * source or description fields.
     *
     * @param scanner  the {@link Scanner} used to read console input
     * @param username the username of the currently logged-in user
     */
    public void searchIncome(Scanner scanner, String username) {
        System.out.println("\n--- Search Income ---");
        String keyword = InputValidator.readNonEmptyString(scanner, "Enter keyword to search (source/description): ");
        List<Income> results = new ArrayList<>();
        for (Income income : getIncomesByUser(username)) {
            if (income.getSource().toLowerCase().contains(keyword.toLowerCase())
                    || income.getDescription().toLowerCase().contains(keyword.toLowerCase())) {
                results.add(income);
            }
        }
        printIncomeList(results);
    }

    /**
     * Sorts and displays the given user's incomes by amount (highest first).
     *
     * @param username the username of the currently logged-in user
     */
    public void sortByAmount(String username) {
        List<Income> userIncomes = getIncomesByUser(username);
        userIncomes.sort(Comparator.comparingDouble(Income::getAmount).reversed());
        printIncomeList(userIncomes);
    }

    /**
     * Returns all income records belonging to the given user.
     *
     * @param username the username to filter by
     * @return a list of the user's income records
     */
    public List<Income> getIncomesByUser(String username) {
        List<Income> result = new ArrayList<>();
        for (Income income : incomes) {
            if (income.getUsername().equalsIgnoreCase(username)) {
                result.add(income);
            }
        }
        return result;
    }

    /**
     * Calculates the total income for the given user.
     *
     * @param username the username to calculate totals for
     * @return the sum of all income amounts for that user
     */
    public double getTotalIncome(String username) {
        double total = 0;
        for (Income income : getIncomesByUser(username)) {
            total += income.getAmount();
        }
        return total;
    }

    /**
     * Prints a formatted list of income records to the console.
     *
     * @param list the list of income records to print
     */
    public void printIncomeList(List<Income> list) {
        if (list.isEmpty()) {
            System.out.println("No matching income records found.");
            return;
        }
        System.out.println("\n---------------------------------------------------------------");
        System.out.printf("%-5s %-15s %-15s %-12s %-20s%n", "ID", "Source", "Amount", "Date", "Description");
        System.out.println("---------------------------------------------------------------");
        for (Income income : list) {
            System.out.printf("%-5d %-15s %-15s %-12s %-20s%n",
                    income.getId(), income.getSource(), CurrencyFormatter.format(income.getAmount()),
                    DateUtil.formatDate(income.getDate()), income.getDescription());
        }
        System.out.println("---------------------------------------------------------------");
    }

    /**
     * Finds an income record by ID that belongs to the given user.
     *
     * @param id       the income ID to search for
     * @param username the username that must own the record
     * @return the matching {@link Income}, or {@code null} if not found
     */
    private Income findIncomeByIdAndUser(int id, String username) {
        for (Income income : incomes) {
            if (income.getId() == id && income.getUsername().equalsIgnoreCase(username)) {
                return income;
            }
        }
        return null;
    }
}
