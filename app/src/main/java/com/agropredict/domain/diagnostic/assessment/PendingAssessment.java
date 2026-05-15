package com.agropredict.domain.diagnostic.assessment;

import com.agropredict.domain.diagnostic.recommendation.IAdvice;
import com.agropredict.domain.diagnostic.recommendation.ISummary;
import com.agropredict.domain.diagnostic.recommendation.NoAdvice;
import com.agropredict.domain.diagnostic.recommendation.NoSummary;
import com.agropredict.domain.diagnostic.severity.ISeverity;
import com.agropredict.domain.diagnostic.visitor.ISeverityConsumer;
import com.agropredict.domain.diagnostic.visitor.IRecommendationConsumer;
import com.agropredict.domain.diagnostic.visitor.ISeverityLevelConsumer;
import com.agropredict.domain.diagnostic.visitor.ISummaryConsumer;
import java.util.Objects;

public final class PendingAssessment implements IAssessment {
    private final ISeverity severity;
    private final ISummary summary;
    private final IAdvice advice;

    public PendingAssessment(ISeverity severity) {
        this.severity = Objects.requireNonNull(severity, "pending assessment requires a severity");
        this.summary = new NoSummary();
        this.advice = new NoAdvice();
    }

    @Override
    public void label(ISeverityConsumer consumer) {
        severity.label(consumer);
    }

    @Override
    public void review(ISeverityLevelConsumer consumer) {
        severity.review(consumer);
    }

    @Override
    public void summarize(ISummaryConsumer consumer) {
        summary.summarize(consumer);
    }

    @Override
    public void recommend(IRecommendationConsumer consumer) {
        advice.recommend(consumer);
    }
}
