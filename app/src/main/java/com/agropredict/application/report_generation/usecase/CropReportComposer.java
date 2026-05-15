package com.agropredict.application.report_generation.usecase;

import com.agropredict.application.service.IReportWriter;
import com.agropredict.domain.crop.Crop;
import com.agropredict.domain.crop.visitor.ICropIdentityConsumer;
import com.agropredict.domain.crop.visitor.IFieldConsumer;
import com.agropredict.domain.crop.visitor.IPlantingConsumer;
import com.agropredict.domain.crop.visitor.ISoilConsumer;
import java.util.Objects;

public final class CropReportComposer implements
        ICropIdentityConsumer, IFieldConsumer, ISoilConsumer, IPlantingConsumer {
    private final IReportWriter report;

    public CropReportComposer(IReportWriter report) {
        this.report = Objects.requireNonNull(report,
                "crop report composer requires a report writer");
    }

    public void compose(Crop crop) {
        crop.describe(this);
        crop.locate(this);
        crop.analyze(this);
        crop.track(this);
    }

    @Override
    public void describe(String identifier, String cropType) {
        report.write("crop_type", cropType);
    }

    @Override
    public void locate(String name, String location) {
        report.write("field_name", name);
        report.write("field_location", location);
    }

    @Override
    public void analyze(String typeIdentifier, String area) {
        report.write("soil_area", area);
    }

    @Override
    public void track(String date, String stageIdentifier) {
        report.write("planting_date", date);
    }
}
