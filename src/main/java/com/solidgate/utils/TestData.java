package com.solidgate.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Utility class for generating test data.
 * Contains information about test cards and methods for generating random orderIds.
 */
public class TestData {

    public static final String TEST_CARD_NUMBER = "4067429974719265";
    public static final String TEST_EXPIRY_DATE = getFutureMonthYear();
    public static final String TEST_CVV = "123";
    public static final String TEST_EMAIL = "example@example.com";

    /**
     * Generates a unique order identifier (orderId) based on UUID.
     */
    public static String generateRandomOrderId() {
        return "order_" + UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * Generates a date in the future (e.g., next year) to be valid.
     * Format MM/YY.
     */
    private static String getFutureMonthYear() {
        LocalDate now = LocalDate.now();
        // Set the date 12 months in the future to avoid expired card issues
        LocalDate futureDate = now.plusYears(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
        return futureDate.format(formatter);
    }

    /**
     * Generates the current date and time in "yyyy-MM-dd HH:mm:ss" format.
     */
    public static String getCurrentDateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }
}
