package com.agropredict.domain.identifier;

import java.util.UUID;

public final class IdentifierFactory {
    public static String generate(String prefix) {
        return prefix + "_" + UUID.randomUUID();
    }
}