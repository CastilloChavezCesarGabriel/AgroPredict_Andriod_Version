package com.agropredict.domain.diagnostic.severity;

import com.agropredict.domain.diagnostic.visitor.ISeverityConsumer;
import com.agropredict.domain.diagnostic.visitor.ISeverityLevelConsumer;
import java.util.Objects;

public final class ModerateSeverity implements ISeverity {
    private static final String KEY = "moderate";
    private static final int URGENCY = 1;
    private final ISeverityPhrase phrase;

    public ModerateSeverity(ISeverityPhrase phrase) {
        this.phrase = Objects.requireNonNull(phrase, "moderate severity requires a phrase");
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
