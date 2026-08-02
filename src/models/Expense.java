package models;

import java.time.LocalDate;


public class Expense extends Transaction {

    private static final long serialVersionUID = 1L;

  
    private ExpenseCategory category;

   
    public Expense(int id, String username, ExpenseCategory category, double amount, LocalDate date, String description) {
        super(id, username, amount, date, description);
        this.category = category;
    }

   
    public ExpenseCategory getCategory() {
        return category;
    }

  
    public void setCategory(ExpenseCategory category) {
        this.category = category;
    }

    
    @Override
    public String getType() {
        return "EXPENSE";
    }


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
