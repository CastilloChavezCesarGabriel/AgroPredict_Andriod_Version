package com.agropredict.infrastructure.persistence.diagnostic_submission;

import com.agropredict.application.repository.IDiagnosticRepository;
import com.agropredict.domain.entity.Diagnostic;

public final class DiagnosticArchive {
    private final IDiagnosticRepository diagnosticRepository;

    public DiagnosticArchive(IDiagnosticRepository diagnosticRepository) {
        this.diagnosticRepository = diagnosticRepository;
    }

    public void archive(Diagnostic diagnostic) {
        diagnosticRepository.store(diagnostic);
    }
}