package com.agropredict.domain;

import java.util.concurrent.atomic.AtomicLong;

public final class Identifier {
    private static final AtomicLong counter = new AtomicLong();

    public static String generate(String prefix) {
        return prefix + "_" + System.currentTimeMillis() + "_" + counter.incrementAndGet();
    }
}
