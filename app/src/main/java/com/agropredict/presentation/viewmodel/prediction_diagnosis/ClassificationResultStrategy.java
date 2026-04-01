package com.agropredict.presentation.viewmodel.prediction_diagnosis;

import com.agropredict.application.visitor.IClassificationResultVisitor;
import java.util.Locale;

public final class ClassificationResultStrategy implements IClassificationResultVisitor {
    private static final double PERCENTAGE_MULTIPLIER = 100;
    private final IPredictionView view;

    public ClassificationResultStrategy(IPredictionView view) {
        this.view = view;
    }

    @Override
    public void visitPrediction(String predictedCrop, double confidence) {
        view.idle();
        String confidenceText = String.format(Locale.getDefault(),
                "%.0f%%", confidence * PERCENTAGE_MULTIPLIER);
        view.classify(predictedCrop, confidenceText);
    }

    @Override
    public void reject(String errorMessage) {
        view.idle();
        view.notify(errorMessage);
    }
}