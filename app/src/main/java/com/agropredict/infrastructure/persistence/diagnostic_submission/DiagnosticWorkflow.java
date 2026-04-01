package com.agropredict.infrastructure.persistence.diagnostic_submission;

import com.agropredict.application.repository.IDiagnosticWorkflow;
import com.agropredict.application.request.diagnostic_submission.SubmissionRequest;
import com.agropredict.domain.entity.Diagnostic;

public final class DiagnosticWorkflow implements IDiagnosticWorkflow {
    private final Submission submission;
    private final SubmissionRecorder submissionRecorder;

    public DiagnosticWorkflow(Submission submission, SubmissionRecorder submissionRecorder) {
        this.submission = submission;
        this.submissionRecorder = submissionRecorder;
    }

    @Override
    public void persist(SubmissionRequest request, Diagnostic diagnostic, String identifier) {
        submission.record(request);
        submissionRecorder.store(diagnostic);
        submissionRecorder.record(request, identifier);
    }
}