package models;


public enum ExpenseCategory {
    FOOD,
    TRAVEL,
    SHOPPING,
    RENT,
    MEDICAL,
    ENTERTAINMENT,
    EDUCATION,
    OTHERS;

    
    public static ExpenseCategory fromString(String text) {
        if (text == null) {
            return OTHERS;
        }
        for (ExpenseCategory category : values()) {
            if (category.name().equalsIgnoreCase(text.trim())) {
                return category;
            }
        }
        return OTHERS;
    }
}
