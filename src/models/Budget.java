package models;

import java.io.Serializable;


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

   
    public Budget(int budgetId, String username, int month, int year, double monthlyLimit) {
        this.budgetId = budgetId;
        this.username = username;
        this.month = month;
        this.year = year;
        this.monthlyLimit = monthlyLimit;
    }

 
    public int getBudgetId() {
        return budgetId;
    }

    public void setBudgetId(int budgetId) {
        this.budgetId = budgetId;
    }


    public String getUsername() {
        return username;
    }

   
    public void setUsername(String username) {
        this.username = username;
    }

    
    public int getMonth() {
        return month;
    }

 
    public void setMonth(int month) {
        this.month = month;
    }

  
    public int getYear() {
        return year;
    }


    public void setYear(int year) {
        this.year = year;
    }

    public double getMonthlyLimit() {
        return monthlyLimit;
    }


    public void setMonthlyLimit(double monthlyLimit) {
        this.monthlyLimit = monthlyLimit;
    }

  
    @Override
    public String toString() {
        return "Budget{" +
                "month=" + month +
                ", year=" + year +
                ", monthlyLimit=" + monthlyLimit +
                '}';
    }
}
