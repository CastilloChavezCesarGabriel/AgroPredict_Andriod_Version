package com.agropredict.presentation.user_interface.form;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import com.agropredict.R;
import com.agropredict.domain.entity.Crop;
import com.agropredict.presentation.user_interface.selector.CropSelection;
import com.agropredict.presentation.viewmodel.report_generation.ReportViewModel;
import com.google.android.material.button.MaterialButtonToggleGroup;
import java.util.List;

public final class ReportForm {

    private final CropSelection cropSelection;
    private final MaterialButtonToggleGroup formatToggleGroup;
    private final Button shareButton;
    private final ProgressBar progressIndicator;

    public ReportForm(Activity activity) {
        this.cropSelection = new CropSelection(activity.findViewById(R.id.spnCropSelection), identifier -> {});
        this.formatToggleGroup = activity.findViewById(R.id.radioGroupFormat);
        this.formatToggleGroup.check(R.id.radioPdf);
        this.shareButton = activity.findViewById(R.id.btnShare);
        this.progressIndicator = activity.findViewById(R.id.progressGeneration);
        shareButton.setVisibility(View.GONE);
    }

    public void load() {
        progressIndicator.setVisibility(View.VISIBLE);
    }

    public void idle() {
        progressIndicator.setVisibility(View.GONE);
    }

    public void populate(List<Crop> crops) {
        cropSelection.populate(crops);
    }

    public void offer() {
        shareButton.setVisibility(View.VISIBLE);
    }

    public void listen(View.OnClickListener listener) {
        shareButton.setOnClickListener(listener);
    }

    public void generate(ReportViewModel viewModel) {
        String cropIdentifier = cropSelection.resolve();
        if (cropIdentifier == null) return;
        String format = format();
        viewModel.generate(cropIdentifier, format);
    }

    private String format() {
        int checkedId = formatToggleGroup.getCheckedButtonId();
        return checkedId == R.id.radioCsv ? "csv" : "pdf";
    }
}