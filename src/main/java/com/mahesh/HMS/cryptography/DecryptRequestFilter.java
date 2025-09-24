package com.mahesh.HMS.cryptography;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class DecryptRequestFilter extends OncePerRequestFilter {

    private final AESUtil aesUtil;

    public DecryptRequestFilter(AESUtil aesUtil) {
        this.aesUtil = aesUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String method = request.getMethod();
        if ("POST".equals(method) || "PUT".equals(method)) {
            // Read the encrypted body
            String encryptedBody = request.getReader().lines()
                    .reduce("", (accumulator, actual) -> accumulator + actual);
            String decryptedBody;
            try {
                decryptedBody = aesUtil.decrypt(encryptedBody);
            } catch (Exception e) {
                throw new ServletException("Failed to decrypt request body", e);
            }

            // Wrap the request with decrypted body
            DecryptedHttpServletRequestWrapper decryptedRequest =
                    new DecryptedHttpServletRequestWrapper(request, decryptedBody);

            filterChain.doFilter(decryptedRequest, response);
        } else {
            filterChain.doFilter(request, response);
        }
    }

}
