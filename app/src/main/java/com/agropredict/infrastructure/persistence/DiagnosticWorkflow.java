package com.agropredict.infrastructure.persistence;

import com.agropredict.application.repository.IDiagnosticWorkflow;
import com.agropredict.application.request.SubmissionRequest;
import com.agropredict.domain.entity.Diagnostic;

public final class DiagnosticWorkflow implements IDiagnosticWorkflow {
    private final Submission submission;
    private final ResultRecorder resultRecorder;

    public DiagnosticWorkflow(Submission submission, ResultRecorder resultRecorder) {
        this.submission = submission;
        this.resultRecorder = resultRecorder;
    }

    @Override
    public String persist(SubmissionRequest request, Diagnostic diagnostic) {
        submission.record(request);
        return resultRecorder.record(request, diagnostic);
    }
}