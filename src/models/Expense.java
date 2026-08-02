package models;

import java.time.LocalDate;

/**
 * Represents a single expense entry recorded by a user.
 * <p>
 * An Expense is a specialized {@link Transaction} that additionally stores
 * the {@link ExpenseCategory} it belongs to. This class demonstrates
 * <b>Inheritance</b> by extending {@link Transaction} and
 * <b>Polymorphism</b> by overriding {@link #getType()} and
 * {@link #toString()}.
 * </p>
 *
 * @author Personal Finance Manager Team
 * @version 1.0
 */
public class Expense extends Transaction {

    private static final long serialVersionUID = 1L;

    /** Category this expense belongs to. */
    private ExpenseCategory category;

    /**
     * Constructs a new Expense entry.
     *
     * @param id          unique identifier for this expense
     * @param username    owner of this expense record
     * @param category    category the expense belongs to
     * @param amount      amount spent
     * @param date        date the expense occurred
     * @param description optional note about the expense
     */
    public Expense(int id, String username, ExpenseCategory category, double amount, LocalDate date, String description) {
        super(id, username, amount, date, description);
        this.category = category;
    }

    /**
     * Returns the category of this expense.
     *
     * @return the expense category
     */
    public ExpenseCategory getCategory() {
        return category;
    }

    /**
     * Updates the category of this expense.
     *
     * @param category the new expense category
     */
    public void setCategory(ExpenseCategory category) {
        this.category = category;
    }

    /**
     * Identifies this transaction as an expense.
     *
     * @return the literal string "EXPENSE"
     */
    @Override
    public String getType() {
        return "EXPENSE";
    }

    /**
     * Returns a formatted string representation of this expense entry.
     *
     * @return a human-readable description of the expense record
     */
    @Override
    public String toString() {
        return "Expense{" +
                "id=" + id +
                ", category=" + category +
                ", amount=" + amount +
                ", date=" + date +
                ", description='" + description + '\'' +
                '}';
    }
}
