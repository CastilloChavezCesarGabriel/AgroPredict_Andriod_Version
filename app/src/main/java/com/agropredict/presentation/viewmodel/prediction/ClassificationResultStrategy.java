package com.agropredict.presentation.viewmodel.prediction;

import com.agropredict.application.consumer.IClassificationResultConsumer;

public final class ClassificationResultStrategy implements IClassificationResultConsumer {
    private static final double MINIMUM_CONFIDENCE_THRESHOLD = 0.6;
    private static final double PERCENTAGE_MULTIPLIER = 100;
    private final IPredictionView view;

    public ClassificationResultStrategy(IPredictionView view) {
        this.view = view;
    }

    @Override
    public void visit(String predictedCrop, double confidence) {
        if (confidence >= MINIMUM_CONFIDENCE_THRESHOLD) {
            String confidenceText = String.format("%.0f%%", confidence * PERCENTAGE_MULTIPLIER);
            view.displayClassification(predictedCrop, confidenceText);
        } else {
            view.notify("No se pudo identificar el cultivo con certeza");
        }
    }
}
