package com.agropredict.domain.diagnostic.assessment;

import com.agropredict.domain.diagnostic.visitor.ISeverityConsumer;
import com.agropredict.domain.diagnostic.visitor.IRecommendationConsumer;
import com.agropredict.domain.diagnostic.visitor.ISeverityLevelConsumer;
import com.agropredict.domain.diagnostic.visitor.ISummaryConsumer;

public interface IAssessment {
    void label(ISeverityConsumer consumer);
    void review(ISeverityLevelConsumer consumer);
    void summarize(ISummaryConsumer consumer);
    void recommend(IRecommendationConsumer consumer);
}
