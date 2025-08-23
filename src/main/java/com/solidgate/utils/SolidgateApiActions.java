package com.solidgate.utils;

import com.solidgate.common.BaseTest;
import io.restassured.response.Response;
import org.testng.Assert;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;



public class SolidgateApiActions extends BaseTest {

    /**
     * Executes a request to the Solidgate API to check the order status
     * and includes assertions about the response.
     *
     * @param orderId ID of the order to check.
     * @param expectedAmount The expected amount of the order in minor units.
     * @param expectedCurrency The expected currency of the order.
     * @return A Response object with the API response.
     */
    public static Response checkOrderStatus(String orderId, int expectedAmount, String expectedCurrency) {
        System.out.println("Performing order status check for order " + orderId + " via API...");

        String jsonPayloadForSignature = "{" +

                "\"order_id\": \"" + currentOrderId + "\"" +

                "}";

        String signature = SolidgateSignature.generate(PUBLIC_KEY, jsonPayloadForSignature, SECRET_KEY);

        Response response = ApiRetryHandler.executeWithRetry(() ->
                        given()
                                .baseUri(API_BASE_URL + "/api/v1") // Set the base URL for the main API
                                .header("Content-Type", "application/json")
                                .header("Merchant", PUBLIC_KEY) // Merchant header
                                .header("signature", signature) // Signature header
                                .body(jsonPayloadForSignature) // Use finalRequestBody
                                .when()
                                .post("/status") // Endpoint without base URL, as it's set via baseUri()
                                .then()
                                .extract().response(),
                "/v1/status"
        );

        response.then()
                .statusCode(200)
                .body("order.status", equalTo("auth_ok")) // Verify overall order status
                .body("order.amount", equalTo(currentOrderAmount))
                .body("order.currency", equalTo(currentOrderCurrency));

        Map<String, ?> transactionsMap = response.jsonPath().getMap("transactions");
        if (transactionsMap != null && !transactionsMap.isEmpty()) {
            String firstTransactionKey = transactionsMap.keySet().iterator().next();
            response.then()
                    .body("transactions." + firstTransactionKey + ".status", equalTo("success"));
        } else {

            Assert.fail("Warning: No transactions found in the response object for order " + orderId);
        }

        System.out.println("Order status and transactions successfully verified.");

        return response;
    }
}