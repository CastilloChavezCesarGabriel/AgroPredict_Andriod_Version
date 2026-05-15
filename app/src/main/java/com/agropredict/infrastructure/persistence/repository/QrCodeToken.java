package com.agropredict.infrastructure.persistence.repository;

import java.security.SecureRandom;
import java.util.Locale;

public final class QrCodeToken {
    private static final int LENGTH_BYTES = 16;

    public String generate() {
        byte[] bytes = new byte[LENGTH_BYTES];
        new SecureRandom().nextBytes(bytes);
        StringBuilder builder = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) builder.append(String.format(Locale.US, "%02x", b));
        return builder.toString();
    }
}