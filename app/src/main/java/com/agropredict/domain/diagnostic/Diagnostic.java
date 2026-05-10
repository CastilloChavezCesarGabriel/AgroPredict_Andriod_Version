package com.agropredict.domain.diagnostic;

import com.agropredict.domain.identifier.IIdentifierConsumer;
import com.agropredict.domain.diagnostic.visitor.ISeverityConsumer;
import com.agropredict.domain.guard.ArgumentPrecondition;
import com.agropredict.domain.diagnostic.visitor.IDiagnosticPairConsumer;
import com.agropredict.domain.diagnostic.visitor.IPredictionConsumer;
import com.agropredict.domain.diagnostic.visitor.IRecommendationConsumer;
import com.agropredict.domain.diagnostic.visitor.ISeverityLevelConsumer;
import com.agropredict.domain.diagnostic.visitor.ISummaryConsumer;
import com.agropredict.domain.diagnostic.visitor.IDiagnosticTargetConsumer;
import java.util.Objects;

public final class Diagnostic {
    private final String identifier;
    private final Prediction prediction;
    private IAssessment assessment;
    private DiagnosticTarget target;

    public Diagnostic(String identifier, Prediction prediction) {
        this.identifier = ArgumentPrecondition.validate(identifier, "diagnostic identifier");
        this.prediction = Objects.requireNonNull(prediction, "diagnostic requires a prediction");
        this.assessment = new PendingAssessment();
    }

    public void conclude(Severity severity, Recommendation recommendation) {
        this.assessment = new ConcludedAssessment(severity, recommendation);
    }

    public void link(String cropIdentifier, String imageIdentifier) {
        this.target = new DiagnosticTarget(cropIdentifier, imageIdentifier);
    }

    public boolean isConfident() {
        return prediction.isConfident();
    }

    public void label(ISeverityConsumer consumer) {
        assessment.label(consumer);
    }

    public void identify(IIdentifierConsumer consumer) {
        consumer.identify(identifier);
    }

    public void classify(IPredictionConsumer consumer) {
        prediction.classify(consumer);
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
        if (target != null) {
            target.bind(consumer);
        }
    }

    public void pair(String otherIdentifier, IDiagnosticPairConsumer consumer) {
        consumer.pair(identifier, otherIdentifier);
    }
}