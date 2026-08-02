package utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Utility class providing helper methods for parsing and formatting
 * {@link LocalDate} and {@link LocalDateTime} values used throughout the
 * Personal Finance Manager application.
 * <p>
 * All methods are static since this class only provides stateless helper
 * behaviour and is never meant to be instantiated.
 * </p>
 *
 * @author Personal Finance Manager Team
 * @version 1.0
 */
public final class DateUtil {

    /** Standard date pattern used across the application: dd-MM-yyyy. */
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    /** Standard date-time pattern used for timestamps: dd-MM-yyyy HH:mm:ss. */
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private DateUtil() {
    }

    /**
     * Parses a date string in dd-MM-yyyy format into a {@link LocalDate}.
     *
     * @param text the date text to parse
     * @return the parsed {@code LocalDate}
     * @throws DateTimeParseException if the text is not a valid date in the expected format
     */
    public static LocalDate parseDate(String text) {
        return LocalDate.parse(text.trim(), DATE_FORMATTER);
    }

    /**
     * Checks whether the given text represents a valid date in dd-MM-yyyy format.
     *
     * @param text the date text to validate
     * @return {@code true} if the text is a valid date, {@code false} otherwise
     */
    public static boolean isValidDate(String text) {
        try {
            parseDate(text);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Formats a {@link LocalDate} into a dd-MM-yyyy string.
     *
     * @param date the date to format
     * @return the formatted date string
     */
    public static String formatDate(LocalDate date) {
        return date.format(DATE_FORMATTER);
    }

    /**
     * Formats a {@link LocalDateTime} into a dd-MM-yyyy HH:mm:ss string.
     *
     * @param dateTime the date-time to format
     * @return the formatted date-time string
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(DATE_TIME_FORMATTER);
    }

    /**
     * Returns the current month (1-12).
     *
     * @return the current month
     */
    public static int getCurrentMonth() {
        return LocalDate.now().getMonthValue();
    }

    /**
     * Returns the current year.
     *
     * @return the current year
     */
    public static int getCurrentYear() {
        return LocalDate.now().getYear();
    }

    /**
     * Returns the full month name (e.g. "January") for a given month number.
     *
     * @param month the month number (1-12)
     * @return the full month name, or "Unknown" if out of range
     */
    public static String getMonthName(int month) {
        String[] months = {
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        };
        if (month < 1 || month > 12) {
            return "Unknown";
        }
        return months[month - 1];
    }
}
