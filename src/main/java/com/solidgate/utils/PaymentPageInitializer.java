package com.solidgate.utils;

import com.solidgate.common.BaseTest;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;


public class PaymentPageInitializer extends BaseTest {


    public static String createPaymentPageAndGetUrl() {
        currentOrderId = TestData.generateRandomOrderId();
        currentOrderAmount = 1000;
        currentOrderCurrency = "USD";
        String orderDescription = "Premium package";


        System.out.println("Creating Payment Page for Order ID: " + currentOrderId);

        String jsonPayloadForSignature = "{" +
                "\"order\": {" +
                "\"order_id\": \"" + currentOrderId + "\"," +
                "\"amount\": " + currentOrderAmount + "," +
                "\"currency\": \"" + currentOrderCurrency + "\"," +
                "\"order_description\": \"" + orderDescription + "\"," +
                "\"type\": \"auth\"" +
                "}," +
                "\"page_customization\": {" +
                "\"public_name\": \"Test Store\"" +
                "}" +
                "}";

        String signature = SolidgateSignature.generate(PUBLIC_KEY, jsonPayloadForSignature, SECRET_KEY);

        Response response = ApiRetryHandler.executeWithRetry(() ->
                        given()
                                .baseUri(PAYMENT_PAGE_BASE_URL + "/api/v1")
                                .header("Content-Type", "application/json")
                                .header("Merchant", PUBLIC_KEY)
                                .header("signature", signature)
                                .body(jsonPayloadForSignature)
                                .when()
                                .post("/init")
                                .then()
                                .extract().response(),
                "/v1/init"
        );


        response.then()
                .statusCode(200)
                .body("url", notNullValue());

        String paymentPageRedirectUrl = response.jsonPath().getString("url");
        System.out.println("Obtained payment page URL: " + paymentPageRedirectUrl);
        return paymentPageRedirectUrl;
    }
}