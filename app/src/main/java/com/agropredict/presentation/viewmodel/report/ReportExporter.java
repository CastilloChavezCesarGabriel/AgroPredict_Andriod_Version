package com.agropredict.presentation.viewmodel.report;

import com.agropredict.application.IRepositoryFactory;
import com.agropredict.application.request.ReportRequest;
import com.agropredict.domain.Identifier;
import com.agropredict.application.result.OperationResult;
import com.agropredict.application.usecase.crop.FindCropUseCase;
import com.agropredict.application.usecase.report.GenerateReportUseCase;
import com.agropredict.application.usecase.report.StoreReportUseCase;
import com.agropredict.application.visitor.IOperationResultVisitor;
import com.agropredict.domain.component.report.ReportContext;
import com.agropredict.domain.component.report.ReportDetail;
import com.agropredict.domain.component.report.ReportIdentity;
import com.agropredict.domain.component.report.ReportStorage;
import com.agropredict.domain.entity.Crop;
import com.agropredict.domain.entity.Diagnostic;

public final class ReportExporter implements IOperationResultVisitor {
    private final IRepositoryFactory factory;
    private final IReportView view;
    private String userIdentifier;
    private String cropIdentifier;
    private String format;

    public ReportExporter(IRepositoryFactory factory, IReportView view) {
        this.factory = factory;
        this.view = view;
    }

    public void configure(String userIdentifier) {
        this.userIdentifier = userIdentifier;
    }

    public void export(String cropIdentifier, String format) {
        this.cropIdentifier = cropIdentifier;
        this.format = format;
        Crop crop = new FindCropUseCase(factory.createCropRepository()).find(cropIdentifier);
        Diagnostic diagnostic = factory.createDiagnosticRepository().resolve(userIdentifier, cropIdentifier);
        if (crop == null || diagnostic == null) {
            view.notify("No data found to export");
            return;
        }
        OperationResult result = new GenerateReportUseCase(factory.createReportService(format)).generate(crop, diagnostic);
        result.accept(this);
    }

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
        ReportIdentity identity = new ReportIdentity(Identifier.generate("rpt"), format);
        ReportContext context = new ReportContext(cropIdentifier, cropIdentifier);
        ReportStorage storage = new ReportStorage(userIdentifier, filePath);
        ReportDetail detail = new ReportDetail(context, storage);
        ReportRequest request = new ReportRequest(identity, detail);
        new StoreReportUseCase(factory.createReportRepository()).store(request);
    }

}