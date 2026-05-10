package com.agropredict.presentation.user_interface.selector;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import com.agropredict.domain.crop.Crop;
import com.agropredict.domain.crop.visitor.ICropIdentityConsumer;
import com.agropredict.presentation.user_interface.catalog_input.SpinnerPopulator;
import java.util.ArrayList;
import java.util.List;

public final class CropSelection implements ICropIdentityConsumer, AdapterView.OnItemSelectedListener {
    private final Spinner spinner;
    private final List<String> identifiers;
    private final List<String> labels;
    private final ISelectionListener listener;

    public CropSelection(Spinner spinner, ISelectionListener listener) {
        this.spinner = spinner;
        this.listener = listener;
        this.identifiers = new ArrayList<>();
        this.labels = new ArrayList<>();
        spinner.setOnItemSelectedListener(this);
    }

    public void populate(List<Crop> crops) {
        identifiers.clear();
        labels.clear();
        for (Crop crop : crops) {
            crop.describe(this);
        }
        SpinnerPopulator.populate(spinner, labels);
    }

    public String resolve() {
        if (spinner.getSelectedItem() == null) return null;
        return identifiers.get(spinner.getSelectedItemPosition());
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position < identifiers.size())
            listener.onSelect(identifiers.get(position));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    @Override
    public void describe(String identifier, String cropType) {
        identifiers.add(identifier);
        labels.add(cropType != null ? cropType : "Unknown");
    }
}
