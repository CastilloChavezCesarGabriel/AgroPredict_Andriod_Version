package com.agropredict.presentation.user_interface.selector;

import android.app.Activity;
import android.widget.EditText;
import android.widget.Spinner;
import com.agropredict.R;
import com.agropredict.application.crop_management.request.CropUpdateRequest;
import com.agropredict.domain.crop.CropProfile;
import com.agropredict.domain.crop.Field;
import com.agropredict.domain.crop.GrowthCycle;
import com.agropredict.domain.crop.Plot;
import com.agropredict.domain.crop.Soil;
import com.agropredict.domain.crop.Crop;
import com.agropredict.domain.crop.visitor.IFieldConsumer;
import com.agropredict.domain.crop.visitor.IPlantingConsumer;
import com.agropredict.domain.crop.visitor.ISoilConsumer;
import com.agropredict.presentation.user_interface.catalog_input.SoilTypeOption;
import com.agropredict.presentation.user_interface.catalog_input.StageOption;

public final class FieldEditor implements IFieldConsumer, ISoilConsumer, IPlantingConsumer {
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
        Field field = new Field(cropNameInput.getText().toString().trim(), null);
        Soil soil = new Soil(soilTypeSpinner.getSelectedItem().toString(),
                areaInput.getText().toString().trim());
        GrowthCycle cycle = new GrowthCycle(null, stageSpinner.getSelectedItem().toString());
        return new CropUpdateRequest(identifier, new CropProfile(new Plot(field, soil), cycle));
    }

    public void populate(Crop crop) {
        crop.locate(this);
        crop.analyze(this);
        crop.track(this);
    }

    public void populate(SoilTypeOption soilTypeOption) {
        soilTypeOption.populate(soilTypeSpinner);
    }

    public void populate(StageOption stageOption) {
        stageOption.populate(stageSpinner);
    }

    @Override
    public void locate(String name, String location) {
        cropNameInput.setText(name);
    }

    @Override
    public void analyze(String typeIdentifier, String area) {
        select(soilTypeSpinner, typeIdentifier);
        areaInput.setText(area != null ? area : "");
    }

    @Override
    public void track(String date, String stageIdentifier) {
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
