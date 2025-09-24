package com.mahesh.HMS.cryptography;

import org.springframework.web.bind.annotation.*;
import javax.crypto.SecretKey;

@RestController
@RequestMapping("/crypto")
public class CryptoController {

    private final AESUtil aesUtil;

    public CryptoController() throws Exception {
        // ⚠️ In real apps: load key from Vault/env, don’t generate each time
        SecretKey key = AESUtil.generateKey();
        this.aesUtil = new AESUtil(key);
    }

    @PostMapping("/encrypt")
    public String encrypt(@RequestBody String plainText) throws Exception {
        return aesUtil.encrypt(plainText);
    }

    @PostMapping("/decrypt")
    public String decrypt(@RequestBody String cipherText) throws Exception {
        return aesUtil.decrypt(cipherText);
    }
}
