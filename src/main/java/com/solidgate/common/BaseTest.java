package com.solidgate.common;

import com.solidgate.utils.DriverSetup;
import org.testng.annotations.BeforeSuite;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class BaseTest {
    // Variables for storing configuration data
    protected static String API_BASE_URL;
    protected static String PAYMENT_PAGE_BASE_URL;
    protected static String SECRET_KEY;
    protected static String PUBLIC_KEY;

    // Variable for storing orderId, passed between UI and API tests
    protected static int currentOrderAmount;
    protected static String currentOrderCurrency;
    protected static String currentOrderId;

    private static final String PLACEHOLDER_SECRET_KEY = "YOUR_SECRET_KEY_HERE";
    private static final String PLACEHOLDER_PUBLIC_KEY = "YOUR_PUBLIC_KEY_HERE";


    @BeforeSuite(alwaysRun = true)
    public void setupSuite() {
        // Loading configuration data from test.properties
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream("src/test/resources/test.properties")) {
            properties.load(fis);
            API_BASE_URL = properties.getProperty("api.base.url");
            PAYMENT_PAGE_BASE_URL = properties.getProperty("payment.page.url");
            SECRET_KEY = properties.getProperty("secret.key");
            PUBLIC_KEY = properties.getProperty("public.key");


            // Handle sensitive keys: check for placeholders, then environment variables
            if (SECRET_KEY == null || SECRET_KEY.isEmpty() || SECRET_KEY.equals(PLACEHOLDER_SECRET_KEY)) {
                SECRET_KEY = System.getenv("SOLIDGATE_SECRET_KEY"); // Changed environment variable name
                if (SECRET_KEY == null || SECRET_KEY.isEmpty()) {
                    throw new IllegalStateException("Secret key is missing or is a placeholder in test.properties and not found in environment variables (SOLIDGATE_SECRET_KEY).");
                }
                System.out.println("Secret key loaded from environment variable SOLIDGATE_SECRET_KEY.");
            } else {
                System.out.println("Secret key loaded from test.properties.");
            }

            if (PUBLIC_KEY == null || PUBLIC_KEY.isEmpty() || PUBLIC_KEY.equals(PLACEHOLDER_PUBLIC_KEY)) {
                PUBLIC_KEY = System.getenv("SOLIDGATE_PUBLIC_KEY");
                if (PUBLIC_KEY == null || PUBLIC_KEY.isEmpty()) {
                    throw new IllegalStateException("Public key is missing or is a placeholder in test.properties and not found in environment variables (SOLIDGATE_PUBLIC_KEY).");
                }
                System.out.println("Public key loaded from environment variable SOLIDGATE_PUBLIC_KEY.");
            } else {
                System.out.println("Public key loaded from test.properties.");
            }

            if (API_BASE_URL == null || PAYMENT_PAGE_BASE_URL == null || SECRET_KEY == null || PUBLIC_KEY == null) {
                throw new IllegalStateException("One or more required parameters are missing in test.properties. Please check the file.");
            }

        } catch (IOException e) {
            System.err.println("Error loading test.properties file: " + e.getMessage());
            throw new RuntimeException("Failed to load configuration file.", e);
        }

        // WebDriver initialization via DriverSetup
        DriverSetup.setupWebDriver();
    }


}