package com.agropredict.infrastructure.persistence.visitor;

import com.agropredict.domain.component.crop.CropContent;
import com.agropredict.domain.component.crop.CropData;
import com.agropredict.domain.component.crop.CropDetail;
import com.agropredict.domain.component.crop.CropEnvironment;
import com.agropredict.domain.component.crop.CropLocation;
import com.agropredict.domain.component.crop.CropOwnership;
import com.agropredict.domain.component.crop.CropSoil;
import com.agropredict.domain.visitor.crop.ICropContentVisitor;
import com.agropredict.domain.visitor.crop.ICropDataVisitor;
import com.agropredict.domain.visitor.crop.ICropDetailVisitor;
import com.agropredict.domain.visitor.crop.ICropEnvironmentVisitor;
import com.agropredict.domain.visitor.crop.ICropLocationVisitor;
import com.agropredict.domain.visitor.crop.ICropOwnershipVisitor;
import com.agropredict.domain.visitor.crop.ICropSoilVisitor;
import com.agropredict.domain.visitor.crop.ICropVisitor;
import com.agropredict.infrastructure.persistence.IRow;

public final class CropPersistenceVisitor implements ICropVisitor, ICropDataVisitor,
        ICropContentVisitor, ICropEnvironmentVisitor, ICropDetailVisitor,
        ICropLocationVisitor, ICropSoilVisitor, ICropOwnershipVisitor {

    private final IRow row;

    public CropPersistenceVisitor(IRow row) {
        this.row = row;
    }

    @Override
    public void visit(String identifier, CropData data) {
        row.record("id", identifier);
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
        row.record("crop_type", cropType);
        row.record("field_name", fieldName);
    }

    @Override
    public void visitLocation(String location, String plantingDate) {
        row.record("location", location);
        row.record("planting_date", plantingDate);
    }

    @Override
    public void visitSoil(String soilTypeIdentifier, String area) {
        row.record("soil_type_id", soilTypeIdentifier);
        row.record("area", area);
    }

    @Override
    public void visitOwnership(String userIdentifier, String stageIdentifier) {
        row.record("user_id", userIdentifier);
        row.record("phenological_stage_id", stageIdentifier);
    }
}