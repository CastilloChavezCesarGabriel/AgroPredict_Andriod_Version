package com.agropredict.presentation.viewmodel.report_generation;

import com.agropredict.application.factory.IReportingFactory;
import com.agropredict.application.request.report_generation.Destination;
import com.agropredict.application.request.report_generation.ReportRequest;
import com.agropredict.application.service.IReportService;
import com.agropredict.application.usecase.crop.FindCropUseCase;
import com.agropredict.application.usecase.report.StoreReportUseCase;
import com.agropredict.domain.entity.Crop;
import com.agropredict.domain.entity.Diagnostic;

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
