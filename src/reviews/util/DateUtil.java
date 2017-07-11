package reviews.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Supporting functions for working with dates.
 */
public class DateUtil {

    /** Date template(can be changed). */
    private static final String DATE_PATTERN = "dd.MM.yyyy";

    /** Date formatter. */
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern(DATE_PATTERN);

    /**
     * Using {@link DateUtil#DATE_PATTERN} to format date to string.
     *
     * @param date - date
     * @return formatted string
     */
    public static String format(LocalDate date) {
        if (date == null) {
            return null;
        }
        return DATE_FORMATTER.format(date);
    }

    /**
     * Converting string using {@link DateUtil#DATE_PATTERN} into {@link LocalDate}.
     *
     * Returns null if string cant be converted.
     *
     * @param dateString - Date as String
     * @return Resulting date
     */
    public static LocalDate parse(String dateString) {
        try {
            return DATE_FORMATTER.parse(dateString, LocalDate::from);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    /**
     * Checking string(is it valid date).
     *
     * @param dateString
     * @return true - string is correct
     */
    public static boolean validDate(String dateString) {
        return DateUtil.parse(dateString) != null;
    }
}