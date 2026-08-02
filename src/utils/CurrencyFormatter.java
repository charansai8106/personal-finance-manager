package utils;

/**
 * Utility class responsible for formatting monetary amounts consistently
 * across the Personal Finance Manager application.
 * <p>
 * All amounts are displayed using the Indian Rupee symbol (Rs.) and two
 * decimal places, so reports and menus look consistent and professional.
 * </p>
 *
 * @author Personal Finance Manager Team
 * @version 1.0
 */
public final class CurrencyFormatter {

    /** Currency symbol/prefix used throughout the application. */
    private static final String CURRENCY_SYMBOL = "Rs. ";

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private CurrencyFormatter() {
    }

    /**
     * Formats a monetary amount with the currency symbol and two decimal places.
     *
     * @param amount the amount to format
     * @return a formatted currency string, e.g. "Rs. 1250.50"
     */
    public static String format(double amount) {
        return CURRENCY_SYMBOL + String.format("%,.2f", amount);
    }
}
