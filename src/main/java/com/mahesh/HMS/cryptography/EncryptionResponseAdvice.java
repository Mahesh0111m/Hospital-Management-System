package com.mahesh.HMS.cryptography;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Map;

@ControllerAdvice
public class EncryptionResponseAdvice implements ResponseBodyAdvice<Object> {

    private final ObjectMapper mapper = new ObjectMapper();
    private static final String TEST_PATH = "/crypto";
    private static final String AAD = "test"; // Static AAD for encryption
    private static final String DATA_FIELD = "data";
    private static final String KEY_FIELD = "key";

    @Override
    public boolean supports(MethodParameter returnType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {

        String path = request.getURI().getPath();
        for (String prefix : TEST_PATH.split(",")) {
            if (path.startsWith(prefix)) {
                return body; // Skip encryption
            }
        }


        // --- General API Response Encryption Logic ---
        try {
            // Convert the plain Java object (the response body) into a JSON String/Node
            // We use the object directly to be compatible with CryptoUtil

            String encrypted = CryptoUtil.encryptJsonToBase64(body, AAD);

            // Return the result wrapped in the required encrypted format
            return Map.of(KEY_FIELD, AAD, DATA_FIELD, encrypted);

        } catch (Exception e) {
            throw new RuntimeException("Response encryption failed.", e);
        }
    }
}