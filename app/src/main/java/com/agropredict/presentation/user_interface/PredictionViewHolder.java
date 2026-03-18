package com.agropredict.presentation.user_interface;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import com.agropredict.R;

public final class PredictionViewHolder {
    private final ImageView cropImagePreview;
    private final TextView plantingDateLabel;
    private final TextView classificationLabel;
    private final Spinner soilTypeSpinner;
    private final Spinner stageSpinner;
    private final ProgressBar progressIndicator;

    public PredictionViewHolder(Activity activity) {
        this.cropImagePreview = activity.findViewById(R.id.ivCropPhoto);
        this.plantingDateLabel = activity.findViewById(R.id.etPlantingDate);
        this.classificationLabel = activity.findViewById(R.id.etCropType);
        this.soilTypeSpinner = activity.findViewById(R.id.spnSoilType);
        this.stageSpinner = activity.findViewById(R.id.spnStage);
        this.progressIndicator = activity.findViewById(R.id.progressLoading);
    }

    public void showLoading() {
        progressIndicator.setVisibility(View.VISIBLE);
    }

    public void hideLoading() {
        progressIndicator.setVisibility(View.GONE);
    }

    public void displayClassification(String cropName, String confidence) {
        classificationLabel.setText(cropName + " (" + confidence + ")");
        classificationLabel.setVisibility(View.VISIBLE);
    }

    public void displayDate(String date) {
        plantingDateLabel.setText(date);
    }

    public void displayImage(android.net.Uri imageUri) {
        cropImagePreview.setImageURI(imageUri);
    }

    public void populateSoilTypes(java.util.List<String> soilTypes) {
        SpinnerPopulator.populate(soilTypeSpinner, soilTypes);
    }

    public void populateStages(java.util.List<String> stages) {
        SpinnerPopulator.populate(stageSpinner, stages);
    }

    public String selectedSoilType() {
        return soilTypeSpinner.getSelectedItem().toString();
    }

    public String selectedStage() {
        return stageSpinner.getSelectedItem().toString();
    }
}
