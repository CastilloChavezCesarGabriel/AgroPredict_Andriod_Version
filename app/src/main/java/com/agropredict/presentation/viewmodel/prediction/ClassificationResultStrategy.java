package com.agropredict.presentation.viewmodel.prediction;

import com.agropredict.application.visitor.IClassificationResultVisitor;
import java.util.Locale;

public final class ClassificationResultStrategy implements IClassificationResultVisitor {
    private static final double MINIMUM_CONFIDENCE_THRESHOLD = 0.6;
    private static final double PERCENTAGE_MULTIPLIER = 100;
    private final IPredictionView view;

    public ClassificationResultStrategy(IPredictionView view) {
        this.view = view;
    }

    @Override
    public void visit(String predictedCrop, double confidence) {
        view.idle();
        if (confidence >= MINIMUM_CONFIDENCE_THRESHOLD) {
            String confidenceText = String.format(Locale.getDefault(),
                    "%.0f%%", confidence * PERCENTAGE_MULTIPLIER);
            view.classify(predictedCrop, confidenceText);
        } else {
            view.notify("Could not identify the crop with certainty");
        }
    }

    @Override
    public void reject(String errorMessage) {
        view.idle();
        view.notify(errorMessage);
    }
}