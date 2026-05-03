package com.agropredict.application.diagnostic_submission;

import com.agropredict.application.request.diagnostic_submission.SubmissionRequest;
import com.agropredict.domain.entity.Diagnostic;

public final class Archival {
    private final DiagnosticArchive diagnosticArchive;
    private final AnswerArchive answerArchive;

    public Archival(DiagnosticArchive diagnosticArchive, AnswerArchive answerArchive) {
        this.diagnosticArchive = diagnosticArchive;
        this.answerArchive = answerArchive;
    }

    public void archive(Diagnostic diagnostic) {
        diagnosticArchive.archive(diagnostic);
    }

    public void record(SubmissionRequest request, String identifier) {
        answerArchive.archive(request, identifier);
    }
}
