package com.agropredict.presentation.viewmodel.diagnostic_history;

import com.agropredict.application.usecase.DeleteUseCase;
import com.agropredict.application.usecase.diagnostic.ListDiagnosticUseCase;
import com.agropredict.application.visitor.IOperationResultVisitor;

public final class HistoryViewModel implements IOperationResultVisitor {
    private final ListDiagnosticUseCase listUseCase;
    private final DeleteUseCase deleteUseCase;
    private final IHistoryView view;
    private String pendingUserIdentifier;

    public HistoryViewModel(ListDiagnosticUseCase listUseCase, DeleteUseCase deleteUseCase, IHistoryView view) {
        this.listUseCase = listUseCase;
        this.deleteUseCase = deleteUseCase;
        this.view = view;
    }

    public void load(String userIdentifier) {
        this.pendingUserIdentifier = userIdentifier;
        view.display(listUseCase.list(userIdentifier));
    }

    public void filter(String cropIdentifier) {
        view.display(listUseCase.filter(pendingUserIdentifier, cropIdentifier));
    }

    public void delete(String diagnosticIdentifier) {
        deleteUseCase.delete(diagnosticIdentifier).accept(this);
    }

    @Override
    public void visit(boolean completed, String resultIdentifier) {
        if (completed) {
            view.notify("Diagnostic deleted successfully");
            load(pendingUserIdentifier);
        } else {
            view.notify("Error deleting the diagnostic");
        }
    }
}