package services;

import models.Expense;
import models.ExpenseCategory;
import models.Income;
import storage.FileManager;
import utils.CurrencyFormatter;
import utils.DateUtil;
import utils.InputValidator;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Generates financial reports for a user by combining data from
 * {@link IncomeService} and {@link ExpenseService}.
 * <p>
 * Reports include totals, savings, category-wise spending breakdowns,
 * monthly summaries, and the highest/lowest recorded expenses.
 * </p>
 *
 * @author Personal Finance Manager Team
 * @version 1.0
 */
public class ReportService {

    /** Service providing access to income records. */
    private final IncomeService incomeService;

    /** Service providing access to expense records. */
    private final ExpenseService expenseService;

    /** Handles exporting reports to disk. */
    private final FileManager fileManager;

    /**
     * Constructs a new ReportService.
     *
     * @param incomeService  the income service to pull income data from
     * @param expenseService the expense service to pull expense data from
     * @param fileManager    the file manager used to export reports
     */
    public ReportService(IncomeService incomeService, ExpenseService expenseService, FileManager fileManager) {
        this.incomeService = incomeService;
        this.expenseService = expenseService;
        this.fileManager = fileManager;
    }

    /**
     * Builds a full financial summary report as a formatted string.
     *
     * @param username the username to generate the report for
     * @return the complete report text
     */
    public String buildSummaryReport(String username) {
        StringBuilder sb = new StringBuilder();
        double totalIncome = incomeService.getTotalIncome(username);
        double totalExpense = expenseService.getTotalExpense(username);
        double savings = totalIncome - totalExpense;

        sb.append("=========================================\n");
        sb.append("      PERSONAL FINANCE SUMMARY REPORT     \n");
        sb.append("=========================================\n");
        sb.append("User: ").append(username).append("\n");
        sb.append("-----------------------------------------\n");
        sb.append("Total Income   : ").append(CurrencyFormatter.format(totalIncome)).append("\n");
        sb.append("Total Expense  : ").append(CurrencyFormatter.format(totalExpense)).append("\n");
        sb.append("Net Savings    : ").append(CurrencyFormatter.format(savings)).append("\n");
        sb.append("-----------------------------------------\n");
        sb.append("Category-wise Spending:\n");

        Map<ExpenseCategory, Double> categoryTotals = getCategoryWiseSpending(username);
        for (Map.Entry<ExpenseCategory, Double> entry : categoryTotals.entrySet()) {
            sb.append(String.format("  %-15s : %s%n", entry.getKey(), CurrencyFormatter.format(entry.getValue())));
        }

        Expense highest = getHighestExpense(username);
        Expense lowest = getLowestExpense(username);
        sb.append("-----------------------------------------\n");
        sb.append("Highest Expense: ").append(highest != null
                ? CurrencyFormatter.format(highest.getAmount()) + " (" + highest.getCategory() + ")" : "N/A").append("\n");
        sb.append("Lowest Expense : ").append(lowest != null
                ? CurrencyFormatter.format(lowest.getAmount()) + " (" + lowest.getCategory() + ")" : "N/A").append("\n");
        sb.append("=========================================\n");

        return sb.toString();
    }

    /**
     * Prints the full summary report for a user to the console.
     *
     * @param username the username to generate the report for
     */
    public void printSummaryReport(String username) {
        System.out.println("\n" + buildSummaryReport(username));
    }

    /**
     * Calculates total spending per expense category for the given user,
     * using a {@link LinkedHashMap} to preserve category declaration order.
     *
     * @param username the username to calculate spending for
     * @return a map of category to total amount spent in that category
     */
    public Map<ExpenseCategory, Double> getCategoryWiseSpending(String username) {
        Map<ExpenseCategory, Double> totals = new LinkedHashMap<>();
        for (ExpenseCategory category : ExpenseCategory.values()) {
            totals.put(category, 0.0);
        }
        for (Expense expense : expenseService.getExpensesByUser(username)) {
            double current = totals.getOrDefault(expense.getCategory(), 0.0);
            totals.put(expense.getCategory(), current + expense.getAmount());
        }
        return totals;
    }

    /**
     * Prompts for a month and year, then prints an income/expense/savings
     * summary limited to that month.
     *
     * @param scanner  the {@link Scanner} used to read console input
     * @param username the username to generate the report for
     */
    public void printMonthlyReport(Scanner scanner, String username) {
        System.out.println("\n--- Monthly Report ---");
        int month = InputValidator.readIntInRange(scanner, "Enter month (1-12): ", 1, 12);
        int year = InputValidator.readIntInRange(scanner, "Enter year (e.g. 2026): ", 2000, 2100);

        double monthlyIncome = 0;
        for (Income income : incomeService.getIncomesByUser(username)) {
            if (income.getDate().getMonthValue() == month && income.getDate().getYear() == year) {
                monthlyIncome += income.getAmount();
            }
        }

        double monthlyExpense = expenseService.getMonthlyExpenseTotal(username, month, year);

        System.out.println("Report for " + DateUtil.getMonthName(month) + " " + year + ":");
        System.out.println("Income  : " + CurrencyFormatter.format(monthlyIncome));
        System.out.println("Expense : " + CurrencyFormatter.format(monthlyExpense));
        System.out.println("Savings : " + CurrencyFormatter.format(monthlyIncome - monthlyExpense));
    }

    /**
     * Finds the single highest expense recorded by the given user.
     *
     * @param username the username to search
     * @return the highest {@link Expense}, or {@code null} if the user has no expenses
     */
    public Expense getHighestExpense(String username) {
        List<Expense> userExpenses = expenseService.getExpensesByUser(username);
        Expense highest = null;
        for (Expense expense : userExpenses) {
            if (highest == null || expense.getAmount() > highest.getAmount()) {
                highest = expense;
            }
        }
        return highest;
    }

    /**
     * Finds the single lowest expense recorded by the given user.
     *
     * @param username the username to search
     * @return the lowest {@link Expense}, or {@code null} if the user has no expenses
     */
    public Expense getLowestExpense(String username) {
        List<Expense> userExpenses = expenseService.getExpensesByUser(username);
        Expense lowest = null;
        for (Expense expense : userExpenses) {
            if (lowest == null || expense.getAmount() < lowest.getAmount()) {
                lowest = expense;
            }
        }
        return lowest;
    }

    /**
     * Exports the full summary report for the given user to a text file
     * inside the {@code /data} folder.
     *
     * @param username the username to generate the report for
     */
    public void exportReport(String username) {
        String fileName = "report_" + username + ".txt";
        boolean success = fileManager.exportReport(fileName, buildSummaryReport(username));
        if (success) {
            System.out.println("Report exported successfully to data/" + fileName);
        }
    }
}
