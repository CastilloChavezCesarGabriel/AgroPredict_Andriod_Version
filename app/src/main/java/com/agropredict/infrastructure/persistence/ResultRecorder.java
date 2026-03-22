package com.agropredict.infrastructure.persistence;

import com.agropredict.application.repository.IDiagnosticRepository;
import com.agropredict.application.repository.IQuestionnaireRepository;
import com.agropredict.application.request.SubmissionRequest;
import com.agropredict.domain.entity.Diagnostic;

public final class ResultRecorder {
    private final IDiagnosticRepository diagnosticRepository;
    private final IQuestionnaireRepository questionnaireRepository;

    public ResultRecorder(IDiagnosticRepository diagnosticRepository, IQuestionnaireRepository questionnaireRepository) {
        this.diagnosticRepository = diagnosticRepository;
        this.questionnaireRepository = questionnaireRepository;
    }

    public String record(SubmissionRequest request, Diagnostic diagnostic) {
        diagnosticRepository.store(diagnostic);
        String identifier = identify(diagnostic);
        request.record(questionnaireRepository, identifier);
        return identifier;
    }

    private String identify(Diagnostic diagnostic) {
        String[] holder = new String[1];
        diagnostic.accept((identifier, data) -> holder[0] = identifier);
        return holder[0];
    }
}