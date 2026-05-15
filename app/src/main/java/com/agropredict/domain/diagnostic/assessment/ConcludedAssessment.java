package com.agropredict.domain.diagnostic.assessment;

import com.agropredict.domain.diagnostic.recommendation.Recommendation;
import com.agropredict.domain.diagnostic.severity.ISeverity;
import com.agropredict.domain.diagnostic.visitor.ISeverityConsumer;
import com.agropredict.domain.diagnostic.visitor.IRecommendationConsumer;
import com.agropredict.domain.diagnostic.visitor.ISeverityLevelConsumer;
import com.agropredict.domain.diagnostic.visitor.ISummaryConsumer;
import java.util.Objects;

public final class ConcludedAssessment implements IAssessment {
    private final ISeverity severity;
    private final Recommendation recommendation;

    public ConcludedAssessment(ISeverity severity, Recommendation recommendation) {
        this.severity = Objects.requireNonNull(severity,
                "concluded assessment requires a severity");
        this.recommendation = Objects.requireNonNull(recommendation,
                "concluded assessment requires a recommendation");
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
        recommendation.summarize(consumer);
    }

    @Override
    public void recommend(IRecommendationConsumer consumer) {
        recommendation.recommend(consumer);
    }
}