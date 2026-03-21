package com.agropredict.application.result;

import com.agropredict.application.visitor.IHistoryVisitor;

public final class HistoryRecord {
    private final Modification modification;
    private final HistoryTransition transition;

    public HistoryRecord(Modification modification, HistoryTransition transition) {
        this.modification = modification;
        this.transition = transition;
    }

    public void accept(IHistoryVisitor visitor) {
        visitor.visit(modification, transition);
    }
}