package com.agropredict.domain.history;

public final class HistoryRecord {
    private final Modification modification;
    private final HistoryTransition transition;

    public HistoryRecord(Modification modification, HistoryTransition transition) {
        this.modification = modification;
        this.transition = transition;
    }

    public void summarize(StringBuilder builder) {
        modification.describe(builder);
        builder.append(": ");
        transition.format(builder);
        builder.append("\n");
        modification.stamp(builder);
        builder.append("\n\n");
    }
}
