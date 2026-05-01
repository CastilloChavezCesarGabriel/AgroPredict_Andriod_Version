package com.agropredict.presentation.viewmodel.prediction_diagnosis;

import com.agropredict.application.visitor.IOperationResultVisitor;

public final class DiagnosticResultPresenter implements IOperationResultVisitor {
    private final IPredictionView view;

    public DiagnosticResultPresenter(IPredictionView view) {
        this.view = view;
    }

    @Override
    public void visit(boolean completed, String resultIdentifier) {
        if (completed) {
            view.onDiagnosed(resultIdentifier);
        } else {
            view.onFailed();
        }
    }
}