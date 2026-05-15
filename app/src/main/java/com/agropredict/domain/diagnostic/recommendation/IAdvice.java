package com.agropredict.domain.diagnostic.recommendation;

import com.agropredict.domain.diagnostic.visitor.IRecommendationConsumer;

public interface IAdvice {
    void recommend(IRecommendationConsumer consumer);
}
