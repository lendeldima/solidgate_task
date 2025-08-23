package api;

import com.solidgate.common.BaseTest;
import com.solidgate.utils.SolidgateApiActions;

import org.testng.annotations.Test;


public class SolidgateApiTest extends BaseTest {

    @Test(description = "Verifies order and transaction status via API after successful UI payment.", dependsOnMethods = {"ui.SolidgatePaymentTest.testFullPaymentFlowUI"}) // Dependency updated
    public void testOrderStatusAfterPayment() {

        SolidgateApiActions.checkOrderStatus(currentOrderId, currentOrderAmount, currentOrderCurrency);

    }
}