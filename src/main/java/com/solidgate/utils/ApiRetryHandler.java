package com.solidgate.utils;

import io.restassured.response.Response;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Utility class for handling retries for API requests.
 * Uses a delay mechanism, taking into account `Retry-After` headers
 * or standard delays for 429/503 statuses.
 */
public class ApiRetryHandler {

    private static final int MAX_RETRIES = 5; // Maximum number of retries
    private static final int MIN_DELAY_SECONDS = 1; // Minimum delay before retrying
    private static final int MAX_DELAY_SECONDS = 5; // Maximum delay before retrying

    /**
     * Executes an API request with a retry mechanism in case of 429 or 503 statuses.
     *
     * @param requestCallable A Callable object containing the logic for executing the API request, which returns a Response.
     * @param endpointPath The endpoint path for logging.
     * @return A Response object from a successful request.
     * @throws RuntimeException If the request failed to execute after all retries
     * or if another unforeseen error occurred.
     */
    public static Response executeWithRetry(Callable<Response> requestCallable, String endpointPath) {
        int retryCount = 0;
        while (retryCount < MAX_RETRIES) {
            try {
                Response response = requestCallable.call(); // Execute the actual API request
                int statusCode = response.getStatusCode();

                if (statusCode == 429 || statusCode == 503) {
                    int retryAfterSeconds = parseRetryAfterHeader(response);
                    int delay = Math.min(Math.max(retryAfterSeconds, MIN_DELAY_SECONDS), MAX_DELAY_SECONDS);
                    System.out.printf("Received status %d for %s. Retrying after %d seconds. Attempt %d/%d.%n",
                            statusCode, endpointPath, delay, retryCount + 1, MAX_RETRIES);
                    TimeUnit.SECONDS.sleep(delay);
                    retryCount++;
                } else {
                    return response; // Successful response, return it
                }
            } catch (Exception e) {
                System.err.printf("Error executing request to %s: %s. Attempt %d/%d.%n",
                        endpointPath, e.getMessage(), retryCount + 1, MAX_RETRIES);
                retryCount++;
                if (retryCount < MAX_RETRIES) {
                    try {
                        TimeUnit.SECONDS.sleep(MIN_DELAY_SECONDS); // Minimum delay before next attempt for unknown errors
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException("Thread interrupted during delay.", ie);
                    }
                }
            }
        }
        throw new RuntimeException("Request to " + endpointPath + " failed after " + MAX_RETRIES + " attempts.");
    }

    /**
     * Parses the `Retry-After` header value from the response.
     *
     * @param response The Response object from which to get the header.
     * @return The number of seconds to delay, or MIN_DELAY_SECONDS by default if the header is missing or invalid.
     */
    private static int parseRetryAfterHeader(Response response) {
        String retryAfter = response.getHeader("Retry-After");
        if (retryAfter != null && !retryAfter.isEmpty()) {
            try {
                return Integer.parseInt(retryAfter);
            }
            catch (NumberFormatException e) {
                System.err.println("Error parsing 'Retry-After' header: " + retryAfter + ". Using default delay.");
            }
        }
        return MIN_DELAY_SECONDS; // Return minimum default delay
    }
}