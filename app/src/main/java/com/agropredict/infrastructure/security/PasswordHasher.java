package com.agropredict.infrastructure.security;

import com.agropredict.application.service.IPasswordHasher;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public final class PasswordHasher implements IPasswordHasher {
    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH = 256;
    private static final int SALT_LENGTH = 16;
    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final String DELIMITER = ":";

    @Override
    public String hash(String password) {
        byte[] salt = salt();
        byte[] derived = derive(password, salt);
        return encode(salt) + DELIMITER + encode(derived);
    }

    @Override
    public boolean isVerified(String password, String stored) {
        String[] parts = stored.split(DELIMITER);
        if (parts.length != 2) return false;
        byte[] salt = decode(parts[0]);
        byte[] expected = decode(parts[1]);
        byte[] actual = derive(password, salt);
        return isEqual(expected, actual);
    }

    private byte[] salt() {
        byte[] salt = new byte[SALT_LENGTH];
        new SecureRandom().nextBytes(salt);
        return salt;
    }

    private byte[] derive(String password, byte[] salt) {
        try {
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
            SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM);
            return factory.generateSecret(spec).getEncoded();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private String encode(byte[] bytes) {
        StringBuilder hex = new StringBuilder();
        for (byte value : bytes) {
            String digit = Integer.toHexString(0xff & value);
            if (digit.length() == 1) hex.append('0');
            hex.append(digit);
        }
        return hex.toString();
    }

    private byte[] decode(String encoded) {
        int length = encoded.length();
        byte[] bytes = new byte[length / 2];
        for (int index = 0; index < length; index += 2) {
            bytes[index / 2] = (byte) Integer.parseInt(encoded.substring(index, index + 2), 16);
        }
        return bytes;
    }

    private boolean isEqual(byte[] expected, byte[] actual) {
        if (expected.length != actual.length) return false;
        int result = 0;
        for (int index = 0; index < expected.length; index++) {
            result |= expected[index] ^ actual[index];
        }
        return result == 0;
    }
}