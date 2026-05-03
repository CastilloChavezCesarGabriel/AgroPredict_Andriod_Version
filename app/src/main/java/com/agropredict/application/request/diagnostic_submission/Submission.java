package com.agropredict.application.request.diagnostic_submission;

import com.agropredict.application.diagnostic_submission.Allocation;
import com.agropredict.application.diagnostic_submission.Cropland;
import com.agropredict.application.visitor.ISubmissionVisitor;
import com.agropredict.domain.entity.Diagnostic;

public final class Submission {
    private final Classification prediction;
    private final Subject subject;

    public Submission(Classification prediction, Subject subject) {
        this.prediction = prediction;
        this.subject = subject;
    }

    public Diagnostic diagnose(String identifier) {
        return prediction.derive(identifier);
    }

    public void store(Cropland cropland, Allocation allocation) {
        subject.store(cropland, allocation);
    }

    public void accept(ISubmissionVisitor visitor) {
        prediction.accept(visitor);
    }
}
