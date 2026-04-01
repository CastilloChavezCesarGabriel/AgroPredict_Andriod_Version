package com.agropredict.application.usecase.diagnostic;

import com.agropredict.application.repository.IDiagnosticRepository;
import com.agropredict.application.operation_result.OperationResult;

public final class DeleteDiagnosticUseCase {
    private final IDiagnosticRepository diagnosticRepository;

    public DeleteDiagnosticUseCase(IDiagnosticRepository diagnosticRepository) {
        this.diagnosticRepository = diagnosticRepository;
    }

    public OperationResult delete(String diagnosticIdentifier) {
        try {
            diagnosticRepository.delete(diagnosticIdentifier);
            return OperationResult.succeed(null);
        } catch (RuntimeException exception) {
            return OperationResult.fail();
        }
    }
}