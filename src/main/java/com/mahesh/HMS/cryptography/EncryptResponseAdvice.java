package com.mahesh.HMS.cryptography;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class EncryptResponseAdvice implements ResponseBodyAdvice<Object> {

    private final AESUtil aesUtil;
    private final ObjectMapper objectMapper;

    public EncryptResponseAdvice(AESUtil aesUtil, ObjectMapper objectMapper) {
        this.aesUtil = aesUtil;
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true; // apply to all controllers
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {
        try {
            // Convert object → JSON → Encrypt → Base64
            String json = objectMapper.writeValueAsString(body);
            return aesUtil.encrypt(json);
        } catch (Exception e) {
            throw new RuntimeException("Encryption failed", e);
        }
    }
}
