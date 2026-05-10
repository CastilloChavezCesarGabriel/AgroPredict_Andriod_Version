package com.agropredict.presentation.viewmodel.prediction_diagnosis;

import com.agropredict.application.visitor.IOperationResult;

public final class DiagnosticResultPresenter implements IOperationResult {
    private final IPredictionView view;

    public DiagnosticResultPresenter(IPredictionView view) {
        this.view = view;
    }

    @Override
    public void onSucceed(String value) {
        view.onDiagnosed(value);
    }

    @Override
    public void onFail() {
        view.onFailed();
    }

    @Override
    public void onReject(String reason) {
        view.onFailed();
    }
}