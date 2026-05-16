package com.agropredict.presentation.user_interface.selector;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import com.agropredict.domain.crop.Crop;
import com.agropredict.domain.crop.visitor.ICropDescriptionConsumer;
import com.agropredict.presentation.user_interface.catalog_input.SpinnerPopulator;
import java.util.ArrayList;
import java.util.List;

public final class CropChoiceSelection implements ICropDescriptionConsumer, AdapterView.OnItemSelectedListener {
    private static final String NEW_CROP_LABEL = "— New crop —";
    private static final String NEW_CROP_SENTINEL = "";
    private final Spinner spinner;
    private final List<String> identifiers;
    private final List<String> labels;
    private final ISelectionListener listener;

    public CropChoiceSelection(Spinner spinner, ISelectionListener listener) {
        this.spinner = spinner;
        this.listener = listener;
        this.identifiers = new ArrayList<>();
        this.labels = new ArrayList<>();
        spinner.setOnItemSelectedListener(this);
    }

    public void populate(List<Crop> crops) {
        identifiers.clear();
        labels.clear();
        identifiers.add(NEW_CROP_SENTINEL);
        labels.add(NEW_CROP_LABEL);
        for (Crop crop : crops) {
            crop.represent(this);
        }
        new SpinnerPopulator().populate(spinner, labels);
    }

    public String resolve() {
        if (spinner.getSelectedItem() == null) return null;
        String identifier = identifiers.get(spinner.getSelectedItemPosition());
        return identifier.equals(NEW_CROP_SENTINEL) ? null : identifier;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position >= identifiers.size()) return;
        String identifier = identifiers.get(position);
        listener.onSelect(identifier.equals(NEW_CROP_SENTINEL) ? null : identifier);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    @Override
    public void describe(String identifier, String type, String plantingDate) {
        identifiers.add(identifier);
        labels.add(type + " (planted " + plantingDate + ")");
    }
}
