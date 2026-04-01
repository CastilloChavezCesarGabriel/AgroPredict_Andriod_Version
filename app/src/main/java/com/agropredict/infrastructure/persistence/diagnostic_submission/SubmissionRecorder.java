package com.agropredict.infrastructure.persistence.diagnostic_submission;

import com.agropredict.application.repository.IDiagnosticRepository;
import com.agropredict.application.repository.IQuestionnaireRepository;
import com.agropredict.application.request.diagnostic_submission.SubmissionRequest;
import com.agropredict.domain.entity.Diagnostic;

public final class SubmissionRecorder {
    private final IDiagnosticRepository diagnosticRepository;
    private final IQuestionnaireRepository questionnaireRepository;

    public SubmissionRecorder(IDiagnosticRepository diagnosticRepository, IQuestionnaireRepository questionnaireRepository) {
        this.diagnosticRepository = diagnosticRepository;
        this.questionnaireRepository = questionnaireRepository;
    }

    public void store(Diagnostic diagnostic) {
        diagnosticRepository.store(diagnostic);
    }

    public void record(SubmissionRequest request, String identifier) {
        request.record(questionnaireRepository, identifier);
    }
}