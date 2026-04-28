package com.agropredict.domain.history;

public final class Modification {
    private final String field;
    private final String timestamp;

    public Modification(String field, String timestamp) {
        this.field = field;
        this.timestamp = timestamp;
    }

    public void describe(StringBuilder builder) {
        builder.append(field);
    }

    public void stamp(StringBuilder builder) {
        builder.append(timestamp);
    }
}