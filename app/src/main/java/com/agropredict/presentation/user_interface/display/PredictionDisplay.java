package com.agropredict.presentation.user_interface.display;

import android.widget.TextView;
import java.util.Locale;

public final class PredictionDisplay {
    private final TextView cropNameLabel;
    private final TextView confidenceLabel;

    public PredictionDisplay(TextView cropNameLabel, TextView confidenceLabel) {
        this.cropNameLabel = cropNameLabel;
        this.confidenceLabel = confidenceLabel;
    }

    public void classify(String predictedCrop, double confidence) {
        cropNameLabel.setText(predictedCrop);
        confidenceLabel.setText(String.format(Locale.getDefault(), "%.0f%%", confidence * 100));
    }
}