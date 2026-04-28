package com.agropredict.presentation.user_interface.form;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.net.Uri;
import com.agropredict.R;
import com.agropredict.application.request.diagnostic_submission.SubmissionRequest;
import com.agropredict.application.request.diagnostic_submission.Classification;
import com.agropredict.application.request.diagnostic_submission.PhotographInput;
import com.agropredict.presentation.user_interface.catalog_input.SoilTypeCatalog;
import com.agropredict.presentation.user_interface.catalog_input.StageCatalog;

public final class PredictionForm {
    private final ImageView cropImagePreview;
    private final TextView plantingDateLabel;
    private final TextView classificationLabel;
    private final ProgressBar progressIndicator;
    private final QuestionnaireForm questionnaire;

    public PredictionForm(Activity activity) {
        this.cropImagePreview = activity.findViewById(R.id.ivCropPhoto);
        this.plantingDateLabel = activity.findViewById(R.id.etPlantingDate);
        this.classificationLabel = activity.findViewById(R.id.etCropType);
        this.progressIndicator = activity.findViewById(R.id.progressLoading);
        this.questionnaire = new QuestionnaireForm(activity);
    }

    public void load() {
        progressIndicator.setVisibility(View.VISIBLE);
    }

    public void rest() {
        progressIndicator.setVisibility(View.GONE);
    }

    public void classify(String cropName, double confidence) {
        String formatted = String.format(java.util.Locale.getDefault(),
                "%.0f%%", confidence * 100);
        String text = classificationLabel.getContext().getString(
                R.string.classification_result, cropName, formatted);
        classificationLabel.setText(text);
        classificationLabel.setVisibility(View.VISIBLE);
    }

    public void stamp(String date) {
        plantingDateLabel.setText(date);
    }

    public void preview(Uri imageUri) {
        cropImagePreview.setImageURI(imageUri);
    }

    public void populate(SoilTypeCatalog soilTypeOption) {
        questionnaire.populate(soilTypeOption);
    }

    public void populate(StageCatalog stageOption) {
        questionnaire.populate(stageOption);
    }

    public SubmissionRequest collect(Classification prediction, PhotographInput image) {
        return questionnaire.assemble(prediction, image);
    }
}