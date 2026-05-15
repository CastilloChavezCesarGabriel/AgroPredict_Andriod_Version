package com.agropredict.domain.diagnostic.recommendation;

import com.agropredict.domain.diagnostic.visitor.IRecommendationConsumer;
import com.agropredict.domain.diagnostic.visitor.ISummaryConsumer;
import java.util.Objects;

public final class Recommendation {
    private final ISummary summary;
    private final IAdvice advice;

    public Recommendation(ISummary summary, IAdvice advice) {
        this.summary = Objects.requireNonNull(summary, "recommendation requires a summary");
        this.advice = Objects.requireNonNull(advice, "recommendation requires an advice");
    }

    public void summarize(ISummaryConsumer consumer) {
        summary.summarize(consumer);
    }

    public void recommend(IRecommendationConsumer consumer) {
        advice.recommend(consumer);
    }
}
