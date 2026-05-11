package com.agropredict.presentation.viewmodel.report_generation;

import com.agropredict.application.factory.IReportingFactory;
import com.agropredict.application.report_generation.request.Destination;
import com.agropredict.application.report_generation.request.ReportRequest;
import com.agropredict.application.service.IReportService;
import com.agropredict.application.crop_management.usecase.FindCropUseCase;
import com.agropredict.application.report_generation.usecase.StoreReportUseCase;
import com.agropredict.domain.crop.Crop;
import com.agropredict.domain.diagnostic.Diagnostic;

public final class ExportScope {
    private final IReportingFactory factory;
    private final String userIdentifier;

    public ExportScope(IReportingFactory factory, String userIdentifier) {
        this.factory = factory;
        this.userIdentifier = userIdentifier;
    }

    public Crop find(String cropIdentifier) {
        return new FindCropUseCase(factory.createCropRepository()).find(cropIdentifier);
    }

    public Diagnostic resolve(String cropIdentifier) {
        return factory.createDiagnosticRepository().resolve(userIdentifier, cropIdentifier);
    }

    public IReportService prepare(String format) {
        return factory.createReportService(format);
    }

    public void archive(ReportRequest request, String filePath) {
        new StoreReportUseCase(factory.createReportRepository()).store(request, new Destination(userIdentifier, filePath));
    }
}