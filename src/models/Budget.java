package models;

import java.io.Serializable;

/**
 * Represents a monthly budget limit set by a user for a specific
 * month and year.
 * <p>
 * The application compares total expenses in a given month against the
 * {@code monthlyLimit} stored here to warn the user or notify them that
 * their budget has been exceeded.
 * </p>
 *
 * @author Personal Finance Manager Team
 * @version 1.0
 */
public class Budget implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Unique identifier for this budget entry. */
    private int budgetId;

    /** Username of the owner of this budget. */
    private String username;

    /** Month this budget applies to (1 = January ... 12 = December). */
    private int month;

    /** Year this budget applies to. */
    private int year;

    /** Maximum amount the user intends to spend during this month. */
    private double monthlyLimit;

    /**
     * Constructs a new Budget entry.
     *
     * @param budgetId     unique identifier for this budget
     * @param username     owner of this budget
     * @param month        the month this budget applies to (1-12)
     * @param year         the year this budget applies to
     * @param monthlyLimit the maximum amount allowed to be spent this month
     */
    public Budget(int budgetId, String username, int month, int year, double monthlyLimit) {
        this.budgetId = budgetId;
        this.username = username;
        this.month = month;
        this.year = year;
        this.monthlyLimit = monthlyLimit;
    }

    /**
     * Returns the unique identifier of this budget entry.
     *
     * @return the budget ID
     */
    public int getBudgetId() {
        return budgetId;
    }

    /**
     * Updates the unique identifier of this budget entry.
     *
     * @param budgetId the new budget ID
     */
    public void setBudgetId(int budgetId) {
        this.budgetId = budgetId;
    }

    /**
     * Returns the username of the owner of this budget.
     *
     * @return the owner's username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Updates the username of the owner of this budget.
     *
     * @param username the new owner username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the month this budget applies to.
     *
     * @return the month (1-12)
     */
    public int getMonth() {
        return month;
    }

    /**
     * Updates the month this budget applies to.
     *
     * @param month the new month (1-12)
     */
    public void setMonth(int month) {
        this.month = month;
    }

    /**
     * Returns the year this budget applies to.
     *
     * @return the year
     */
    public int getYear() {
        return year;
    }

    /**
     * Updates the year this budget applies to.
     *
     * @param year the new year
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * Returns the monthly spending limit for this budget.
     *
     * @return the monthly limit
     */
    public double getMonthlyLimit() {
        return monthlyLimit;
    }

    /**
     * Updates the monthly spending limit for this budget.
     *
     * @param monthlyLimit the new monthly limit
     */
    public void setMonthlyLimit(double monthlyLimit) {
        this.monthlyLimit = monthlyLimit;
    }

    /**
     * Returns a formatted string representation of this budget entry.
     *
     * @return a human-readable description of the budget record
     */
    @Override
    public String toString() {
        return "Budget{" +
                "month=" + month +
                ", year=" + year +
                ", monthlyLimit=" + monthlyLimit +
                '}';
    }
}
