package com.agropredict.presentation.viewmodel.prediction_diagnosis;

import com.agropredict.application.visitor.IClassificationResultVisitor;

public final class ClassificationResultPresenter implements IClassificationResultVisitor {
    private final IPredictionView view;

    public ClassificationResultPresenter(IPredictionView view) {
        this.view = view;
    }

    @Override
    public void visitPrediction(String predictedCrop, double confidence) {
        view.onIdle();
        view.onClassified(predictedCrop, confidence);
    }

    @Override
    public void onReject(String errorMessage) {
        view.onIdle();
        view.notify(errorMessage);
    }
}