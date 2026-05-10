package com.agropredict.domain.diagnostic;

import com.agropredict.domain.guard.ArgumentPrecondition;
import com.agropredict.domain.diagnostic.visitor.ISummaryConsumer;

public final class Summary {
    private final String text;

    public Summary(String text) {
        this.text = ArgumentPrecondition.validate(text, "summary text");
    }

    public void summarize(ISummaryConsumer consumer) {
        consumer.summarize(text);
    }
}