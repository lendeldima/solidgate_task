package ui;

import com.solidgate.common.BaseTest;
import com.solidgate.pages.SolidgatePaymentPage;
import com.solidgate.utils.PaymentPageInitializer;
import com.solidgate.utils.TestData;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Selenide.open;


public class SolidgatePaymentTest extends BaseTest {

    private String paymentPageRedirectUrl;
    private SolidgatePaymentPage solidgatePaymentPage;

    @BeforeMethod(alwaysRun = true)

    public void setupPaymentPage() {
        solidgatePaymentPage = new SolidgatePaymentPage();
        paymentPageRedirectUrl = PaymentPageInitializer.createPaymentPageAndGetUrl();
        open(paymentPageRedirectUrl);
    }

    @Test(description = "Verifies displayed price, currency, and performs payment via UI.")
    public void testFullPaymentFlowUI() {

        solidgatePaymentPage.verifyHeaderDisplayedPrice(currentOrderAmount);
        solidgatePaymentPage.verifyHeaderDisplayedCurrency(currentOrderCurrency);
        solidgatePaymentPage.verifySubmitButtonPriceAndCurrencyText(currentOrderAmount, currentOrderCurrency);
        solidgatePaymentPage.enterCardDetails(TestData.TEST_CARD_NUMBER, TestData.TEST_EXPIRY_DATE, TestData.TEST_CVV, TestData.TEST_EMAIL);
        solidgatePaymentPage.clickSubmitPayment();
        solidgatePaymentPage.verifyPaymentSuccess("Оплата пройшла успішно!");

    }
}
