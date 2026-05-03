package com.agropredict.presentation.user_interface.display;

import android.app.Activity;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.TextView;
import com.agropredict.R;
import com.agropredict.domain.entity.Photograph;
import com.agropredict.domain.visitor.photograph.IPhotographVisitor;
import java.io.File;

public final class FieldDetailDisplay extends DiagnosticDisplay implements IPhotographVisitor {
    private final TextView severityLabel;
    private final TextView summaryLabel;
    private final TextView recommendationsLabel;
    private final ImageView photoView;

    public FieldDetailDisplay(Activity activity) {
        super(new PredictionDisplay(activity.findViewById(R.id.tvCropType),
                        activity.findViewById(R.id.tvConfidence)));
        severityLabel = activity.findViewById(R.id.tvSeverity);
        summaryLabel = activity.findViewById(R.id.tvSummary);
        recommendationsLabel = activity.findViewById(R.id.tvRecommendations);
        photoView = activity.findViewById(R.id.ivCropPhoto);
    }

    @Override
    public void visitSeverity(String value) {
        severityLabel.setText(value);
    }

    @Override
    public void visitSummary(String text) {
        summaryLabel.setText(text);
    }

    @Override
    public void visitRecommendation(String text) {
        recommendationsLabel.setText(text);
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
    }
}