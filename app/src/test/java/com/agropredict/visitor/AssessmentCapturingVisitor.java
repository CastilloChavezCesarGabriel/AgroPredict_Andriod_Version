package com.agropredict.visitor;

import com.agropredict.domain.visitor.diagnostic.IDiagnosticVisitor;

public final class AssessmentCapturingVisitor implements IDiagnosticVisitor {
    private String shortSummary;
    private String recommendationText;

    @Override
    public void visitSummary(String text) {
        this.shortSummary = text;
    }

    @Override
    public void visitRecommendation(String text) {
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
