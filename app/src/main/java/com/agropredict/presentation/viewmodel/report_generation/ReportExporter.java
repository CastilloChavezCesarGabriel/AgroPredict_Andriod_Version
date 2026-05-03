package com.agropredict.presentation.viewmodel.report_generation;

import com.agropredict.application.request.report_generation.ReportRequest;
import com.agropredict.domain.Identifier;
import com.agropredict.application.operation_result.OperationResult;
import com.agropredict.application.usecase.report.GenerateReportUseCase;
import com.agropredict.domain.entity.Crop;
import com.agropredict.domain.entity.Diagnostic;
import com.agropredict.domain.entity.Report;

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
        Report report = new Report(Identifier.generate("rpt"), format);
        OperationResult result = new GenerateReportUseCase(scope.prepare(format)).generate(crop, diagnostic);
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
