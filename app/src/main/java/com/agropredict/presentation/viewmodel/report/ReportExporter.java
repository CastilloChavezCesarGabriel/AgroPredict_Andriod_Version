package com.agropredict.presentation.viewmodel.report;

import com.agropredict.application.IRepositoryFactory;
import com.agropredict.application.request.report_generation.ReportRequest;
import com.agropredict.application.request.report_generation.Destination;
import com.agropredict.application.request.report_generation.Finding;
import com.agropredict.domain.Identifier;
import com.agropredict.application.operation_result.OperationResult;
import com.agropredict.application.usecase.crop.FindCropUseCase;
import com.agropredict.application.usecase.report.GenerateReportUseCase;
import com.agropredict.application.usecase.report.StoreReportUseCase;
import com.agropredict.application.visitor.IOperationResultVisitor;
import com.agropredict.domain.entity.Crop;
import com.agropredict.domain.entity.Diagnostic;
import com.agropredict.domain.entity.Report;
import com.agropredict.domain.visitor.diagnostic.IDiagnosticVisitor;

public final class ReportExporter implements IOperationResultVisitor, IDiagnosticVisitor {
    private final IRepositoryFactory factory;
    private final IReportView view;
    private String userIdentifier;
    private String diagnosticIdentifier;
    private ReportRequest request;

    public ReportExporter(IRepositoryFactory factory, IReportView view) {
        this.factory = factory;
        this.view = view;
    }

    public void configure(String userIdentifier) {
        this.userIdentifier = userIdentifier;
    }

    public void export(String cropIdentifier, String format) {
        Crop crop = new FindCropUseCase(factory.createCropRepository()).find(cropIdentifier);
        Diagnostic diagnostic = factory.createDiagnosticRepository().resolve(userIdentifier, cropIdentifier);
        if (crop == null || diagnostic == null) {
            view.notify("No data found to export");
            return;
        }
        prepare(diagnostic, cropIdentifier, format);
        generate(crop, diagnostic, format);
    }

    private void prepare(Diagnostic diagnostic, String cropIdentifier, String format) {
        diagnostic.accept(this);
        Report report = new Report(Identifier.generate("rpt"), format);
        this.request = new ReportRequest(report, new Finding(diagnosticIdentifier, cropIdentifier));
    }

    private void generate(Crop crop, Diagnostic diagnostic, String format) {
        OperationResult result = new GenerateReportUseCase(factory.createReportService(format)).generate(crop, diagnostic);
        result.accept(this);
    }

    @Override
    public void visitIdentity(String identifier) {
        this.diagnosticIdentifier = identifier;
    }

    @Override
    public void visitPrediction(String predictedCrop, double confidence) {}

    @Override
    public void visitAssessment(String severity, String shortSummary) {}

    @Override
    public void visitRecommendation(String recommendationText) {}

    @Override
    public void visit(boolean completed, String filePath) {
        view.idle();
        if (completed) {
            persist(filePath);
            view.notify("Report generated successfully");
            view.offer(filePath);
        } else {
            view.notify("Error generating the report");
        }
    }

    private void persist(String filePath) {
        Destination destination = new Destination(userIdentifier, filePath);
        new StoreReportUseCase(factory.createReportRepository()).store(request, destination);
    }
}
