package com.agropredict.domain.component.diagnostic;

import com.agropredict.domain.visitor.diagnostic.IDiagnosticVisitor;

public final class Recommendation {
    private final String summary;
    private final String advice;

    public Recommendation(String summary, String advice) {
        if (summary == null && advice == null) {
            throw new IllegalArgumentException("recommendation requires summary or advice");
        }
        this.summary = summary;
        this.advice = advice;
    }

    public void accept(IDiagnosticVisitor visitor) {
        if (summary != null) visitor.visitSummary(summary);
        if (advice != null) visitor.visitRecommendation(advice);
    }
}