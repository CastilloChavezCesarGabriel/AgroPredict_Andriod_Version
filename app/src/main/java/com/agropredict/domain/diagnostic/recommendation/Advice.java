package com.agropredict.domain.diagnostic.recommendation;

import com.agropredict.domain.guard.ArgumentPrecondition;
import com.agropredict.domain.diagnostic.visitor.IRecommendationConsumer;

public final class Advice implements IAdvice {
    private final String text;

    public Advice(String text) {
        this.text = ArgumentPrecondition.validate(text, "advice text");
    }

    @Override
    public void recommend(IRecommendationConsumer consumer) {
        consumer.recommend(text);
    }
}
