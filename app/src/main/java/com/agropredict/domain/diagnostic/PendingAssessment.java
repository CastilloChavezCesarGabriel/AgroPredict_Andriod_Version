package com.agropredict.domain.diagnostic;

import com.agropredict.domain.diagnostic.visitor.ISeverityConsumer;
import com.agropredict.domain.diagnostic.visitor.IRecommendationConsumer;
import com.agropredict.domain.diagnostic.visitor.ISeverityLevelConsumer;
import com.agropredict.domain.diagnostic.visitor.ISummaryConsumer;

public final class PendingAssessment implements IAssessment {
    @Override
    public void label(ISeverityConsumer consumer) {
        consumer.label("Pending", 0);
    }

    @Override
    public void review(ISeverityLevelConsumer consumer) {}

    @Override
    public void summarize(ISummaryConsumer consumer) {}

    @Override
    public void recommend(IRecommendationConsumer consumer) {}
}
