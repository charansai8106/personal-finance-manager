import services.AuthenticationService;
import services.BudgetService;
import services.ExpenseService;
import services.IncomeService;
import services.ReportService;
import services.SearchService;
import storage.FileManager;
import utils.InputValidator;

import java.util.Scanner;

/**
 * Entry point for the Personal Finance Manager console application.
 * <p>
 * This class wires together all services (authentication, income,
 * expense, budget, report and search) and drives the main menu loop
 * that the user interacts with. All user input is validated so the
 * application never crashes on bad input.
 * </p>
 *
 * @author Personal Finance Manager Team
 * @version 1.0
 */
public class Main {

    /**
     * Application entry point.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Storage and services are created once and shared across the whole session.
        FileManager fileManager = new FileManager();
        AuthenticationService authService = new AuthenticationService(fileManager);
        IncomeService incomeService = new IncomeService(fileManager);
        ExpenseService expenseService = new ExpenseService(fileManager);
        BudgetService budgetService = new BudgetService(fileManager);
        ReportService reportService = new ReportService(incomeService, expenseService, fileManager);
        SearchService searchService = new SearchService(incomeService, expenseService);

        printWelcomeBanner();

        boolean running = true;
        while (running) {
            if (!authService.isLoggedIn()) {
                running = handleAuthMenu(scanner, authService);
                continue;
            }

            String username = authService.getCurrentUser().getUsername();
            printMainMenu(authService.getCurrentUser().getFullName());
            int choice = InputValidator.readIntInRange(scanner, "Enter your choice: ", 1, 10);

            switch (choice) {
                case 1 -> incomeService.addIncome(scanner, username);
                case 2 -> handleIncomeMenu(scanner, incomeService, username);
                case 3 -> expenseService.addExpense(scanner, username);
                case 4 -> handleExpenseMenu(scanner, expenseService, username);
                case 5 -> reportService.printSummaryReport(username);
                case 6 -> searchService.searchMenu(scanner, username);
                case 7 -> handleBudgetMenu(scanner, budgetService, expenseService, username);
                case 8 -> reportService.exportReport(username);
                case 9 -> authService.logout();
                case 10 -> running = false;
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }

        System.out.println("\nThank you for using Personal Finance Manager. Goodbye!");
        scanner.close();
    }

    /**
     * Prints the ASCII welcome banner shown when the application starts.
     */
    private static void printWelcomeBanner() {
        System.out.println("=========================================");
        System.out.println("      PERSONAL FINANCE MANAGER (v1.0)     ");
        System.out.println("=========================================");
    }

    /**
     * Handles the pre-login menu, offering Register, Login and Exit options.
     *
     * @param scanner     the {@link Scanner} used to read console input
     * @param authService the authentication service handling login/registration
     * @return {@code true} to keep the application running, {@code false} to exit
     */
    private static boolean handleAuthMenu(Scanner scanner, AuthenticationService authService) {
        System.out.println("\n1. Register");
        System.out.println("2. Login");
        System.out.println("3. Exit");
        int choice = InputValidator.readIntInRange(scanner, "Enter your choice: ", 1, 3);

        switch (choice) {
            case 1 -> authService.register(scanner);
            case 2 -> authService.login(scanner);
            case 3 -> {
                return false;
            }
            default -> System.out.println("Invalid choice. Please try again.");
        }
        return true;
    }

    /**
     * Prints the main menu shown to a logged-in user.
     *
     * @param fullName the full name of the logged-in user
     */
    private static void printMainMenu(String fullName) {
        System.out.println("\n===================== MAIN MENU =====================");
        System.out.println("Logged in as: " + fullName);
        System.out.println("1.  Add Income");
        System.out.println("2.  Manage Income (View / Update / Delete / Search / Sort)");
        System.out.println("3.  Add Expense");
        System.out.println("4.  Manage Expense (View / Update / Delete / Search / Sort)");
        System.out.println("5.  Reports");
        System.out.println("6.  Search");
        System.out.println("7.  Budget");
        System.out.println("8.  Export Report");
        System.out.println("9.  Logout");
        System.out.println("10. Exit");
        System.out.println("======================================================");
    }

    /**
     * Handles the income management sub-menu.
     *
     * @param scanner       the {@link Scanner} used to read console input
     * @param incomeService the service handling income operations
     * @param username      the username of the currently logged-in user
     */
    private static void handleIncomeMenu(Scanner scanner, IncomeService incomeService, String username) {
        System.out.println("\n--- Manage Income ---");
        System.out.println("1. View Income");
        System.out.println("2. Update Income");
        System.out.println("3. Delete Income");
        System.out.println("4. Search Income");
        System.out.println("5. Sort Income by Amount");
        int choice = InputValidator.readIntInRange(scanner, "Enter your choice: ", 1, 5);

        switch (choice) {
            case 1 -> incomeService.viewIncomes(username);
            case 2 -> incomeService.updateIncome(scanner, username);
            case 3 -> incomeService.deleteIncome(scanner, username);
            case 4 -> incomeService.searchIncome(scanner, username);
            case 5 -> incomeService.sortByAmount(username);
            default -> System.out.println("Invalid choice.");
        }
    }

    /**
     * Handles the expense management sub-menu.
     *
     * @param scanner        the {@link Scanner} used to read console input
     * @param expenseService the service handling expense operations
     * @param username       the username of the currently logged-in user
     */
    private static void handleExpenseMenu(Scanner scanner, ExpenseService expenseService, String username) {
        System.out.println("\n--- Manage Expense ---");
        System.out.println("1. View Expenses");
        System.out.println("2. Update Expense");
        System.out.println("3. Delete Expense");
        System.out.println("4. Search Expense");
        System.out.println("5. Sort by Amount");
        System.out.println("6. Sort by Date");
        System.out.println("7. Sort by Category");
        int choice = InputValidator.readIntInRange(scanner, "Enter your choice: ", 1, 7);

        switch (choice) {
            case 1 -> expenseService.viewExpenses(username);
            case 2 -> expenseService.updateExpense(scanner, username);
            case 3 -> expenseService.deleteExpense(scanner, username);
            case 4 -> expenseService.searchExpense(scanner, username);
            case 5 -> expenseService.sortByAmount(username);
            case 6 -> expenseService.sortByDate(username);
            case 7 -> expenseService.sortByCategory(username);
            default -> System.out.println("Invalid choice.");
        }
    }

    /**
     * Handles the budget management sub-menu.
     *
     * @param scanner        the {@link Scanner} used to read console input
     * @param budgetService  the service handling budget operations
     * @param expenseService the service used to calculate spending against the budget
     * @param username       the username of the currently logged-in user
     */
    private static void handleBudgetMenu(Scanner scanner, BudgetService budgetService, ExpenseService expenseService, String username) {
        System.out.println("\n--- Budget Menu ---");
        System.out.println("1. Set Monthly Budget");
        System.out.println("2. View Remaining Budget");
        int choice = InputValidator.readIntInRange(scanner, "Enter your choice: ", 1, 2);

        switch (choice) {
            case 1 -> budgetService.setMonthlyBudget(scanner, username);
            case 2 -> budgetService.showRemainingBudget(scanner, username, expenseService);
            default -> System.out.println("Invalid choice.");
        }
    }
}
