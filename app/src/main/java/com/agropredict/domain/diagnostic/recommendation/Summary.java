package com.agropredict.domain.diagnostic.recommendation;

import com.agropredict.domain.guard.ArgumentPrecondition;
import com.agropredict.domain.diagnostic.visitor.ISummaryConsumer;

public final class Summary implements ISummary {
    private final String text;

    public Summary(String text) {
        this.text = ArgumentPrecondition.validate(text, "summary text");
    }

    @Override
    public void summarize(ISummaryConsumer consumer) {
        consumer.summarize(text);
    }
}
