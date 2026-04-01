package com.agropredict.presentation.user_interface.component;

import android.app.Activity;
import android.widget.TextView;
import com.agropredict.R;

public final class PredictionResult extends DiagnosticDisplay {
    private final TextView severityLabel;
    private final TextView summaryLabel;
    private final TextView recommendationsLabel;

    public PredictionResult(Activity activity) {
        super(new PredictionDisplay(activity.findViewById(R.id.tvCropName),
                        activity.findViewById(R.id.tvConfidence)));
        severityLabel = activity.findViewById(R.id.tvSeverity);
        summaryLabel = activity.findViewById(R.id.tvSummary);
        recommendationsLabel = activity.findViewById(R.id.tvRecommendations);
    }

    @Override
    public void visitAssessment(String severity, String shortSummary) {
        severityLabel.setText(severity);
        summaryLabel.setText(shortSummary);
    }

    @Override
    public void visitRecommendation(String recommendationText) {
        recommendationsLabel.setText(recommendationText);
    }
}
