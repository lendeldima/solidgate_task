package com.solidgate.utils;

import com.google.common.hash.Hashing;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class SolidgateSignature {


    public static String generate(String publicKey, String jsonString, String secretKey) {
        if (publicKey == null || publicKey.isEmpty()) {
            throw new IllegalArgumentException("Public key cannot be empty.");
        }
        if (jsonString == null || jsonString.isEmpty()) {
            throw new IllegalArgumentException("JSON string for signature cannot be empty.");
        }
        if (secretKey == null || secretKey.isEmpty()) {
            throw new IllegalArgumentException("Secret key cannot be empty.");
        }

        // 1. Create the string for hashing: publicKey + jsonString + publicKey
        String dataToHash = publicKey + jsonString + publicKey;

        // 2. Apply HMAC-SHA512 hashing with the secret key
        byte[] hashedBytes = Hashing.hmacSha512(secretKey.getBytes())
                .hashString(dataToHash, StandardCharsets.UTF_8)
                .toString() // Convert the hash to its hexadecimal string representation
                .getBytes(StandardCharsets.UTF_8); // Encode the hexadecimal string into bytes (using UTF_8 for safety)

        // 3. Base64 encode the byte array
        return Base64.getEncoder().encodeToString(hashedBytes);
    }
}