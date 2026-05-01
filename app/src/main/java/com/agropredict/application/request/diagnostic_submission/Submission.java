package com.agropredict.application.request.diagnostic_submission;

import com.agropredict.application.repository.ICropRepository;
import com.agropredict.application.repository.IPhotographRepository;
import com.agropredict.application.visitor.ISubmissionVisitor;
import com.agropredict.domain.entity.Diagnostic;

public final class Submission {
    private final Classification prediction;
    private final SubmissionField field;

    public Submission(Classification prediction, SubmissionField field) {
        this.prediction = prediction;
        this.field = field;
    }

    public Diagnostic diagnose(String identifier) {
        return prediction.derive(identifier);
    }

    public void store(ICropRepository cropRepository, IPhotographRepository photoRepository) {
        field.store(cropRepository, photoRepository);
    }

    public void accept(ISubmissionVisitor visitor) {
        prediction.accept(visitor);
    }
}