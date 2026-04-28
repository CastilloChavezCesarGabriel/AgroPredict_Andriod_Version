package com.agropredict.application.diagnostic_submission;

import com.agropredict.application.request.diagnostic_submission.SubmissionRequest;
import com.agropredict.domain.entity.Diagnostic;

public final class DiagnosticWorkflow implements IDiagnosticWorkflow {
    private final FieldRecorder fieldRecorder;
    private final DiagnosticArchive diagnosticArchive;
    private final AnswerArchive answerArchive;

    public DiagnosticWorkflow(FieldRecorder fieldRecorder, DiagnosticArchive diagnosticArchive, AnswerArchive answerArchive) {
        this.fieldRecorder = fieldRecorder;
        this.diagnosticArchive = diagnosticArchive;
        this.answerArchive = answerArchive;
    }

    @Override
    public void persist(SubmissionRequest request, Diagnostic diagnostic, String identifier) {
        fieldRecorder.record(request);
        diagnosticArchive.archive(diagnostic);
        answerArchive.archive(request, identifier);
    }
}