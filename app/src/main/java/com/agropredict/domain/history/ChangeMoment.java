package com.agropredict.domain.history;

import com.agropredict.domain.guard.ArgumentPrecondition;

public final class ChangeMoment {
    private final String value;

    public ChangeMoment(String value) {
        this.value = ArgumentPrecondition.validate(value, "change moment value");
    }

    public void stamp(ITimestampConsumer consumer) {
        consumer.stamp(value);
    }
}