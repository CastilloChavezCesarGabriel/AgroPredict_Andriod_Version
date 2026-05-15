package com.agropredict.domain.guard;

public final class MagnitudePrecondition {
    public static long validate(long value, String role) {
        if (value < 0) {
            throw new IllegalArgumentException(role + " must be non-negative");
        }
        return value;
    }
}