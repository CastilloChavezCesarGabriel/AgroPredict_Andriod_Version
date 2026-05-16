package com.agropredict.presentation.viewmodel.report_generation;

import com.agropredict.application.report_generation.request.ReportRequest;
import com.agropredict.application.service.ReportFormat;
import com.agropredict.domain.identifier.IdentifierFactory;
import com.agropredict.application.operation_result.IUseCaseResult;
import com.agropredict.application.report_generation.usecase.GenerateReportUseCase;
import com.agropredict.domain.crop.Crop;
import com.agropredict.domain.diagnostic.Diagnostic;
import com.agropredict.domain.report.Report;
import java.util.Objects;

public final class ReportExporter {
    private final ExportScope scope;
    private final IReportView view;
    private final ExportPresentation presentation;

    public ReportExporter(ExportScope scope, IReportView view, ExportPresentation presentation) {
        this.scope = Objects.requireNonNull(scope, "report exporter requires an export scope");
        this.view = Objects.requireNonNull(view, "report exporter requires a view");
        this.presentation = Objects.requireNonNull(presentation, "report exporter requires an export presentation");
    }

    public void export(String cropIdentifier, ReportFormat format) {
        Crop crop = scope.find(cropIdentifier);
        Diagnostic diagnostic = scope.resolve(cropIdentifier);
        if (crop == null || diagnostic == null) {
            view.notify("No data found to export");
            return;
        }
        Report report = new Report(IdentifierFactory.generate("rpt"), format.serialize());
        IUseCaseResult result = new GenerateReportUseCase(scope.prepare(format)).generate(crop, diagnostic);
        diagnostic.pair(cropIdentifier, new ReportRequestComposer(report, new ReportOutcome(result, this, format)));
    }

    public void persist(ReportRequest request, String filePath, ReportFormat format) {
        view.rest();
        scope.archive(request, filePath);
        view.notify("Report generated successfully");
        presentation.choose(format, filePath, view::offer);
    }

    public void reject() {
        view.rest();
        view.notify("Error generating the report");
    }
}
