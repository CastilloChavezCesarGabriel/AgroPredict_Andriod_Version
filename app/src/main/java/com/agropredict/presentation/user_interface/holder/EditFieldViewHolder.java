package com.agropredict.presentation.user_interface.holder;

import android.app.Activity;
import android.widget.EditText;
import android.widget.Spinner;
import com.agropredict.R;
import com.agropredict.application.request.data.CropField;
import com.agropredict.application.request.data.CropIdentity;
import com.agropredict.application.request.CropUpdateRequest;
import com.agropredict.domain.component.crop.CropContent;
import com.agropredict.domain.component.crop.CropData;
import com.agropredict.domain.component.crop.CropDetail;
import com.agropredict.domain.component.crop.CropEnvironment;
import com.agropredict.domain.component.crop.CropLocation;
import com.agropredict.domain.component.crop.CropOwnership;
import com.agropredict.domain.component.crop.CropSoil;
import com.agropredict.domain.entity.Crop;
import com.agropredict.domain.visitor.crop.ICropContentVisitor;
import com.agropredict.domain.visitor.crop.ICropDataVisitor;
import com.agropredict.domain.visitor.crop.ICropDetailVisitor;
import com.agropredict.domain.visitor.crop.ICropEnvironmentVisitor;
import com.agropredict.domain.visitor.crop.ICropLocationVisitor;
import com.agropredict.domain.visitor.crop.ICropOwnershipVisitor;
import com.agropredict.domain.visitor.crop.ICropSoilVisitor;
import com.agropredict.domain.visitor.crop.ICropVisitor;
import com.agropredict.presentation.user_interface.input.SoilTypeCatalog;
import com.agropredict.presentation.user_interface.input.StageCatalog;

public final class EditFieldViewHolder implements ICropVisitor, ICropDataVisitor,
        ICropDetailVisitor, ICropContentVisitor, ICropEnvironmentVisitor,
        ICropSoilVisitor, ICropOwnershipVisitor, ICropLocationVisitor {
    private final EditText cropNameInput;
    private final EditText areaInput;
    private final Spinner soilTypeSpinner;
    private final Spinner stageSpinner;

    public EditFieldViewHolder(Activity activity) {
        cropNameInput = activity.findViewById(R.id.etFieldName);
        areaInput = activity.findViewById(R.id.etArea);
        soilTypeSpinner = activity.findViewById(R.id.spnSoilType);
        stageSpinner = activity.findViewById(R.id.spnStage);
    }

    public CropUpdateRequest collect(String identifier) {
        String cropName = cropNameInput.getText().toString().trim();
        CropIdentity identity = new CropIdentity(identifier, cropName);
        String soilType = soilTypeSpinner.getSelectedItem().toString();
        String stage = stageSpinner.getSelectedItem().toString();
        CropField field = new CropField(soilType, stage);
        return new CropUpdateRequest(identity, field);
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
    public void visit(String identifier, CropData data) {
        data.accept(this);
    }

    @Override
    public void visit(CropDetail detail, CropContent content) {
        if (detail != null) detail.accept(this);
        if (content != null) content.accept(this);
    }

    @Override
    public void visit(String cropType, String fieldName) {
        cropNameInput.setText(fieldName);
    }

    @Override
    public void visit(CropEnvironment environment, CropOwnership ownership) {
        if (environment != null) environment.accept(this);
        if (ownership != null) ownership.accept(this);
    }

    @Override
    public void visit(CropLocation location, CropSoil soil) {
        if (soil != null) soil.accept(this);
    }

    @Override
    public void visitSoil(String soilTypeIdentifier, String area) {
        select(soilTypeSpinner, soilTypeIdentifier);
        areaInput.setText(area != null ? area : "");
    }

    @Override
    public void visitOwnership(String userIdentifier, String stageIdentifier) {
        select(stageSpinner, stageIdentifier);
    }

    @Override
    public void visitLocation(String location, String plantingDate) {}

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