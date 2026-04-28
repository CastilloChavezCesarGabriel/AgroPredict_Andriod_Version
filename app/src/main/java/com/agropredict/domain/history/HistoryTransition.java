package com.agropredict.domain.history;

public final class HistoryTransition {
    private final String previousValue;
    private final String currentValue;

    public HistoryTransition(String previousValue, String currentValue) {
        this.previousValue = previousValue;
        this.currentValue = currentValue;
    }

    public void format(StringBuilder builder) {
        builder.append(previousValue);
        builder.append(" → ");
        builder.append(currentValue);
    }
}