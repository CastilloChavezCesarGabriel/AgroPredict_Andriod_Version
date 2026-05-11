package com.agropredict.application.diagnostic_history;

import com.agropredict.application.repository.IDiagnosticRepository;
import com.agropredict.domain.diagnostic.Diagnostic;
import java.util.Objects;

public final class FindDiagnosticUseCase {
    private final IDiagnosticRepository diagnosticRepository;

    public FindDiagnosticUseCase(IDiagnosticRepository diagnosticRepository) {
        this.diagnosticRepository = Objects.requireNonNull(diagnosticRepository, "find diagnostic use case requires a diagnostic repository");
    }

    public Diagnostic find(String identifier) {
        return diagnosticRepository.find(identifier);
    }
}