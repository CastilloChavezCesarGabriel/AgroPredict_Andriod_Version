package com.agropredict.domain.diagnostic;

import com.agropredict.domain.diagnostic.assessment.PendingAssessment;
import com.agropredict.domain.diagnostic.classification.Prediction;
import com.agropredict.domain.diagnostic.recommendation.Recommendation;
import com.agropredict.domain.diagnostic.severity.ISeverity;
import com.agropredict.domain.diagnostic.visitor.IDiagnosticPairConsumer;
import com.agropredict.domain.diagnostic.visitor.IDiagnosticTargetConsumer;
import com.agropredict.domain.diagnostic.visitor.IPredictionConsumer;
import com.agropredict.domain.diagnostic.visitor.IRecommendationConsumer;
import com.agropredict.domain.diagnostic.visitor.ISeverityConsumer;
import com.agropredict.domain.diagnostic.visitor.ISeverityLevelConsumer;
import com.agropredict.domain.diagnostic.visitor.ISummaryConsumer;
import com.agropredict.domain.guard.ArgumentPrecondition;
import com.agropredict.domain.identifier.IIdentifierConsumer;
import java.util.Objects;

public final class Diagnostic {
    private final String identifier;
    private final Prediction prediction;
    private final DiagnosticState state;

    public Diagnostic(String identifier, Prediction prediction, DiagnosticState state) {
        this.identifier = ArgumentPrecondition.validate(identifier, "diagnostic identifier");
        this.prediction = Objects.requireNonNull(prediction, "diagnostic requires a prediction");
        this.state = Objects.requireNonNull(state, "diagnostic requires a state");
    }

    public static Diagnostic begin(String identifier, Prediction prediction, ISeverity pendingSeverity) {
        DiagnosticState initial = new DiagnosticState(new PendingAssessment(pendingSeverity), new NoDiagnosticTarget());
        return new Diagnostic(identifier, prediction, initial);
    }

    public Diagnostic conclude(ISeverity severity, Recommendation recommendation) {
        return new Diagnostic(identifier, prediction, state.conclude(severity, recommendation));
    }

    public Diagnostic link(String cropIdentifier, String imageIdentifier) {
        return new Diagnostic(identifier, prediction, state.link(cropIdentifier, imageIdentifier));
    }

    public boolean isConfident() {
        return prediction.isConfident();
    }

    public void identify(IIdentifierConsumer consumer) {
        consumer.identify(identifier);
    }

    public void classify(IPredictionConsumer consumer) {
        prediction.classify(consumer);
    }

    public void label(ISeverityConsumer consumer) {
        state.label(consumer);
    }

    public void review(ISeverityLevelConsumer consumer) {
        state.review(consumer);
    }

    public void summarize(ISummaryConsumer consumer) {
        state.summarize(consumer);
    }

    public void recommend(IRecommendationConsumer consumer) {
        state.recommend(consumer);
    }

    public void bind(IDiagnosticTargetConsumer consumer) {
        state.bind(consumer);
    }

    public void pair(String otherIdentifier, IDiagnosticPairConsumer consumer) {
        consumer.pair(identifier, otherIdentifier);
    }
}
