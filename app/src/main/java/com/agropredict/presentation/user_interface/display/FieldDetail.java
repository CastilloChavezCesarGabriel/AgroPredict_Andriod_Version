package com.agropredict.presentation.user_interface.display;

import android.app.Activity;
import android.widget.TextView;
import com.agropredict.R;

public final class FieldDetail extends DiagnosticDisplay {
    private final TextView severityLabel;
    private final TextView recommendationsLabel;

    public FieldDetail(Activity activity) {
        super(new PredictionDisplay(activity.findViewById(R.id.tvCropType),
                        activity.findViewById(R.id.tvConfidence)));
        severityLabel = activity.findViewById(R.id.tvSeverity);
        recommendationsLabel = activity.findViewById(R.id.tvRecommendations);
    }

    @Override
    public void visitAssessment(String severity, String shortSummary) {
        severityLabel.setText(severity);
    }

    @Override
    public void visitRecommendation(String recommendationText) {
        recommendationsLabel.setText(recommendationText);
    }
}