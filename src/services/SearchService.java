package services;

import models.Expense;
import models.ExpenseCategory;
import models.Income;
import utils.InputValidator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Coordinates advanced searching across a user's income and expense
 * records by date, category, amount, or keyword.
 * <p>
 * This service composes {@link IncomeService} and {@link ExpenseService}
 * rather than duplicating their data, demonstrating good use of
 * composition between service classes.
 * </p>
 *
 * @author Personal Finance Manager Team
 * @version 1.0
 */
public class SearchService {

    /** Service providing access to income records. */
    private final IncomeService incomeService;

    /** Service providing access to expense records. */
    private final ExpenseService expenseService;

    /**
     * Constructs a new SearchService.
     *
     * @param incomeService  the income service to search within
     * @param expenseService the expense service to search within
     */
    public SearchService(IncomeService incomeService, ExpenseService expenseService) {
        this.incomeService = incomeService;
        this.expenseService = expenseService;
    }

    /**
     * Displays the search menu and routes the user to the chosen search type.
     *
     * @param scanner  the {@link Scanner} used to read console input
     * @param username the username of the currently logged-in user
     */
    public void searchMenu(Scanner scanner, String username) {
        System.out.println("\n--- Search Module ---");
        System.out.println("1. Search Expenses by Date");
        System.out.println("2. Search Expenses by Category");
        System.out.println("3. Search Expenses by Amount");
        System.out.println("4. Search Expenses by Keyword");
        System.out.println("5. Search Income by Date");
        System.out.println("6. Search Income by Amount");
        System.out.println("7. Search Income by Keyword");
        int choice = InputValidator.readIntInRange(scanner, "Enter your choice: ", 1, 7);

        switch (choice) {
            case 1 -> searchExpenseByDate(scanner, username);
            case 2 -> searchExpenseByCategory(scanner, username);
            case 3 -> searchExpenseByAmount(scanner, username);
            case 4 -> expenseService.searchExpense(scanner, username);
            case 5 -> searchIncomeByDate(scanner, username);
            case 6 -> searchIncomeByAmount(scanner, username);
            case 7 -> incomeService.searchIncome(scanner, username);
            default -> System.out.println("Invalid choice.");
        }
    }

    /**
     * Searches the given user's expenses for a specific date.
     *
     * @param scanner  the {@link Scanner} used to read console input
     * @param username the username of the currently logged-in user
     */
    private void searchExpenseByDate(Scanner scanner, String username) {
        LocalDate date = InputValidator.readDate(scanner, "Enter date to search");
        List<Expense> results = new ArrayList<>();
        for (Expense expense : expenseService.getExpensesByUser(username)) {
            if (expense.getDate().equals(date)) {
                results.add(expense);
            }
        }
        expenseService.printExpenseList(results);
    }

    /**
     * Searches the given user's expenses for a specific category.
     *
     * @param scanner  the {@link Scanner} used to read console input
     * @param username the username of the currently logged-in user
     */
    private void searchExpenseByCategory(Scanner scanner, String username) {
        System.out.println("Categories: FOOD, TRAVEL, SHOPPING, RENT, MEDICAL, ENTERTAINMENT, EDUCATION, OTHERS");
        String input = InputValidator.readNonEmptyString(scanner, "Enter category: ");
        ExpenseCategory category = ExpenseCategory.fromString(input);
        List<Expense> results = new ArrayList<>();
        for (Expense expense : expenseService.getExpensesByUser(username)) {
            if (expense.getCategory() == category) {
                results.add(expense);
            }
        }
        expenseService.printExpenseList(results);
    }

    /**
     * Searches the given user's expenses for an exact amount.
     *
     * @param scanner  the {@link Scanner} used to read console input
     * @param username the username of the currently logged-in user
     */
    private void searchExpenseByAmount(Scanner scanner, String username) {
        double amount = InputValidator.readPositiveDouble(scanner, "Enter amount to search: ");
        List<Expense> results = new ArrayList<>();
        for (Expense expense : expenseService.getExpensesByUser(username)) {
            if (Double.compare(expense.getAmount(), amount) == 0) {
                results.add(expense);
            }
        }
        expenseService.printExpenseList(results);
    }

    /**
     * Searches the given user's incomes for a specific date.
     *
     * @param scanner  the {@link Scanner} used to read console input
     * @param username the username of the currently logged-in user
     */
    private void searchIncomeByDate(Scanner scanner, String username) {
        LocalDate date = InputValidator.readDate(scanner, "Enter date to search");
        List<Income> results = new ArrayList<>();
        for (Income income : incomeService.getIncomesByUser(username)) {
            if (income.getDate().equals(date)) {
                results.add(income);
            }
        }
        incomeService.printIncomeList(results);
    }

    /**
     * Searches the given user's incomes for an exact amount.
     *
     * @param scanner  the {@link Scanner} used to read console input
     * @param username the username of the currently logged-in user
     */
    private void searchIncomeByAmount(Scanner scanner, String username) {
        double amount = InputValidator.readPositiveDouble(scanner, "Enter amount to search: ");
        List<Income> results = new ArrayList<>();
        for (Income income : incomeService.getIncomesByUser(username)) {
            if (Double.compare(income.getAmount(), amount) == 0) {
                results.add(income);
            }
        }
        incomeService.printIncomeList(results);
    }
}
