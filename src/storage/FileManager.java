package storage;

import models.Budget;
import models.Expense;
import models.ExpenseCategory;
import models.Income;
import models.User;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles all file input/output operations for the Personal Finance
 * Manager application.
 * <p>
 * All application data (users, incomes, expenses, budgets) is stored as
 * plain, human-readable CSV text files inside the {@code /data} folder,
 * which is created automatically if it does not already exist. This class
 * uses {@link BufferedReader} and {@link BufferedWriter} for efficient
 * file reading and writing, and ensures the rest of the application never
 * crashes due to missing files or folders.
 * </p>
 *
 * @author Personal Finance Manager Team
 * @version 1.0
 */
public class FileManager {

    /** Folder where all application data files are stored. */
    private static final String DATA_FOLDER = "data";

    private static final String USERS_FILE = DATA_FOLDER + File.separator + "users.csv";
    private static final String INCOMES_FILE = DATA_FOLDER + File.separator + "incomes.csv";
    private static final String EXPENSES_FILE = DATA_FOLDER + File.separator + "expenses.csv";
    private static final String BUDGETS_FILE = DATA_FOLDER + File.separator + "budgets.csv";

    /** Delimiter used to separate CSV fields (commas inside text fields are replaced with this instead). */
    private static final String DELIMITER = ",";

    /**
     * Constructs a new FileManager and ensures that the data folder and
     * all required data files exist on disk before the application starts.
     */
    public FileManager() {
        ensureDataFolderExists();
    }

    /**
     * Creates the {@code /data} folder and empty data files if they do not
     * already exist. This method is called automatically whenever a
     * {@code FileManager} is constructed so the application never fails
     * due to missing files.
     */
    private void ensureDataFolderExists() {
        File folder = new File(DATA_FOLDER);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        createFileIfMissing(USERS_FILE);
        createFileIfMissing(INCOMES_FILE);
        createFileIfMissing(EXPENSES_FILE);
        createFileIfMissing(BUDGETS_FILE);
    }

