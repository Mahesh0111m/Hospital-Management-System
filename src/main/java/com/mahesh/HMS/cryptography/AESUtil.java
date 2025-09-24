package com.mahesh.HMS.cryptography;

import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.util.Base64;
import java.security.SecureRandom;

@Component
public class AESUtil {
    private static final String AES = "AES";
    private static final String AES_GCM = "AES/GCM/NoPadding";
    private static final int GCM_TAG_LENGTH = 128;
    private static final int IV_LENGTH = 12;

    private final SecretKey secretKey;

    public AESUtil(SecretKey secretKey) {
        this.secretKey = secretKey;
    }

    public String encrypt(String plainText) throws Exception {
        byte[] iv = new byte[IV_LENGTH];
        new SecureRandom().nextBytes(iv);

        Cipher cipher = Cipher.getInstance(AES_GCM);
        GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, spec);

        byte[] cipherText = cipher.doFinal(plainText.getBytes());

        // Store IV with ciphertext
        byte[] cipherWithIv = new byte[iv.length + cipherText.length];
        System.arraycopy(iv, 0, cipherWithIv, 0, iv.length);
        System.arraycopy(cipherText, 0, cipherWithIv, iv.length, cipherText.length);

        return Base64.getEncoder().encodeToString(cipherWithIv);
    }

    public String decrypt(String encrypted) throws Exception {
        byte[] cipherWithIv = Base64.getDecoder().decode(encrypted);

        byte[] iv = new byte[IV_LENGTH];
        byte[] cipherText = new byte[cipherWithIv.length - IV_LENGTH];
        System.arraycopy(cipherWithIv, 0, iv, 0, iv.length);
        System.arraycopy(cipherWithIv, iv.length, cipherText, 0, cipherText.length);

        Cipher cipher = Cipher.getInstance(AES_GCM);
        GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, spec);

        byte[] plainText = cipher.doFinal(cipherText);
        return new String(plainText);
    }

    // Example key generator (in real-world: load from Secret Manager)
    public static SecretKey generateKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(AES);
        keyGenerator.init(256);
        return keyGenerator.generateKey();
    }
}
