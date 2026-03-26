package com.agropredict.domain;

public final class Identifier {
    private Identifier() {}

    public static String generate(String prefix) {
        return prefix + "_" + System.currentTimeMillis();
    }
}
