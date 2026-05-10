package com.agropredict.application.diagnostic_submission;

import com.agropredict.application.repository.IDiagnosticRepository;
import com.agropredict.application.repository.IQuestionnaireRepository;
import com.agropredict.application.request.diagnostic_submission.SubmissionRequest;
import com.agropredict.domain.diagnostic.Diagnostic;
import java.util.Objects;

public final class DiagnosticArchive {
    private final IDiagnosticRepository diagnosticRepository;
    private final IQuestionnaireRepository questionnaireRepository;

    public DiagnosticArchive(IDiagnosticRepository diagnosticRepository, IQuestionnaireRepository questionnaireRepository) {
        this.diagnosticRepository = Objects.requireNonNull(diagnosticRepository, "diagnostic archive requires a diagnostic repository");
        this.questionnaireRepository = Objects.requireNonNull(questionnaireRepository, "diagnostic archive requires a questionnaire repository");
    }

    public void preserve(Diagnostic diagnostic, SubmissionRequest request) {
        store(diagnostic);
        record(diagnostic, request);
    }

    private void store(Diagnostic diagnostic) {
        diagnosticRepository.store(diagnostic);
    }

    private void record(Diagnostic diagnostic, SubmissionRequest request) {
        diagnostic.identify(identifier -> request.record(questionnaireRepository, identifier));
    }
}