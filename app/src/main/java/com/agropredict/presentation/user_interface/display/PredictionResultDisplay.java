package com.agropredict.presentation.user_interface.display;

import android.app.Activity;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.agropredict.R;
import com.agropredict.domain.entity.Photograph;
import com.agropredict.domain.visitor.photograph.IPhotographVisitor;
import java.io.File;

public final class PredictionResultDisplay extends DiagnosticDisplay implements IPhotographVisitor {
    private final TextView severityLabel;
    private final TextView summaryLabel;
    private final TextView recommendationsLabel;
    private final ImageView photoView;

    public PredictionResultDisplay(Activity activity) {
        super(new PredictionDisplay(activity.findViewById(R.id.tvCropName),
                        activity.findViewById(R.id.tvConfidence)));
        severityLabel = activity.findViewById(R.id.tvSeverity);
        summaryLabel = activity.findViewById(R.id.tvSummary);
        recommendationsLabel = activity.findViewById(R.id.tvRecommendations);
        photoView = activity.findViewById(R.id.ivCropPhoto);
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

    public void display(Photograph photograph) {
        photograph.accept(this);
    }

    @Override
    public void visitPhotograph(String identifier, String filePath) {
        if (filePath == null || filePath.isEmpty()) return;
        File file = new File(filePath);
        if (!file.exists()) return;
        photoView.setImageURI(Uri.fromFile(file));
        photoView.setVisibility(View.VISIBLE);
    }
}