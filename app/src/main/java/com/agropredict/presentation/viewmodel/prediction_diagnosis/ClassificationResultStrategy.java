package com.agropredict.presentation.viewmodel.prediction_diagnosis;

import com.agropredict.application.visitor.IClassificationResultVisitor;

public final class ClassificationResultStrategy implements IClassificationResultVisitor {
    private final IPredictionView view;

    public ClassificationResultStrategy(IPredictionView view) {
        this.view = view;
    }

    @Override
    public void visitPrediction(String predictedCrop, double confidence) {
        view.onIdle();
        view.onClassified(predictedCrop, confidence);
    }

    @Override
    public void reject(String errorMessage) {
        view.onIdle();
        view.notify(errorMessage);
    }
}