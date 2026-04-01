package com.agropredict.infrastructure.persistence.visitor;

import com.agropredict.domain.Session;
import com.agropredict.domain.visitor.session.ISessionVisitor;
import com.agropredict.domain.visitor.crop.ICropVisitor;
import com.agropredict.infrastructure.persistence.database.IRow;

public final class CropPersistenceVisitor implements ICropVisitor, ISessionVisitor {

    private final IRow row;

    public CropPersistenceVisitor(IRow row, Session session) {
        this.row = row;
        session.accept(this);
    }

    @Override
    public void visitIdentity(String identifier, String cropType) {
        row.record("id", identifier);
        row.record("crop_type", cropType);
    }

    @Override
    public void visitField(String name, String location) {
        row.record("field_name", name);
        row.record("location", location);
    }

    @Override
    public void visitSoil(String typeIdentifier, String area) {
        row.record("soil_type_id", typeIdentifier);
        row.record("area", area);
    }

    @Override
    public void visitPlanting(String date, String stageIdentifier) {
        row.record("planting_date", date);
        row.record("phenological_stage_id", stageIdentifier);
    }

    @Override
    public void visit(String userIdentifier, String occupation) {
        row.record("user_id", userIdentifier);
    }
}