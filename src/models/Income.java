package models;

import java.time.LocalDate;


public class Income extends Transaction {

    private static final long serialVersionUID = 1L;

  
    private String source;

  
    public Income(int id, String username, String source, double amount, LocalDate date, String description) {
        super(id, username, amount, date, description);
        this.source = source;
    }

    
    public String getSource() {
        return source;
    }

   
    public void setSource(String source) {
        this.source = source;
    }

   
    @Override
    public String getType() {
        return "INCOME";
    }

  
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
