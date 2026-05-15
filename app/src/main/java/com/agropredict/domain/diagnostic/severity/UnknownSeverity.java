package com.agropredict.domain.diagnostic.severity;

import com.agropredict.domain.diagnostic.visitor.ISeverityConsumer;
import com.agropredict.domain.diagnostic.visitor.ISeverityLevelConsumer;
import java.util.Objects;

public final class UnknownSeverity implements ISeverity {
    private static final String KEY = "unknown";
    private static final int URGENCY = 0;
    private final ISeverityPhrase phrase;

    public UnknownSeverity(ISeverityPhrase phrase) {
        this.phrase = Objects.requireNonNull(phrase, "unknown severity requires a phrase");
    }

    @Override
    public void label(ISeverityConsumer consumer) {
        consumer.label(phrase.describe(), URGENCY);
    }

    @Override
    public void review(ISeverityLevelConsumer consumer) {
        consumer.review(KEY);
    }
}