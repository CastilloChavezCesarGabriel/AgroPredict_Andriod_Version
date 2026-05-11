package com.agropredict.presentation.viewmodel.diagnostic_history;

import com.agropredict.application.operation_result.IUseCaseResult;
import com.agropredict.application.DeleteUseCase;
import com.agropredict.application.diagnostic_history.ListDiagnosticUseCase;
import com.agropredict.domain.diagnostic.Diagnostic;
import java.util.List;

public final class HistoryWorkflow {
    private final ListDiagnosticUseCase listUseCase;
    private final DeleteUseCase deleteUseCase;

    public HistoryWorkflow(ListDiagnosticUseCase listUseCase, DeleteUseCase deleteUseCase) {
        this.listUseCase = listUseCase;
        this.deleteUseCase = deleteUseCase;
    }

    public List<Diagnostic> list(String userIdentifier) {
        return listUseCase.list(userIdentifier);
    }

    public List<Diagnostic> filter(String userIdentifier, String cropIdentifier) {
        return listUseCase.filter(userIdentifier, cropIdentifier);
    }

    public IUseCaseResult delete(String diagnosticIdentifier) {
        return deleteUseCase.delete(diagnosticIdentifier);
    }
}
