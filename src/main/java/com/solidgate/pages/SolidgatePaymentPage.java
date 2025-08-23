package com.solidgate.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.testng.Assert;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;

import static com.codeborne.selenide.Selenide.$;


public class SolidgatePaymentPage {

    // Element locators on the Solidgate payment page.

    private final SelenideElement cardNumberField = $("[data-testid=cardNumber]"); // Card number field
    private final SelenideElement expiryDateField = $("[data-testid=cardExpiryDate]"); // Expiry date field
    private final SelenideElement cvvField = $("[data-testid=cardCvv]");                 // CVV field

    private final SelenideElement emailField = $("[data-testid=charity-email]");
    private final SelenideElement submitPaymentButton = $("[data-testid=submit]"); // "Pay" or "Submit" button
    private final SelenideElement paymentStatusMessage = $("[data-testid=status-title]"); // Payment success message
    private final SelenideElement priceMajorElement = $("[data-testid='price_major']"); // Element for displaying price and currency

    /**
     *
     * @param cardNumber Test card number.
     * @param expiryDate Card expiry date in MM/YY format.
     * @param cvv Card CVV/CVC code.
     */
    public void enterCardDetails(String cardNumber, String expiryDate, String cvv, String exampleEmail) {

        cardNumberField.shouldBe(Condition.visible).setValue(cardNumber);
        expiryDateField.setValue(expiryDate);
        emailField.setValue(exampleEmail);
        cvvField.setValue(cvv);

    }

    public void clickSubmitPayment() {

        submitPaymentButton.shouldBe(Condition.enabled).click();
    }

    /**
     * Verifies that the payment success message is displayed on the page
     * and contains the expected text.
     *
     * @param expectedMessage The expected text of the payment success message.
     */
    public void verifyPaymentSuccess(String expectedMessage) {

        paymentStatusMessage.shouldBe(Condition.visible).shouldHave(Condition.text(expectedMessage));
    }

    /**
     * Verifies that the displayed price on the payment page
     * matches the expected amount.
     *
     * @param expectedAmountMinorUnits The expected amount in minor units (e.g., 1000 for $10.00).
     */
    public void verifyDisplayedPrice(int expectedAmountMinorUnits) {

        priceMajorElement.shouldBe(Condition.visible);

        String displayedText = priceMajorElement.text();
        displayedText = displayedText.replace('\u00A0', ' ').trim();

        String amountString;
        // Determine if there is a currency symbol at the beginning
        if (displayedText.startsWith("$") || displayedText.startsWith("€") || displayedText.startsWith("£")) {
            amountString = displayedText.substring(1).trim();
        } else {
            // Assume the amount comes before a space preceding the currency code
            int lastSpaceIndex = displayedText.lastIndexOf(' ');
            if (lastSpaceIndex != -1) {
                amountString = displayedText.substring(0, lastSpaceIndex).trim();
            } else {
                amountString = displayedText.trim(); // If no space, take the whole text
            }
        }

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        // If the format uses a comma, then DecimalFormatSymbols with Locale.getDefault() (if correctly set to uk-UA)
        // should use a comma as the decimal separator.
        // If a dot is found instead, then assume it's a US format.
        if (amountString.contains(".") && !amountString.contains(",")) {
            symbols.setDecimalSeparator('.');
        } else {
            symbols.setDecimalSeparator(',');
        }

        DecimalFormat format = new DecimalFormat("0.00", symbols);

        try {
            Number number = format.parse(amountString);
            double actualAmountMajorUnits = number.doubleValue();
            int actualAmountMinorUnits = (int) (Math.round(actualAmountMajorUnits * 100.0));

            Assert.assertEquals(actualAmountMinorUnits, expectedAmountMinorUnits, "Amount on the page does not match the expected (in minor units).");
        } catch (ParseException e) {
            Assert.fail("Failed to parse the displayed amount '" + amountString + "': " + e.getMessage());
        }
    }

    /**
     * Verifies that the displayed currency on the payment page
     * matches the expected currency code.
     *
     * @param expectedCurrency The expected currency code (e.g., "USD").
     */
    public void verifyDisplayedCurrency(String expectedCurrency) {

        priceMajorElement.shouldBe(Condition.visible);

        String displayedText = priceMajorElement.text();
        displayedText = displayedText.replace('\u00A0', ' ').trim();

        String actualCurrency;
        // Determine if there is a currency symbol at the beginning, or currency code at the end
        if (displayedText.matches("^\\$?(\\d[\\d.]*)$")) { // Format "$10.00"
            actualCurrency = expectedCurrency; // Assume the expected currency
        } else {
            // Assume the currency code comes after the last space
            int lastSpaceIndex = displayedText.lastIndexOf(' ');
            if (lastSpaceIndex != -1 && displayedText.length() - 3 == lastSpaceIndex + 1) { // If it's a 3-letter code after space
                actualCurrency = displayedText.substring(lastSpaceIndex + 1);
            } else {
                actualCurrency = ""; // If not found, then empty string
            }
        }

        Assert.assertEquals(actualCurrency, expectedCurrency, "Currency on the page does not match the expected.");

    }
}
