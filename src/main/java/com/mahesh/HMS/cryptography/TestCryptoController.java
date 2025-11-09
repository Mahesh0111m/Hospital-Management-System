package com.mahesh.HMS.cryptography;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest; // NEW IMPORT
import java.io.IOException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/crypto")
public class TestCryptoController {

    private final ObjectMapper objectMapper = new ObjectMapper();

    // Helper DTO (Still needed for the encrypt endpoint response)
    public static class Envelope {
        // ... (Envelope fields and methods remain the same)
        private String key;
        private String data;

        public Envelope() {}
        public String getKey() { return key; }
        public void setKey(String key) { this.key = key; }
        public String getData() { return data; }
        public void setData(String data) { this.data = data; }
    }

    // --- ENCRYPT ENDPOINT ---
    @PostMapping("/encrypt")
    public Envelope encryptJson(@RequestBody JsonNode plainJson) throws Exception {
        String aad = "test";
        String encrypted = CryptoUtil.encryptJsonToBase64(plainJson, aad);

        Envelope env = new Envelope();
        env.key = aad;
        env.data = encrypted;
        return env;
    }

    // --- DECRYPT ENDPOINT (FINAL, ABSOLUTE LOWEST-LEVEL FIX) ---
    // Takes HttpServletRequest directly to guarantee reading the stream.
    @PostMapping("/decrypt")
    public JsonNode decryptJson(HttpServletRequest request) throws Exception {

        String rawBody;
        try {
            // Read the input stream directly from the servlet request
            rawBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read raw request body stream.", e);
        }

        if (rawBody.isEmpty()) {
            // This is the literal string content
            throw new IllegalArgumentException("Raw request body is empty after reading the stream.");
        }

        Envelope env;
        try {
            // Manually deserialize the raw JSON string
            env = objectMapper.readValue(rawBody, Envelope.class);
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to parse raw stream as JSON. Check syntax.", e);
        }

        // Line 49 in your trace is here (or near here)
        if (env.data == null || env.data.isEmpty()) {
            // If this fails, the raw JSON was received, but the 'data' field was missing/null/empty.
            throw new IllegalArgumentException("Encrypted data field ('data') cannot be null or empty. Check JSON key name/value.");
        }

        // AAD/key is read from the input envelope
        return CryptoUtil.decryptBase64ToJson(env.data, env.key, JsonNode.class);
    }
}