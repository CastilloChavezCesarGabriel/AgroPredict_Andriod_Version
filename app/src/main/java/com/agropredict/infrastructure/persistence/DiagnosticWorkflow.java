package com.agropredict.infrastructure.persistence;

import com.agropredict.application.repository.IDiagnosticWorkflow;
import com.agropredict.application.request.SubmissionRequest;
import com.agropredict.domain.entity.Diagnostic;

public final class DiagnosticWorkflow implements IDiagnosticWorkflow {
    private final SubmissionRecorder submissionRecorder;
    private final ResultRecorder resultRecorder;

    public DiagnosticWorkflow(SubmissionRecorder submissionRecorder, ResultRecorder resultRecorder) {
        this.submissionRecorder = submissionRecorder;
        this.resultRecorder = resultRecorder;
    }

    @Override
    public String persist(SubmissionRequest request, Diagnostic diagnostic) {
        submissionRecorder.record(request);
        return resultRecorder.record(request, diagnostic);
    }
}