    /**
     * Creates an empty file at the given path if it does not already exist.
     *
     * @param path the path of the file to check/create
     */
    private void createFileIfMissing(String path) {
        File file = new File(path);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            System.out.println("Warning: could not create file " + path + " (" + e.getMessage() + ")");
        }
    }

    /**
     * Replaces commas in free-text fields with semicolons so the CSV
     * structure is never broken by user-entered text.
     *
     * @param text the raw text to sanitize
     * @return the sanitized text safe for CSV storage
     */
    private String sanitize(String text) {
        if (text == null) {
            return "";
        }
        return text.replace(",", ";");
    }

    // ==========================================================
    // USER PERSISTENCE
    // ==========================================================

    /**
     * Loads all registered users from {@code users.csv}.
     *
     * @return a list of all stored {@link User} objects; empty if none exist
     */
    public List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] parts = line.split(DELIMITER, -1);
                if (parts.length < 4) {
                    continue;
                }
                User user = new User(parts[0], parts[1], parts[2], parts[3]);
                users.add(user);
            }
        } catch (IOException e) {
            System.out.println("Warning: could not load users (" + e.getMessage() + ")");
        }
        return users;
    }

    /**
     * Saves the given list of users to {@code users.csv}, overwriting any
     * previous content.
     *
     * @param users the list of users to save
     */
    public void saveUsers(List<User> users) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE))) {
            for (User user : users) {
                writer.write(String.join(DELIMITER,
                        user.getUsername(),
                        user.getPassword(),
                        sanitize(user.getFullName()),
                        sanitize(user.getEmail())));
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Warning: could not save users (" + e.getMessage() + ")");
        }
    }

    // ==========================================================
    // INCOME PERSISTENCE
    // ==========================================================

    /**
     * Loads all income records from {@code incomes.csv}.
     *
     * @return a list of all stored {@link Income} objects; empty if none exist
     */
    public List<Income> loadIncomes() {
        List<Income> incomes = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(INCOMES_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] parts = line.split(DELIMITER, -1);
                if (parts.length < 6) {
                    continue;
                }
                try {
                    int id = Integer.parseInt(parts[0]);
                    String username = parts[1];
                    String source = parts[2];
                    double amount = Double.parseDouble(parts[3]);
                    LocalDate date = LocalDate.parse(parts[4]);
                    String description = parts[5];
                    incomes.add(new Income(id, username, source, amount, date, description));
                } catch (Exception e) {
                    System.out.println("Warning: skipping corrupted income record.");
                }
            }
        } catch (IOException e) {
            System.out.println("Warning: could not load incomes (" + e.getMessage() + ")");
        }
        return incomes;
    }

    /**
     * Saves the given list of incomes to {@code incomes.csv}, overwriting
     * any previous content.
     *
     * @param incomes the list of incomes to save
     */
    public void saveIncomes(List<Income> incomes) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(INCOMES_FILE))) {
            for (Income income : incomes) {
                writer.write(String.join(DELIMITER,
                        String.valueOf(income.getId()),
                        income.getUsername(),
                        sanitize(income.getSource()),
                        String.valueOf(income.getAmount()),
                        income.getDate().toString(),
                        sanitize(income.getDescription())));
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Warning: could not save incomes (" + e.getMessage() + ")");
        }
    }

    // ==========================================================
    // EXPENSE PERSISTENCE
    // ==========================================================

    /**
     * Loads all expense records from {@code expenses.csv}.
     *
     * @return a list of all stored {@link Expense} objects; empty if none exist
     */
    public List<Expense> loadExpenses() {
        List<Expense> expenses = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(EXPENSES_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] parts = line.split(DELIMITER, -1);
                if (parts.length < 6) {
                    continue;
                }
                try {
                    int id = Integer.parseInt(parts[0]);
                    String username = parts[1];
                    ExpenseCategory category = ExpenseCategory.fromString(parts[2]);
                    double amount = Double.parseDouble(parts[3]);
                    LocalDate date = LocalDate.parse(parts[4]);
                    String description = parts[5];
                    expenses.add(new Expense(id, username, category, amount, date, description));
                } catch (Exception e) {
                    System.out.println("Warning: skipping corrupted expense record.");
                }
            }
        } catch (IOException e) {
            System.out.println("Warning: could not load expenses (" + e.getMessage() + ")");
        }
        return expenses;
    }

    /**
     * Saves the given list of expenses to {@code expenses.csv}, overwriting
     * any previous content.
     *
     * @param expenses the list of expenses to save
     */
    public void saveExpenses(List<Expense> expenses) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(EXPENSES_FILE))) {
            for (Expense expense : expenses) {
                writer.write(String.join(DELIMITER,
                        String.valueOf(expense.getId()),
                        expense.getUsername(),
                        expense.getCategory().name(),
                        String.valueOf(expense.getAmount()),
                        expense.getDate().toString(),
                        sanitize(expense.getDescription())));
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Warning: could not save expenses (" + e.getMessage() + ")");
        }
    }

    // ==========================================================
    // BUDGET PERSISTENCE
    // ==========================================================

    /**
     * Loads all budget records from {@code budgets.csv}.
     *
     * @return a list of all stored {@link Budget} objects; empty if none exist
     */
    public List<Budget> loadBudgets() {
        List<Budget> budgets = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(BUDGETS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] parts = line.split(DELIMITER, -1);
                if (parts.length < 5) {
                    continue;
                }
                try {
                    int budgetId = Integer.parseInt(parts[0]);
                    String username = parts[1];
                    int month = Integer.parseInt(parts[2]);
                    int year = Integer.parseInt(parts[3]);
                    double limit = Double.parseDouble(parts[4]);
                    budgets.add(new Budget(budgetId, username, month, year, limit));
                } catch (Exception e) {
                    System.out.println("Warning: skipping corrupted budget record.");
                }
            }
        } catch (IOException e) {
            System.out.println("Warning: could not load budgets (" + e.getMessage() + ")");
        }
        return budgets;
    }

    /**
     * Saves the given list of budgets to {@code budgets.csv}, overwriting
     * any previous content.
     *
     * @param budgets the list of budgets to save
     */
    public void saveBudgets(List<Budget> budgets) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BUDGETS_FILE))) {
            for (Budget budget : budgets) {
                writer.write(String.join(DELIMITER,
                        String.valueOf(budget.getBudgetId()),
                        budget.getUsername(),
                        String.valueOf(budget.getMonth()),
                        String.valueOf(budget.getYear()),
                        String.valueOf(budget.getMonthlyLimit())));
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Warning: could not save budgets (" + e.getMessage() + ")");
        }
    }

    // ==========================================================
    // REPORT EXPORT
    // ==========================================================

    /**
     * Writes free-form report text to a file inside the {@code /data}
     * folder so the user can keep a copy of a generated report.
     *
     * @param fileName the name of the file to create (e.g. "report_2025.txt")
     * @param content  the text content to write
     * @return {@code true} if the export succeeded, {@code false} otherwise
     */
    public boolean exportReport(String fileName, String content) {
        String path = DATA_FOLDER + File.separator + fileName;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            writer.write(content);
            return true;
        } catch (IOException e) {
            System.out.println("Warning: could not export report (" + e.getMessage() + ")");
            return false;
        }
    }
}
