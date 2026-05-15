package com.agropredict.application.diagnostic_history;

import com.agropredict.application.repository.IDiagnosticRepository;
import com.agropredict.domain.diagnostic.Diagnostic;
import java.util.Objects;

public final class ResolveDiagnosticUseCase {
    private final IDiagnosticRepository diagnosticRepository;

    public ResolveDiagnosticUseCase(IDiagnosticRepository diagnosticRepository) {
        this.diagnosticRepository = Objects.requireNonNull(diagnosticRepository,
                "resolve diagnostic use case requires a diagnostic repository");
    }

    public Diagnostic resolve(String userIdentifier, String cropIdentifier) {
        return diagnosticRepository.resolve(userIdentifier, cropIdentifier);
    }
}