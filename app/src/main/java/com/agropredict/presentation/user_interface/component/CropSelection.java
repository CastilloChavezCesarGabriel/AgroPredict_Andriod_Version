package com.agropredict.presentation.user_interface.component;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import com.agropredict.domain.component.crop.CropContent;
import com.agropredict.domain.component.crop.CropData;
import com.agropredict.domain.component.crop.CropDetail;
import com.agropredict.domain.entity.Crop;
import com.agropredict.domain.visitor.crop.ICropDataVisitor;
import com.agropredict.domain.visitor.crop.ICropDetailVisitor;
import com.agropredict.domain.visitor.crop.ICropVisitor;
import com.agropredict.presentation.utilities.SpinnerPopulator;
import java.util.ArrayList;
import java.util.List;

public final class CropSelection implements ICropVisitor, ICropDataVisitor, ICropDetailVisitor,
        AdapterView.OnItemSelectedListener {
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
            crop.accept(this);
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
    public void visit(String identifier, CropData data) {
        identifiers.add(identifier);
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