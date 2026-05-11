package com.agropredict.presentation.viewmodel.diagnostic_history;

import com.agropredict.application.operation_result.IOperationResult;

public final class HistoryViewModel implements IOperationResult {
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
    public void onSucceed(String value) {
        view.notify("Diagnostic deleted successfully");
        load(pendingUserIdentifier);
    }

    @Override
    public void onFail() {
        view.notify("Error deleting the diagnostic");
    }

    @Override
    public void onReject(String reason) {
        view.notify(reason);
    }
}
