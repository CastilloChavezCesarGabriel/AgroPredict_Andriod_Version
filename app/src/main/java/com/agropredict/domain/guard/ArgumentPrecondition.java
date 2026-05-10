package com.agropredict.domain.guard;

public final class ArgumentPrecondition {
    public static String validate(String value, String role) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(role + " is required");
        }
        return value;
    }
}