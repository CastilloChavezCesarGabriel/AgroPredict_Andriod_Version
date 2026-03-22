package com.agropredict.presentation.user_interface.component;

import android.app.Activity;
import android.widget.TextView;
import com.agropredict.R;
import com.agropredict.application.repository.ICropImageRepository;

public final class PredictionResult extends DiagnosticDisplay {
    private final TextView severityLabel;
    private final TextView summaryLabel;
    private final TextView recommendationsLabel;

    public PredictionResult(Activity activity, ICropImageRepository imageRepository) {
        super(new PredictionDisplay(activity.findViewById(R.id.tvCropName),
                        activity.findViewById(R.id.tvConfidence)),
                new PhotoDisplay(activity.findViewById(R.id.ivCropPhoto), imageRepository));
        severityLabel = activity.findViewById(R.id.tvSeverity);
        summaryLabel = activity.findViewById(R.id.tvSummary);
        recommendationsLabel = activity.findViewById(R.id.tvRecommendations);
    }

    @Override
    public void visitSummary(String severity, String shortSummary) {
        severityLabel.setText(severity);
        summaryLabel.setText(shortSummary);
    }

    @Override
    public void visitOwnership(String userIdentifier, String recommendationText) {
        recommendationsLabel.setText(recommendationText);
    }
}