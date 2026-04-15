package com.agropredict.presentation.viewmodel.diagnostic_history;

import com.agropredict.application.usecase.DeleteUseCase;
import com.agropredict.application.usecase.diagnostic.ListDiagnosticUseCase;
import com.agropredict.application.visitor.IOperationResultVisitor;
import com.agropredict.domain.entity.Diagnostic;
import java.util.List;

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
        render(listUseCase.list(userIdentifier));
    }

    public void filter(String cropIdentifier) {
        render(listUseCase.filter(pendingUserIdentifier, cropIdentifier));
    }

    public void delete(String diagnosticIdentifier) {
        deleteUseCase.delete(diagnosticIdentifier).accept(this);
    }

    private void render(List<Diagnostic> diagnostics) {
        if (diagnostics.isEmpty()) view.empty();
        else view.display(diagnostics);
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