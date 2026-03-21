package com.agropredict.presentation.user_interface.holder;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.agropredict.R;
import com.agropredict.application.request.SubmissionRequest;
import com.agropredict.application.request.data.Classification;
import com.agropredict.application.request.input.Photograph;
import java.util.List;

public final class PredictionViewHolder {
    private final ImageView cropImagePreview;
    private final TextView plantingDateLabel;
    private final TextView classificationLabel;
    private final ProgressBar progressIndicator;
    private final QuestionnaireHolder questionnaire;

    public PredictionViewHolder(Activity activity) {
        this.cropImagePreview = activity.findViewById(R.id.ivCropPhoto);
        this.plantingDateLabel = activity.findViewById(R.id.etPlantingDate);
        this.classificationLabel = activity.findViewById(R.id.etCropType);
        this.progressIndicator = activity.findViewById(R.id.progressLoading);
        this.questionnaire = new QuestionnaireHolder(activity);
    }

    public void load() {
        progressIndicator.setVisibility(View.VISIBLE);
    }

    public void idle() {
        progressIndicator.setVisibility(View.GONE);
    }

    public void classify(String cropName, String confidence) {
        classificationLabel.setText(cropName + " (" + confidence + ")");
        classificationLabel.setVisibility(View.VISIBLE);
    }

    public void date(String date) {
        plantingDateLabel.setText(date);
    }

    public void preview(android.net.Uri imageUri) {
        cropImagePreview.setImageURI(imageUri);
    }

    public void populateSoilTypes(List<String> soilTypes) {
        questionnaire.populateSoilTypes(soilTypes);
    }

    public void populateStages(List<String> stages) {
        questionnaire.populateStages(stages);
    }

    public SubmissionRequest collect(Classification prediction, Photograph image) {
        return questionnaire.assemble(prediction, image);
    }
}
