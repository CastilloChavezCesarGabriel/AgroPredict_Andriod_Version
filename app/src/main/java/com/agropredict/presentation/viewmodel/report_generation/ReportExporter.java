package com.agropredict.presentation.viewmodel.report_generation;

import com.agropredict.application.report_generation.request.ReportRequest;
import com.agropredict.domain.identifier.IdentifierFactory;
import com.agropredict.application.operation_result.IUseCaseResult;
import com.agropredict.application.report_generation.usecase.GenerateReportUseCase;
import com.agropredict.domain.crop.Crop;
import com.agropredict.domain.diagnostic.Diagnostic;
import com.agropredict.domain.report.Report;

public final class ReportExporter {
    private final ExportScope scope;
    private final IReportView view;

    public ReportExporter(ExportScope scope, IReportView view) {
        this.scope = scope;
        this.view = view;
    }

    public void export(String cropIdentifier, String format) {
        Crop crop = scope.find(cropIdentifier);
        Diagnostic diagnostic = scope.resolve(cropIdentifier);
        if (crop == null || diagnostic == null) {
            view.notify("No data found to export");
            return;
        }
        Report report = new Report(IdentifierFactory.generate("rpt"), format);
        IUseCaseResult result = new GenerateReportUseCase(scope.prepare(format)).generate(crop, diagnostic);
        diagnostic.pair(cropIdentifier, new ReportRequestComposer(report, new ReportOutcome(result, this)));
    }

    public void persist(ReportRequest request, String filePath) {
        view.rest();
        scope.archive(request, filePath);
        view.notify("Report generated successfully");
        view.offer(filePath);
    }

    public void reject() {
        view.rest();
        view.notify("Error generating the report");
    }
}