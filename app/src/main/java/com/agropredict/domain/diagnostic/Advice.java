package com.agropredict.domain.diagnostic;

import com.agropredict.domain.guard.ArgumentPrecondition;
import com.agropredict.domain.diagnostic.visitor.IRecommendationConsumer;

public final class Advice {
    private final String text;

    public Advice(String text) {
        this.text = ArgumentPrecondition.validate(text, "advice text");
    }

    public void recommend(IRecommendationConsumer consumer) {
        consumer.recommend(text);
    }
}