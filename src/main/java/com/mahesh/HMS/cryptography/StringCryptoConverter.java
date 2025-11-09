package com.mahesh.HMS.cryptography;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

// This converter tells JPA to apply encryption/decryption whenever a String field uses it.
@Converter
public class StringCryptoConverter implements AttributeConverter<String, String> {

    // Note: CryptoUtil methods must be static or you need to inject it.
    // Assuming CryptoUtil.encrypt/decrypt methods are static for simplicity.

    @Override
    public String convertToDatabaseColumn(String entityValue) {
        if (entityValue == null) {
            return null;
        }
        try {
            // Use your CryptoUtil to encrypt the plaintext value
            // NOTE: You need to decide on the AAD/key value here,
            // but for simplicity, let's use a dummy value compatible with your CryptoUtil.
            String AAD_KEY = "field-aad";

            // This is the core save action: Encrypt the value before writing to the DB.
            return CryptoUtil.encryptJsonToBase64(entityValue, AAD_KEY);
        } catch (Exception e) {
            throw new RuntimeException("Could not encrypt database field", e);
        }
    }

    @Override
    public String convertToEntityAttribute(String databaseValue) {
        if (databaseValue == null) {
            return null;
        }
        try {
            // This is the core read action: Decrypt the value after reading from the DB.
            String AAD_KEY = "field-aad";

            // Your CryptoUtil decrypts JSON to an Object, so we need to handle the type.
            // Since the input was a String, we decrypt it back to a String.
            return CryptoUtil.decryptBase64ToJson(databaseValue, AAD_KEY, String.class);
        } catch (Exception e) {
            // WARNING: Data read from DB may be corrupted or unreadable!
            System.err.println("Could not decrypt database field: " + databaseValue);
            return null;
        }
    }
}