package com.agropredict.presentation.user_interface.selector;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import com.agropredict.domain.entity.Crop;
import com.agropredict.domain.visitor.crop.ICropVisitor;
import com.agropredict.presentation.user_interface.catalog_input.SpinnerPopulator;
import java.util.ArrayList;
import java.util.List;

public final class CropTypeFilter implements ICropVisitor, AdapterView.OnItemSelectedListener {
    private final Spinner spinner;
    private final List<String> types;
    private final ISelectionListener listener;

    public CropTypeFilter(Spinner spinner, ISelectionListener listener) {
        this.spinner = spinner;
        this.listener = listener;
        this.types = new ArrayList<>();
        spinner.setOnItemSelectedListener(this);
    }

    public void populate(List<Crop> crops) {
        types.clear();
        for (Crop crop : crops) crop.accept(this);
        SpinnerPopulator.populate(spinner, types);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position < types.size()) listener.onSelect(types.get(position));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    @Override
    public void visitIdentity(String identifier, String cropType) {
        String label = cropType != null ? cropType : "Unknown";
        if (types.contains(label)) return;
        types.add(label);
    }
}
