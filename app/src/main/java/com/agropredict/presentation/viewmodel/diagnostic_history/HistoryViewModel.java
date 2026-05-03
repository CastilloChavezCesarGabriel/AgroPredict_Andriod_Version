package com.agropredict.presentation.viewmodel.diagnostic_history;

import com.agropredict.application.visitor.IOperationResultVisitor;

public final class HistoryViewModel implements IOperationResultVisitor {
    private final HistoryWorkflow workflow;
    private final IHistoryView view;
    private String pendingUserIdentifier;

    public HistoryViewModel(HistoryWorkflow workflow, IHistoryView view) {
        this.workflow = workflow;
        this.view = view;
    }

    public void load(String userIdentifier) {
        this.pendingUserIdentifier = userIdentifier;
        view.display(workflow.list(userIdentifier));
    }

    public void filter(String cropIdentifier) {
        view.display(workflow.filter(pendingUserIdentifier, cropIdentifier));
    }

    public void delete(String diagnosticIdentifier) {
        workflow.delete(diagnosticIdentifier).accept(this);
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
