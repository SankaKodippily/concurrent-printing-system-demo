/*
Author: Senthuran Ambalavanar | 2015215 | w1608452
 */

package utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Contains helper methods for the printing system
 */
public class Utility {
    // To avoid instantiation, since this class only contains static utility methods
    private Utility() {

    }

    /**
     * Generates a random duration, which is used in sleeping the thread
     *
     * @return Duration
     */
    public static long generateRandomDuration() {
        return new Random().nextInt(1000) + 1000;
    }

    /**
     * Prints a given message with logging time
     *
     * @param loggerProcess        Process that is responsible for the message
     * @param message              The message to be logged
     * @param messageSuccessStatus Whether the message is a success message (true), failure message (false),
     *                             or a normal one (null)
     */
    public static synchronized void log(LoggerProcess loggerProcess, String message, Boolean messageSuccessStatus) {
        Date date = new Date(System.currentTimeMillis());
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss.SSS");
        String logTime = formatter.format(date);

        System.out.println("[" + logTime + "]  " + generateLog(loggerProcess, message, messageSuccessStatus));
    }

    /**
     * Generates a log message to be printed, with ANSI escaped colour sequence,
     * depending on the logging process and the success status of the message.
     * [LOGGING PROCESS] - logged in its particular colour
     * message           - logged in green, red, or in the colour of the process, namely when the success status is
     * true, false and null
     *
     * @param loggerProcess        The process which is responsible for the message
     * @param message              The message to be logged
     * @param messageSuccessStatus Whether the message is a success message (true), failure message (false),
     *                             or a normal one (null)
     * @return Generated log message
     */
    private static String generateLog(LoggerProcess loggerProcess, String message, Boolean messageSuccessStatus) {
        if (null == messageSuccessStatus) {
            String template = "%s[%s] %s\033[0m";
            return String.format(
                    template, getColorCode(loggerProcess), getLoggerProcessAsString(loggerProcess), message);
        }
        String template = "%s[%s]\033[0m %s%s\033[0m";
        return String.format(
                template,
                getColorCode(loggerProcess),
                getLoggerProcessAsString(loggerProcess),
                getColorCode(messageSuccessStatus),
                message);
    }

    /**
     * Generates the string representation of the given logger process enum
     *
     * @param loggerProcess Logger process
     * @return String representation of the logger process
     */
    private static String getLoggerProcessAsString(LoggerProcess loggerProcess) {
        switch (loggerProcess) {
            case PRINTER:
                return "PRINTER";
            case STUDENT:
                return "STUDENT";
            case PAPER_TECHNICIAN:
                return "PAPER TECHNICIAN";
            case TONER_TECHNICIAN:
                return "TONER TECHNICIAN";
            case PRINTING_SYSTEM:
                return "PRINTING SYSTEM";
            default:
                return "UNKNOWN";
        }
    }

    /**
     * Gets the ANSI escaped colour sequence for the given logger process
     *
     * @param loggerProcess Logger process
     * @return ANSI escaped colour sequence
     */
    private static String getColorCode(LoggerProcess loggerProcess) {
        switch (loggerProcess) {
            case PRINTER:
                return "\033[37;0;0m"; // White
            case STUDENT:
                return "\033[35;0;0m"; // Purple
            case PAPER_TECHNICIAN:
            case TONER_TECHNICIAN:
                return "\033[34;0;0m"; // Blue
            default:
                return "\033[37;0;0m"; // White
        }
    }

    /**
     * Gets the ANSI escaped colour sequence based on the given message status
     *
     * @param messageSuccessStatus Success status of the message
     * @return ANSI escaped colour sequence
     */
    private static String getColorCode(boolean messageSuccessStatus) {
        if (messageSuccessStatus) {
            return "\033[36;0;0m"; // Green
        }
        return "\033[31;0;0m"; // Red
    }
}
