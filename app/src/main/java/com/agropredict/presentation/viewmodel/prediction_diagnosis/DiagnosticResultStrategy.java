package com.agropredict.presentation.viewmodel.prediction_diagnosis;

import com.agropredict.application.visitor.IOperationResultVisitor;

public final class DiagnosticResultStrategy implements IOperationResultVisitor {
    private final IPredictionView view;

    public DiagnosticResultStrategy(IPredictionView view) {
        this.view = view;
    }

    @Override
    public void visit(boolean completed, String resultIdentifier) {
        if (completed) {
            view.reveal(resultIdentifier);
        } else {
            view.notify("Error generating the diagnosis");
        }
    }
}