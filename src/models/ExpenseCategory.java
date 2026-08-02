package models;

/**
 * Enumeration of all valid expense categories supported by the
 * Personal Finance Manager application.
 * <p>
 * Using an enum (instead of plain Strings) prevents invalid category values
 * from being stored and makes category-based operations type-safe.
 * </p>
 *
 * @author Personal Finance Manager Team
 * @version 1.0
 */
public enum ExpenseCategory {
    FOOD,
    TRAVEL,
    SHOPPING,
    RENT,
    MEDICAL,
    ENTERTAINMENT,
    EDUCATION,
    OTHERS;

    /**
     * Safely converts a raw string (case-insensitive) into an
     * {@code ExpenseCategory}. Falls back to {@code OTHERS} if the given
     * text does not match any known category, ensuring the application
     * never crashes on invalid category input.
     *
     * @param text the raw text entered by the user
     * @return the matching {@code ExpenseCategory}, or {@code OTHERS} if no match is found
     */
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
