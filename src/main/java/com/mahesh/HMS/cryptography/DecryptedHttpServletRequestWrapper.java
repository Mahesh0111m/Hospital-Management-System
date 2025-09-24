package com.mahesh.HMS.cryptography;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.ServletInputStream;


import java.io.*;

public class DecryptedHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private final String body;

    public DecryptedHttpServletRequestWrapper(HttpServletRequest request, String body) {
        super(request);
        this.body = body;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body.getBytes());
        return new BufferedReader(new InputStreamReader(byteArrayInputStream));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body.getBytes());
        return new ServletInputStream() {
            @Override
            public int read() throws IOException { return byteArrayInputStream.read(); }
            @Override
            public boolean isFinished() { return byteArrayInputStream.available() == 0; }
            @Override
            public boolean isReady() { return true; }
            @Override
            public void setReadListener(jakarta.servlet.ReadListener readListener) {
                // Not implemented
            }

        };
    }
}
