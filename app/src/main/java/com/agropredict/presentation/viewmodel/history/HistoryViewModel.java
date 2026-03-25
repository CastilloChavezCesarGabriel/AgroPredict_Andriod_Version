package com.agropredict.presentation.viewmodel.history;

import com.agropredict.application.DiagnosticHistoryFacade;
import com.agropredict.application.visitor.IOperationResultVisitor;
import com.agropredict.application.result.OperationResult;
import com.agropredict.domain.entity.Diagnostic;
import java.util.List;

public final class HistoryViewModel implements IOperationResultVisitor {
    private final DiagnosticHistoryFacade facade;
    private final IHistoryView view;
    private String pendingUserIdentifier;

    public HistoryViewModel(DiagnosticHistoryFacade facade, IHistoryView view) {
        this.facade = facade;
        this.view = view;
    }

    public void load(String userIdentifier) {
        this.pendingUserIdentifier = userIdentifier;
        List<Diagnostic> diagnostics = facade.list(userIdentifier);
        if (diagnostics.isEmpty()) {
            view.empty();
        } else {
            view.display(diagnostics);
        }
    }

    public void delete(String diagnosticIdentifier) {
        OperationResult result = facade.delete(diagnosticIdentifier);
        result.accept(this);
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