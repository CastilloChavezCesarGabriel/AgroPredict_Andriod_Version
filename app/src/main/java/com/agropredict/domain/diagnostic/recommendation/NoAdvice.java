package com.agropredict.domain.diagnostic.recommendation;

import com.agropredict.domain.diagnostic.visitor.IRecommendationConsumer;

public final class NoAdvice implements IAdvice {
    @Override
    public void recommend(IRecommendationConsumer consumer) {}
}