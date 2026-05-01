package com.agropredict.presentation.viewmodel.report_generation;

import com.agropredict.application.factory.IReportingFactory;
import com.agropredict.application.request.report_generation.ReportRequest;
import com.agropredict.application.request.report_generation.Destination;
import com.agropredict.domain.Identifier;
import com.agropredict.application.operation_result.OperationResult;
import com.agropredict.application.usecase.crop.FindCropUseCase;
import com.agropredict.application.usecase.report.GenerateReportUseCase;
import com.agropredict.application.usecase.report.StoreReportUseCase;
import com.agropredict.domain.entity.Crop;
import com.agropredict.domain.entity.Diagnostic;
import com.agropredict.domain.entity.Report;

public final class ReportExporter {
    private final IReportingFactory factory;
    private final IReportView view;
    private final String userIdentifier;

    public ReportExporter(IReportingFactory factory, IReportView view, String userIdentifier) {
        this.factory = factory;
        this.view = view;
        this.userIdentifier = userIdentifier;
    }

    public void export(String cropIdentifier, String format) {
        Crop crop = new FindCropUseCase(factory.createCropRepository()).find(cropIdentifier);
        Diagnostic diagnostic = factory.createDiagnosticRepository().resolve(userIdentifier, cropIdentifier);
        if (crop == null || diagnostic == null) {
            view.notify("No data found to export");
            return;
        }
        Report report = new Report(Identifier.generate("rpt"), format);
        OperationResult result = new GenerateReportUseCase(factory.createReportService(format)).generate(crop, diagnostic);
        diagnostic.pair(cropIdentifier, new ReportRequestComposer(report, result, this));
    }

    public void persist(ReportRequest request, String filePath) {
        view.rest();
        new StoreReportUseCase(factory.createReportRepository()).store(request, new Destination(userIdentifier, filePath));
        view.notify("Report generated successfully");
        view.offer(filePath);
    }

    public void reject() {
        view.rest();
        view.notify("Error generating the report");
    }
}