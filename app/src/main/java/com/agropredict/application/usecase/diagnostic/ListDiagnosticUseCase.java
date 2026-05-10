package com.agropredict.application.usecase.diagnostic;

import com.agropredict.application.repository.IDiagnosticRepository;
import com.agropredict.domain.diagnostic.Diagnostic;
import java.util.List;
import java.util.Objects;

public final class ListDiagnosticUseCase {
    private final IDiagnosticRepository diagnosticRepository;

    public ListDiagnosticUseCase(IDiagnosticRepository diagnosticRepository) {
        this.diagnosticRepository = Objects.requireNonNull(diagnosticRepository, "list diagnostic use case requires a diagnostic repository");
    }

    public List<Diagnostic> list(String userIdentifier) {
        return diagnosticRepository.list(userIdentifier);
    }

    public List<Diagnostic> filter(String userIdentifier, String cropIdentifier) {
        return diagnosticRepository.filter(userIdentifier, cropIdentifier);
    }
}