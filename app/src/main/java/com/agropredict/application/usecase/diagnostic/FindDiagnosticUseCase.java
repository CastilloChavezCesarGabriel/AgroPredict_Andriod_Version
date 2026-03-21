package com.agropredict.application.usecase.diagnostic;

import com.agropredict.application.repository.IDiagnosticRepository;
import com.agropredict.domain.entity.Diagnostic;

public final class FindDiagnosticUseCase {
    private final IDiagnosticRepository diagnosticRepository;

    public FindDiagnosticUseCase(IDiagnosticRepository diagnosticRepository) {
        this.diagnosticRepository = diagnosticRepository;
    }

    public Diagnostic find(String diagnosticIdentifier) {
        return diagnosticRepository.find(diagnosticIdentifier);
    }
}