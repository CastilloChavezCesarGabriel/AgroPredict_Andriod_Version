package com.agropredict.infrastructure.persistence;

import android.content.ContentValues;
import com.agropredict.domain.value.crop.CropContent;
import com.agropredict.domain.value.crop.CropData;
import com.agropredict.domain.value.crop.CropDetail;
import com.agropredict.domain.value.crop.CropEnvironment;
import com.agropredict.domain.value.crop.CropLocation;
import com.agropredict.domain.value.crop.CropOwnership;
import com.agropredict.domain.value.crop.CropSoil;
import com.agropredict.domain.visitor.ICropContentVisitor;
import com.agropredict.domain.visitor.ICropDataVisitor;
import com.agropredict.domain.visitor.ICropDetailVisitor;
import com.agropredict.domain.visitor.ICropEnvironmentVisitor;
import com.agropredict.domain.visitor.ICropLocationVisitor;
import com.agropredict.domain.visitor.ICropOwnershipVisitor;
import com.agropredict.domain.visitor.ICropSoilVisitor;
import com.agropredict.domain.visitor.ICropVisitor;

public final class CropRecorder implements ICropVisitor, ICropDataVisitor,
        ICropContentVisitor, ICropEnvironmentVisitor, ICropDetailVisitor,
        ICropLocationVisitor, ICropSoilVisitor, ICropOwnershipVisitor {

    private final ContentValues values;

    public CropRecorder(ContentValues values) {
        this.values = values;
    }

    @Override
    public void visit(String identifier, CropData data) {
        values.put("id", identifier);
        data.accept(this);
    }

    @Override
    public void visit(CropDetail detail, CropContent content) {
        if (detail != null) detail.accept(this);
        if (content != null) content.accept(this);
    }

    @Override
    public void visit(CropEnvironment environment, CropOwnership ownership) {
        if (environment != null) environment.accept(this);
        if (ownership != null) ownership.accept(this);
    }

    @Override
    public void visit(CropLocation location, CropSoil soil) {
        if (location != null) location.accept(this);
        if (soil != null) soil.accept(this);
    }

    @Override
    public void visit(String cropType, String fieldName) {
        values.put("crop_type", cropType);
        values.put("field_name", fieldName);
    }

    @Override
    public void visitLocation(String location, String plantingDate) {
        values.put("location", location);
        values.put("planting_date", plantingDate);
    }

    @Override
    public void visitSoil(String soilTypeIdentifier, String area) {
        values.put("soil_type_id", soilTypeIdentifier);
        values.put("area", area);
    }

    @Override
    public void visitOwnership(String userIdentifier, String stageIdentifier) {
        values.put("user_id", userIdentifier);
        values.put("phenological_stage_id", stageIdentifier);
    }
}
