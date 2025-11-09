package com.mahesh.HMS.cryptography;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.security.GeneralSecurityException;
import java.io.IOException;

public class CryptoUtil {
    // --- Configuration ---
    private static final String AES = "AES";
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int IV_SIZE = 12;
    private static final int TAG_LENGTH_BITS = 128;

    // --- Static Key (DEV ONLY) ---
    private static final String BASE64_KEY = "qB1tY1g6p1mCkFg5qKu1c9m7qZ+0r2Y8z3fL1jN7cXo=";
    private static final SecretKeySpec KEY_SPEC =
            new SecretKeySpec(Base64.getDecoder().decode(BASE64_KEY), AES);

    private static final SecureRandom RNG = new SecureRandom();

    // --- FIXED ObjectMapper ---
    private static final ObjectMapper MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    public static String encryptJsonToBase64(Object jsonObject, String aad)
            throws GeneralSecurityException, IOException {

        byte[] iv = new byte[IV_SIZE];
        RNG.nextBytes(iv);

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH_BITS, iv);
        cipher.init(Cipher.ENCRYPT_MODE, KEY_SPEC, spec);

        if (aad != null) {
            cipher.updateAAD(aad.getBytes(StandardCharsets.UTF_8));
        }

        byte[] plaintext = MAPPER.writeValueAsBytes(jsonObject);
        byte[] cipherBytes = cipher.doFinal(plaintext);

        byte[] out = new byte[iv.length + cipherBytes.length];
        System.arraycopy(iv, 0, out, 0, iv.length);
        System.arraycopy(cipherBytes, 0, out, iv.length, cipherBytes.length);

        return Base64.getEncoder().encodeToString(out);
    }

    public static <T> T decryptBase64ToJson(String base64IvCipher, String aad, Class<T> valueType)
            throws GeneralSecurityException, IOException {

        byte[] all = Base64.getDecoder().decode(base64IvCipher);
        if (all.length < IV_SIZE) throw new IllegalArgumentException("Input too short");

        byte[] iv = new byte[IV_SIZE];
        System.arraycopy(all, 0, iv, 0, IV_SIZE);
        byte[] cipherBytes = new byte[all.length - IV_SIZE];
        System.arraycopy(all, IV_SIZE, cipherBytes, 0, cipherBytes.length);

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH_BITS, iv);
        cipher.init(Cipher.DECRYPT_MODE, KEY_SPEC, spec);

        if (aad != null) {
            cipher.updateAAD(aad.getBytes(StandardCharsets.UTF_8));
        }

        byte[] plain = cipher.doFinal(cipherBytes);
        return MAPPER.readValue(plain, valueType);
    }
}
