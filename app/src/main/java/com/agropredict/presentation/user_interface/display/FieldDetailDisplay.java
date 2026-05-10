package com.agropredict.presentation.user_interface.display;

import android.app.Activity;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.TextView;
import com.agropredict.R;
import com.agropredict.domain.photograph.Photograph;
import com.agropredict.domain.photograph.IPhotographConsumer;
import java.io.File;

public final class FieldDetailDisplay extends DiagnosticDisplay implements IPhotographConsumer {
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
    public void review(String value) {
        severityLabel.setText(value);
    }

    @Override
    public void summarize(String text) {
        summaryLabel.setText(text);
    }

    @Override
    public void recommend(String text) {
        recommendationsLabel.setText(text);
    }

    public void display(Photograph photograph) {
        photograph.expose(this);
    }

    @Override
    public void expose(String identifier, String filePath) {
        if (filePath == null || filePath.isEmpty()) return;
        File file = new File(filePath);
        if (!file.exists()) return;
        photoView.setImageURI(Uri.fromFile(file));
    }
}