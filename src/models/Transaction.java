package models;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Abstract base class representing a generic financial transaction.
 * <p>
 * Both {@link Income} and {@link Expense} share common attributes such as
 * an identifier, owner username, amount, date and description. Rather than
 * duplicating these fields, this class centralizes them and demonstrates
 * <b>Abstraction</b> and <b>Inheritance</b> in the project's design.
 * </p>
 * <p>
 * Subclasses must implement {@link #getType()} to identify themselves,
 * which demonstrates <b>Polymorphism</b> when transactions of different
 * types are processed through a common reference.
 * </p>
 *
 * @author Personal Finance Manager Team
 * @version 1.0
 */
public abstract class Transaction implements Serializable, Comparable<Transaction> {

    private static final long serialVersionUID = 1L;

    /** Unique identifier for this transaction. */
    protected int id;

    /** Username of the account that owns this transaction. */
    protected String username;

    /** Monetary amount involved in this transaction. */
    protected double amount;

    /** Date the transaction occurred. */
    protected LocalDate date;

    /** Optional description/note about the transaction. */
    protected String description;

    /**
     * Constructs a new Transaction with the given details.
     *
     * @param id          unique identifier
     * @param username    owner of the transaction
     * @param amount      monetary amount
     * @param date        date of the transaction
     * @param description optional description
     */
    protected Transaction(int id, String username, double amount, LocalDate date, String description) {
        this.id = id;
        this.username = username;
        this.amount = amount;
        this.date = date;
        this.description = description;
    }

    /**
     * Returns the unique identifier of this transaction.
     *
     * @return the transaction ID
     */
    public int getId() {
        return id;
    }

    /**
     * Updates the unique identifier of this transaction.
     *
     * @param id the new transaction ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the username of the owner of this transaction.
     *
     * @return the owner's username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Updates the username of the owner of this transaction.
     *
     * @param username the new owner username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the amount of this transaction.
     *
     * @return the transaction amount
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Updates the amount of this transaction.
     *
     * @param amount the new transaction amount
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * Returns the date of this transaction.
     *
     * @return the transaction date
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Updates the date of this transaction.
     *
     * @param date the new transaction date
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Returns the description of this transaction.
     *
     * @return the transaction description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Updates the description of this transaction.
     *
     * @param description the new description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns a short label identifying the type of transaction
     * (e.g. "INCOME" or "EXPENSE"). Each subclass must provide its own
     * implementation, demonstrating polymorphism.
     *
     * @return the type label of this transaction
     */
    public abstract String getType();

    /**
     * Compares this transaction to another based on date, allowing lists of
     * transactions to be sorted chronologically using {@code Collections.sort()}.
     *
     * @param other the other transaction to compare to
     * @return a negative, zero, or positive integer as this date is before,
     *         equal to, or after the other date
     */
    @Override
    public int compareTo(Transaction other) {
        return this.date.compareTo(other.date);
    }

    /**
     * Returns a basic string representation shared by all transaction types.
     * Subclasses typically extend this with their own specific fields.
     *
     * @return a human-readable description of the transaction
     */
    @Override
    public String toString() {
        return "[" + getType() + "] ID=" + id + ", amount=" + amount +
                ", date=" + date + ", description='" + description + '\'';
    }
}
