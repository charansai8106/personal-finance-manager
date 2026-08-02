package models;

import java.time.LocalDate;

/**
 * Represents a single income entry recorded by a user.
 * <p>
 * An Income is a specialized {@link Transaction} that additionally stores
 * the source of the money received (e.g. "Salary", "Freelance", "Business").
 * This class demonstrates <b>Inheritance</b> by extending {@link Transaction}
 * and <b>Polymorphism</b> by overriding {@link #getType()} and
 * {@link #toString()}.
 * </p>
 *
 * @author Personal Finance Manager Team
 * @version 1.0
 */
public class Income extends Transaction {

    private static final long serialVersionUID = 1L;

    /** Source of the income, e.g. "Salary", "Freelance", "Business". */
    private String source;

    /**
     * Constructs a new Income entry.
     *
     * @param id          unique identifier for this income
     * @param username    owner of this income record
     * @param source      source of the income
     * @param amount      amount received
     * @param date        date the income was received
     * @param description optional note about the income
     */
    public Income(int id, String username, String source, double amount, LocalDate date, String description) {
        super(id, username, amount, date, description);
        this.source = source;
    }

    /**
     * Returns the source of this income.
     *
     * @return the income source
     */
    public String getSource() {
        return source;
    }

    /**
     * Updates the source of this income.
     *
     * @param source the new income source
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * Identifies this transaction as an income.
     *
     * @return the literal string "INCOME"
     */
    @Override
    public String getType() {
        return "INCOME";
    }

    /**
     * Returns a formatted string representation of this income entry.
     *
     * @return a human-readable description of the income record
     */
    @Override
    public String toString() {
        return "Income{" +
                "id=" + id +
                ", source='" + source + '\'' +
                ", amount=" + amount +
                ", date=" + date +
                ", description='" + description + '\'' +
                '}';
    }
}
