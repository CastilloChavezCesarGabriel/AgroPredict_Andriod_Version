package com.agropredict.application.usecase.diagnostic;

import com.agropredict.application.repository.IDiagnosticRepository;
import com.agropredict.domain.entity.Diagnostic;

public final class LoadDiagnosticDetailUseCase {
    private final IDiagnosticRepository diagnosticRepository;

    public LoadDiagnosticDetailUseCase(IDiagnosticRepository diagnosticRepository) {
        this.diagnosticRepository = diagnosticRepository;
    }

    public Diagnostic load(String diagnosticIdentifier) {
        return diagnosticRepository.load(diagnosticIdentifier);
    }
}