package com.agropredict.presentation.viewmodel.prediction;

import com.agropredict.application.consumer.IOperationResultConsumer;

public final class DiagnosticResultStrategy implements IOperationResultConsumer {

    private final IPredictionView view;

    public DiagnosticResultStrategy(IPredictionView view) {
        this.view = view;
    }

    @Override
    public void visit(boolean completed, String resultIdentifier) {
        if (completed) {
            view.navigateToResult(resultIdentifier);
        } else {
            view.notify("Error al generar el diagnostico");
        }
    }
}
