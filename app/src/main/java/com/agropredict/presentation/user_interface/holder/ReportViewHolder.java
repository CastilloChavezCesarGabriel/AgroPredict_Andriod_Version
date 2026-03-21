package com.agropredict.presentation.user_interface.holder;
import com.agropredict.presentation.user_interface.spinner.SpinnerPopulator;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
import com.agropredict.R;
import com.agropredict.domain.component.crop.CropContent;
import com.agropredict.domain.component.crop.CropData;
import com.agropredict.domain.component.crop.CropDetail;
import com.agropredict.domain.entity.Crop;
import com.agropredict.domain.visitor.crop.ICropDataVisitor;
import com.agropredict.domain.visitor.crop.ICropDetailVisitor;
import com.agropredict.domain.visitor.crop.ICropVisitor;
import com.agropredict.presentation.viewmodel.report.ReportViewModel;
import java.util.ArrayList;
import java.util.List;

public final class ReportViewHolder implements ICropVisitor,
        ICropDataVisitor, ICropDetailVisitor {

    private final Spinner cropSpinner;
    private final RadioGroup formatRadioGroup;
    private final Button shareButton;
    private final ProgressBar progressIndicator;
    private final List<String> cropIdentifiers;
    private final List<String> labels;

    public ReportViewHolder(Activity activity) {
        this.cropSpinner = activity.findViewById(R.id.spnCropSelection);
        this.formatRadioGroup = activity.findViewById(R.id.radioGroupFormat);
        this.shareButton = activity.findViewById(R.id.btnShare);
        this.progressIndicator = activity.findViewById(R.id.progressGeneration);
        this.cropIdentifiers = new ArrayList<>();
        this.labels = new ArrayList<>();
        shareButton.setVisibility(View.GONE);
    }

    public void load() {
        progressIndicator.setVisibility(View.VISIBLE);
    }

    public void idle() {
        progressIndicator.setVisibility(View.GONE);
    }

    public void populate(List<Crop> crops) {
        cropIdentifiers.clear();
        labels.clear();
        for (Crop crop : crops) {
            crop.accept(this);
        }
        SpinnerPopulator.populate(cropSpinner, labels);
    }

    public void offer(String filePath) {
        shareButton.setVisibility(View.VISIBLE);
    }

    public void listen(View.OnClickListener listener) {
        shareButton.setOnClickListener(listener);
    }

    public void generate(ReportViewModel viewModel) {
        if (cropSpinner.getSelectedItem() == null) return;
        int position = cropSpinner.getSelectedItemPosition();
        String cropIdentifier = cropIdentifiers.get(position);
        int checkedId = formatRadioGroup.getCheckedRadioButtonId();
        String format = checkedId == R.id.radioCsv ? "csv" : "pdf";
        viewModel.generate(cropIdentifier, format);
    }

    @Override
    public void visit(String identifier, CropData data) {
        cropIdentifiers.add(identifier);
        data.accept(this);
    }

    @Override
    public void visit(CropDetail detail, CropContent content) {
        if (detail != null) detail.accept(this);
    }

    @Override
    public void visit(String cropType, String fieldName) {
        labels.add(cropType != null ? cropType : fieldName);
    }
}
