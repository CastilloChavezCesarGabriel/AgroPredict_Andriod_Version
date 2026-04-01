package com.agropredict.application.usecase.diagnostic;

import com.agropredict.application.repository.IDiagnosticRepository;
import com.agropredict.domain.entity.Diagnostic;
import java.util.List;

public final class ListDiagnosticUseCase {
    private final IDiagnosticRepository diagnosticRepository;

    public ListDiagnosticUseCase(IDiagnosticRepository diagnosticRepository) {
        this.diagnosticRepository = diagnosticRepository;
    }

    public List<Diagnostic> list(String userIdentifier) {
        return diagnosticRepository.list(userIdentifier);
    }

    public List<Diagnostic> filter(String userIdentifier, String cropIdentifier) {
        return diagnosticRepository.filter(userIdentifier, cropIdentifier);
    }
}