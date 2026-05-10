package com.agropredict.visitor;

import com.agropredict.domain.diagnostic.visitor.IRecommendationConsumer;
import com.agropredict.domain.diagnostic.visitor.ISummaryConsumer;

public final class AssessmentCapturingVisitor implements ISummaryConsumer, IRecommendationConsumer {
    private String shortSummary;
    private String recommendationText;

    @Override
    public void summarize(String text) {
        this.shortSummary = text;
    }

    @Override
    public void recommend(String text) {
        this.recommendationText = text;
    }

    public boolean recordedSummary(String expected) {
        return expected != null && expected.equals(shortSummary);
    }

    public boolean summaryMentions(String fragment) {
        return shortSummary != null && fragment != null && shortSummary.contains(fragment);
    }

    public boolean recommendationMentions(String fragment) {
        return recommendationText != null && fragment != null && recommendationText.contains(fragment);
    }

    public boolean recommendationLacks(String fragment) {
        return fragment != null && (recommendationText == null || !recommendationText.contains(fragment));
    }
}
