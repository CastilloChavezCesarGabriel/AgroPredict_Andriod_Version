package com.agropredict.presentation.user_interface.selector;

import android.app.Activity;
import android.widget.EditText;
import android.widget.Spinner;
import com.agropredict.R;
import com.agropredict.application.crop_management.request.CropUpdateRequest;
import com.agropredict.application.repository.ICatalogRepository;
import com.agropredict.domain.crop.Crop;
import com.agropredict.domain.crop.CropProfile;
import com.agropredict.domain.crop.Field;
import com.agropredict.domain.crop.GrowthCycle;
import com.agropredict.domain.crop.Plot;
import com.agropredict.domain.crop.Soil;
import com.agropredict.domain.crop.visitor.IFieldConsumer;
import com.agropredict.domain.crop.visitor.IPlantingConsumer;
import com.agropredict.domain.crop.visitor.ISoilConsumer;
import com.agropredict.presentation.user_interface.catalog_input.SoilTypeOption;
import com.agropredict.presentation.user_interface.catalog_input.StageOption;

public final class FieldEditor implements IFieldConsumer, ISoilConsumer, IPlantingConsumer {
    private final Activity activity;
    private final ICatalogRepository soilTypes;
    private final ICatalogRepository stages;

    public FieldEditor(Activity activity, ICatalogRepository soilTypes, ICatalogRepository stages) {
        this.activity = activity;
        this.soilTypes = soilTypes;
        this.stages = stages;
    }

    public CropUpdateRequest collect(String identifier) {
        Field field = new Field(read(R.id.etFieldName), read(R.id.etLocation));
        Soil soil = new Soil(soilTypes.resolve(pick(R.id.spnSoilType)), read(R.id.etArea));
        GrowthCycle cycle = new GrowthCycle(read(R.id.etPlantingDate), stages.resolve(pick(R.id.spnStage)));
        return new CropUpdateRequest(identifier, new CropProfile(new Plot(field, soil), cycle));
    }

    public void apply(Crop crop) {
        crop.locate(this);
        crop.analyze(this);
        crop.track(this);
    }

    public void furnish(SoilTypeOption soilTypeOption) {
        soilTypeOption.populate(activity.findViewById(R.id.spnSoilType));
    }

    public void arrange(StageOption stageOption) {
        stageOption.populate(activity.findViewById(R.id.spnStage));
    }

    @Override
    public void locate(String name, String location) {
        write(R.id.etFieldName, name);
        write(R.id.etLocation, location);
    }

    @Override
    public void analyze(String typeIdentifier, String area) {
        choose(R.id.spnSoilType, typeIdentifier);
        write(R.id.etArea, area);
    }

    @Override
    public void track(String date, String stageIdentifier) {
        write(R.id.etPlantingDate, date);
        choose(R.id.spnStage, stageIdentifier);
    }

    private String read(int viewId) {
        EditText input = activity.findViewById(viewId);
        return input.getText().toString().trim();
    }

    private String pick(int spinnerId) {
        Spinner spinner = activity.findViewById(spinnerId);
        return spinner.getSelectedItem().toString();
    }

    private void write(int viewId, String value) {
        EditText input = activity.findViewById(viewId);
        input.setText(value != null ? value : "");
    }

    private void choose(int spinnerId, String value) {
        if (value == null) return;
        Spinner spinner = activity.findViewById(spinnerId);
        if (spinner.getAdapter() == null) return;
        for (int index = 0; index < spinner.getCount(); index++) {
            if (spinner.getItemAtPosition(index).toString().equals(value)) {
                spinner.setSelection(index);
                return;
            }
        }
    }
}
