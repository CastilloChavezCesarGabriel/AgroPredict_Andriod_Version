package com.agropredict.presentation.user_interface.selector;

import android.app.Activity;
import android.widget.EditText;
import android.widget.Spinner;
import com.agropredict.R;
import com.agropredict.application.request.CropUpdateRequest;
import com.agropredict.domain.entity.Crop;
import com.agropredict.domain.visitor.crop.ICropVisitor;
import com.agropredict.presentation.user_interface.catalog_input.SoilTypeCatalog;
import com.agropredict.presentation.user_interface.catalog_input.StageCatalog;

public final class FieldEditor implements ICropVisitor {
    private final EditText cropNameInput;
    private final EditText areaInput;
    private final Spinner soilTypeSpinner;
    private final Spinner stageSpinner;

    public FieldEditor(Activity activity) {
        cropNameInput = activity.findViewById(R.id.etFieldName);
        areaInput = activity.findViewById(R.id.etArea);
        soilTypeSpinner = activity.findViewById(R.id.spnSoilType);
        stageSpinner = activity.findViewById(R.id.spnStage);
    }

    public CropUpdateRequest collect(String identifier) {
        String fieldName = cropNameInput.getText().toString().trim();
        Crop crop = new Crop(identifier, null);
        crop.locate(fieldName, null);
        String soilType = soilTypeSpinner.getSelectedItem().toString();
        crop.plant(soilType, areaInput.getText().toString().trim());
        String stage = stageSpinner.getSelectedItem().toString();
        crop.schedule(null, stage);
        return new CropUpdateRequest(crop);
    }

    public void populate(Crop crop) {
        crop.accept(this);
    }

    public void populate(SoilTypeCatalog soilTypeOption) {
        soilTypeOption.populate(soilTypeSpinner);
    }

    public void populate(StageCatalog stageOption) {
        stageOption.populate(stageSpinner);
    }

    @Override
    public void visitIdentity(String identifier, String cropType) {}

    @Override
    public void visitField(String name, String location) {
        cropNameInput.setText(name);
    }

    @Override
    public void visitSoil(String typeIdentifier, String area) {
        select(soilTypeSpinner, typeIdentifier);
        areaInput.setText(area != null ? area : "");
    }

    @Override
    public void visitPlanting(String date, String stageIdentifier) {
        select(stageSpinner, stageIdentifier);
    }

    private void select(Spinner spinner, String value) {
        if (value == null || spinner.getAdapter() == null) return;
        for (int index = 0; index < spinner.getCount(); index++) {
            if (spinner.getItemAtPosition(index).toString().equals(value)) {
                spinner.setSelection(index);
                return;
            }
        }
    }
}
