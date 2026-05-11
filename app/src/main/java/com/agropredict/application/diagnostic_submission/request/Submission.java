package com.agropredict.application.diagnostic_submission.request;

import com.agropredict.application.diagnostic_submission.workflow.CropRegistry;
import com.agropredict.application.diagnostic_submission.workflow.SubmissionIdentity;
import com.agropredict.domain.diagnostic.visitor.IPredictionConsumer;
import com.agropredict.domain.diagnostic.Diagnostic;
import java.util.Objects;

public final class Submission {
    private final ImagePrediction prediction;
    private final DiagnosticSubject subject;

    public Submission(ImagePrediction prediction, DiagnosticSubject subject) {
        this.prediction = Objects.requireNonNull(prediction, "submission requires a classification");
        this.subject = Objects.requireNonNull(subject, "submission requires a subject");
    }

    public Diagnostic diagnose(String identifier) {
        return prediction.diagnose(identifier);
    }

    public void store(CropRegistry registry, SubmissionIdentity identity) {
        identity.enroll(subject, registry);
    }

    public void dispatch(IPredictionConsumer consumer) {
        prediction.accept(consumer);
    }
}
