package com.agropredict.domain.diagnostic;

import com.agropredict.domain.diagnostic.assessment.ConcludedAssessment;
import com.agropredict.domain.diagnostic.assessment.IAssessment;
import com.agropredict.domain.diagnostic.recommendation.Recommendation;
import com.agropredict.domain.diagnostic.severity.ISeverity;
import com.agropredict.domain.diagnostic.visitor.IDiagnosticTargetConsumer;
import com.agropredict.domain.diagnostic.visitor.IRecommendationConsumer;
import com.agropredict.domain.diagnostic.visitor.ISeverityConsumer;
import com.agropredict.domain.diagnostic.visitor.ISeverityLevelConsumer;
import com.agropredict.domain.diagnostic.visitor.ISummaryConsumer;
import java.util.Objects;

public final class DiagnosticState {
    private final IAssessment assessment;
    private final IDiagnosticTarget target;

    public DiagnosticState(IAssessment assessment, IDiagnosticTarget target) {
        this.assessment = Objects.requireNonNull(assessment, "diagnostic state requires an assessment");
        this.target = Objects.requireNonNull(target, "diagnostic state requires a target");
    }

    public DiagnosticState conclude(ISeverity severity, Recommendation recommendation) {
        return new DiagnosticState(new ConcludedAssessment(severity, recommendation), target);
    }

    public DiagnosticState link(String cropIdentifier, String imageIdentifier) {
        return new DiagnosticState(assessment, new DiagnosticTarget(cropIdentifier, imageIdentifier));
    }

    public void label(ISeverityConsumer consumer) {
        assessment.label(consumer);
    }

    public void review(ISeverityLevelConsumer consumer) {
        assessment.review(consumer);
    }

    public void summarize(ISummaryConsumer consumer) {
        assessment.summarize(consumer);
    }

    public void recommend(IRecommendationConsumer consumer) {
        assessment.recommend(consumer);
    }

    public void bind(IDiagnosticTargetConsumer consumer) {
        target.bind(consumer);
    }
}
