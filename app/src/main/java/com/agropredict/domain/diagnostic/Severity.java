package com.agropredict.domain.diagnostic;

import com.agropredict.domain.diagnostic.visitor.ISeverityConsumer;
import com.agropredict.domain.guard.ArgumentPrecondition;
import com.agropredict.domain.diagnostic.visitor.ISeverityLevelConsumer;

public final class Severity {
    private final String value;
    private final String text;
    private final int urgency;

    public Severity(String value, String text, int urgency) {
        this.value = value;
        this.text = ArgumentPrecondition.validate(text, "severity text");
        this.urgency = urgency;
    }

    public void label(ISeverityConsumer consumer) {
        consumer.label(text, urgency);
    }

    public void review(ISeverityLevelConsumer consumer) {
        consumer.review(value);
    }
}
