package com.agropredict.infrastructure.persistence;

import com.agropredict.application.repository.IDiagnosticWorkflow;
import com.agropredict.application.request.SubmissionRequest;
import com.agropredict.domain.entity.Diagnostic;

public final class DiagnosticWorkflow implements IDiagnosticWorkflow {
    private final Submission submission;
    private final SubmissionRecorder submissionRecorder;

    public DiagnosticWorkflow(Submission submission, SubmissionRecorder submissionRecorder) {
        this.submission = submission;
        this.submissionRecorder = submissionRecorder;
    }

    @Override
    public String persist(SubmissionRequest request, Diagnostic diagnostic) {
        submission.record(request);
        return submissionRecorder.record(request, diagnostic);
    }
}