package com.agropredict.domain.history;

import java.util.Objects;

public final class HistoryRecord {
    private final FieldModification modification;
    private final HistoryTransition transition;
    private final ChangeMoment timestamp;

    public HistoryRecord(FieldModification modification, HistoryTransition transition, ChangeMoment timestamp) {
        this.modification = Objects.requireNonNull(modification, "history record requires a modification");
        this.transition = Objects.requireNonNull(transition, "history record requires a transition");
        this.timestamp = Objects.requireNonNull(timestamp, "history record requires a timestamp");
    }

    public void inscribe(IModificationConsumer consumer) {
        modification.inscribe(consumer);
    }

    public void link(IHistoryTransitionConsumer consumer) {
        transition.link(consumer);
    }

    public void stamp(ITimestampConsumer consumer) {
        timestamp.stamp(consumer);
    }
}