package com.agropredict.infrastructure.persistence.visitor;

import com.agropredict.domain.authentication.ISession;
import com.agropredict.domain.crop.Crop;
import com.agropredict.domain.crop.visitor.ICropIdentityConsumer;
import com.agropredict.domain.crop.visitor.IFieldConsumer;
import com.agropredict.domain.crop.visitor.IPlantingConsumer;
import com.agropredict.domain.authentication.ISessionConsumer;
import com.agropredict.domain.crop.visitor.ISoilConsumer;
import com.agropredict.domain.identifier.IIdentifierConsumer;
import com.agropredict.infrastructure.persistence.database.IRow;

public final class CropPersistenceVisitor implements
        ICropIdentityConsumer, IIdentifierConsumer, IFieldConsumer, ISoilConsumer, IPlantingConsumer, ISessionConsumer {
    private final IRow row;

    public CropPersistenceVisitor(IRow row, ISession session) {
        this.row = row;
        session.report(this);
    }

    public void persist(Crop crop) {
        crop.describe(this);
        crop.locate(this);
        crop.analyze(this);
        crop.track(this);
    }

    @Override
    public void identify(String identifier) {
        row.record("id", identifier);
    }

    @Override
    public void describe(String identifier, String cropType) {
        row.record("id", identifier);
        row.record("crop_type", cropType);
    }

    @Override
    public void locate(String name, String location) {
        row.record("field_name", name);
        row.record("location", location);
    }

    @Override
    public void analyze(String typeIdentifier, String area) {
        row.record("soil_type_id", typeIdentifier);
        row.record("area", area);
    }

    @Override
    public void track(String date, String stageIdentifier) {
        row.record("planting_date", date);
        row.record("phenological_stage_id", stageIdentifier);
    }

    @Override
    public void report(String userIdentifier, String occupation) {
        row.record("user_id", userIdentifier);
    }
}
