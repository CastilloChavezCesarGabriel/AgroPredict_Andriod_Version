package com.agropredict.presentation.user_interface.holder;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import com.agropredict.R;
import com.agropredict.domain.entity.Crop;
import com.agropredict.presentation.viewmodel.report.ReportViewModel;
import java.util.List;

public final class ReportViewHolder {

    private final CropSelection cropSelection;
    private final RadioGroup formatRadioGroup;
    private final Button shareButton;
    private final ProgressBar progressIndicator;

    public ReportViewHolder(Activity activity) {
        this.cropSelection = new CropSelection(activity.findViewById(R.id.spnCropSelection));
        this.formatRadioGroup = activity.findViewById(R.id.radioGroupFormat);
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
        int checkedId = formatRadioGroup.getCheckedRadioButtonId();
        return checkedId == R.id.radioCsv ? "csv" : "pdf";
    }
}