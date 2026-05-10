package com.agropredict.domain.history;

import com.agropredict.domain.guard.ArgumentPrecondition;

public final class FieldModification {
    private final String field;

    public FieldModification(String field) {
        this.field = ArgumentPrecondition.validate(field, "modification field");
    }

    public void inscribe(IModificationConsumer consumer) {
        consumer.inscribe(field);
    }
}