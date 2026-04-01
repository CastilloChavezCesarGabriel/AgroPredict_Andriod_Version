package com.agropredict.application.facade;

import com.agropredict.application.operation_result.OperationResult;
import com.agropredict.application.usecase.diagnostic.DeleteDiagnosticUseCase;
import com.agropredict.application.usecase.diagnostic.ListDiagnosticUseCase;
import com.agropredict.domain.entity.Diagnostic;
import java.util.List;

public final class DiagnosticHistoryFacade {
    private final ListDiagnosticUseCase listUseCase;
    private final DeleteDiagnosticUseCase deleteUseCase;

    public DiagnosticHistoryFacade(ListDiagnosticUseCase listUseCase, DeleteDiagnosticUseCase deleteUseCase) {
        this.listUseCase = listUseCase;
        this.deleteUseCase = deleteUseCase;
    }

    public List<Diagnostic> list(String userIdentifier) {
        return listUseCase.list(userIdentifier);
    }

    public List<Diagnostic> filter(String userIdentifier, String cropIdentifier) {
        return listUseCase.filter(userIdentifier, cropIdentifier);
    }

    public OperationResult delete(String diagnosticIdentifier) {
        return deleteUseCase.delete(diagnosticIdentifier);
    }
}