package com.mahesh.HMS.cryptography;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

@Configuration
public class CryptoConfig {

    @Bean
    public SecretKey secretKey() throws Exception {
        return AESUtil.generateKey();
    }

    @Bean
    public AESUtil aesUtil(SecretKey secretKey) {
        return new AESUtil(secretKey);
    }
}
