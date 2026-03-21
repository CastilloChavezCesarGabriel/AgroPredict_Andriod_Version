package com.agropredict.application;

import com.agropredict.application.result.OperationResult;
import com.agropredict.application.usecase.diagnostic.DeleteDiagnosticUseCase;
import com.agropredict.application.usecase.diagnostic.ListDiagnosticsUseCase;
import com.agropredict.domain.entity.Diagnostic;
import java.util.List;

public final class DiagnosticHistoryFacade {
    private final ListDiagnosticsUseCase listUseCase;
    private final DeleteDiagnosticUseCase deleteUseCase;

    public DiagnosticHistoryFacade(ListDiagnosticsUseCase listUseCase, DeleteDiagnosticUseCase deleteUseCase) {
        this.listUseCase = listUseCase;
        this.deleteUseCase = deleteUseCase;
    }

    public List<Diagnostic> list(String userIdentifier) {
        return listUseCase.list(userIdentifier);
    }

    public OperationResult delete(String diagnosticIdentifier) {
        return deleteUseCase.delete(diagnosticIdentifier);
    }
}