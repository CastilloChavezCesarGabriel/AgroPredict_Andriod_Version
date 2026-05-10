package com.agropredict.domain.diagnostic;

import com.agropredict.domain.diagnostic.visitor.IRecommendationConsumer;
import com.agropredict.domain.diagnostic.visitor.ISummaryConsumer;

public final class Recommendation {
    private final Summary summary;
    private final Advice advice;

    public Recommendation(Summary summary, Advice advice) {
        this.summary = summary;
        this.advice = advice;
    }

    public void summarize(ISummaryConsumer consumer) {
        if (summary != null) {
            summary.summarize(consumer);
        }
    }

    public void recommend(IRecommendationConsumer consumer) {
        if (advice != null) {
            advice.recommend(consumer);
        }
    }
}