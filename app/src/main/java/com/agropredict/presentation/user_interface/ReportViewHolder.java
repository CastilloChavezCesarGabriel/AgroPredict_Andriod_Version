package com.agropredict.presentation.user_interface;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
import com.agropredict.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class ReportViewHolder {
    private final Spinner cropSpinner;
    private final RadioGroup formatRadioGroup;
    private final Button shareButton;
    private final ProgressBar progressIndicator;
    private final List<Map<String, String>> cropItems;

    public ReportViewHolder(Activity activity) {
        this.cropSpinner = activity.findViewById(R.id.spnCropSelection);
        this.formatRadioGroup = activity.findViewById(R.id.radioGroupFormat);
        this.shareButton = activity.findViewById(R.id.btnShare);
        this.progressIndicator = activity.findViewById(R.id.progressGeneration);
        this.cropItems = new ArrayList<>();
        shareButton.setVisibility(View.GONE);
    }

    public void showLoading() {
        progressIndicator.setVisibility(View.VISIBLE);
    }

    public void hideLoading() {
        progressIndicator.setVisibility(View.GONE);
    }

    public void populateCrops(List<Map<String, String>> crops) {
        cropItems.clear();
        cropItems.addAll(crops);
        List<String> labels = new ArrayList<>();
        for (Map<String, String> crop : crops) {
            labels.add(crop.get("name"));
        }
        SpinnerPopulator.populate(cropSpinner, labels);
    }

    public void showShareOption(String filePath) {
        shareButton.setVisibility(View.VISIBLE);
    }

    public void attachShareListener(View.OnClickListener listener) {
        shareButton.setOnClickListener(listener);
    }

    public boolean hasCropSelected() {
        return cropSpinner.getSelectedItem() != null;
    }

    public String selectedCropIdentifier() {
        int position = cropSpinner.getSelectedItemPosition();
        return cropItems.get(position).get("identifier");
    }

    public String selectedFormat() {
        if (formatRadioGroup.getCheckedRadioButtonId() == R.id.radioCsv) return "csv";
        return "pdf";
    }
}
