package com.agropredict.infrastructure.persistence.diagnostic_submission;

import com.agropredict.application.repository.IDiagnosticWorkflow;
import com.agropredict.application.request.diagnostic_submission.SubmissionRequest;
import com.agropredict.domain.entity.Diagnostic;

public final class DiagnosticWorkflow implements IDiagnosticWorkflow {
    private final Submission submission;
    private final DiagnosticArchive diagnosticArchive;
    private final AnswerArchive answerArchive;

    public DiagnosticWorkflow(Submission submission,
                              DiagnosticArchive diagnosticArchive,
                              AnswerArchive answerArchive) {
        this.submission = submission;
        this.diagnosticArchive = diagnosticArchive;
        this.answerArchive = answerArchive;
    }

    @Override
    public void persist(SubmissionRequest request, Diagnostic diagnostic, String identifier) {
        submission.record(request);
        diagnosticArchive.archive(diagnostic);
        answerArchive.archive(request, identifier);
    }
}