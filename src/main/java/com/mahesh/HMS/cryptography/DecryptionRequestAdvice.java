package com.mahesh.HMS.cryptography;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.lang.reflect.Type;
import java.security.GeneralSecurityException;
import java.io.IOException;

@ControllerAdvice
public class DecryptionRequestAdvice implements RequestBodyAdvice {

    private final ObjectMapper mapper = new ObjectMapper();
    private static final String EXCLUSION_PATH_PREFIX = "/crypto";
    private static final String ENCRYPTED_DATA_FIELD = "data";
    private static final String AAD_KEY_FIELD = "key";

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter,
                                           Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {

        // 1. Check for path exclusion (for test endpoints like /crypto/decrypt)
        if (inputMessage instanceof ServerHttpRequest) {
            String path = ((ServerHttpRequest) inputMessage).getURI().getPath();
            for (String prefix : EXCLUSION_PATH_PREFIX.split(",")) {
                if (path.startsWith(prefix)) {
                    return inputMessage; // Skip decryption
                }
            }

        } else {
            // Cannot determine path, proceed cautiously or skip.
            // Continuing here as general API paths are usually protected.
        }

        // --- General API Request Decryption Logic ---
        try {
            // Read stream bytes fully
            byte[] bodyBytes = inputMessage.getBody().readAllBytes();

            // Check if input is empty or too short before parsing JSON
            if (bodyBytes.length == 0) return inputMessage;

            // Use a temporary stream copy to check the JSON structure
            JsonNode root = mapper.readTree(new ByteArrayInputStream(bodyBytes));

            // Check for the expected encrypted wrapper format
            if (root.has(ENCRYPTED_DATA_FIELD) && root.has(AAD_KEY_FIELD)) {

                JsonNode dataNode = root.get(ENCRYPTED_DATA_FIELD);
                JsonNode keyNode = root.get(AAD_KEY_FIELD);

                if (dataNode == null || dataNode.isNull() || !dataNode.isTextual() || dataNode.asText().isEmpty()) {
                    throw new IllegalArgumentException("Encrypted data field ('data') is missing or empty.");
                }

                String encrypted = dataNode.asText();
                // AAD/Key is optional, check for nulls
                String key = keyNode != null && keyNode.isTextual() ? keyNode.asText() : null;

                // Decrypt the payload. The result is the plaintext JSON node (e.g., {"name": "Mahesh", ...})
                Object decryptedObject = CryptoUtil.decryptBase64ToJson(encrypted, key, Object.class);

                // Write the decrypted plaintext object back into bytes for the new stream
                byte[] decryptedBytes = mapper.writeValueAsBytes(decryptedObject);
                InputStream decryptedStream = new ByteArrayInputStream(decryptedBytes);

                // Return new HttpInputMessage with the plaintext body
                return new HttpInputMessage() {
                    @Override public InputStream getBody() { return decryptedStream; }
                    @Override public org.springframework.http.HttpHeaders getHeaders() { return inputMessage.getHeaders(); }
                };
            }

            // If the body contains content but not the encrypted wrapper, re-wrap original bytes
            return new HttpInputMessage() {
                @Override public InputStream getBody() { return new ByteArrayInputStream(bodyBytes); }
                @Override public org.springframework.http.HttpHeaders getHeaders() { return inputMessage.getHeaders(); }
            };

        } catch (GeneralSecurityException e) {
            throw new RuntimeException("Request decryption failed due to security error.", e);
        } catch (IOException e) {
            throw new RuntimeException("Request processing failed: I/O or JSON error.", e);
        } catch (Exception e) {
            throw new RuntimeException("Request processing failed in DecryptionRequestAdvice.", e);
        }
    }

    // Remaining methods are boilerplate and return the original input/body
    @Override public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) { return body; }
    @Override public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) { return body; }
}