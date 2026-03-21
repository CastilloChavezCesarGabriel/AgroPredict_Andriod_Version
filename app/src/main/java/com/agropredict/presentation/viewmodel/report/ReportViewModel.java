package com.agropredict.presentation.viewmodel.report;

import com.agropredict.application.IRepositoryFactory;
import com.agropredict.application.request.ReportRequest;
import com.agropredict.application.result.OperationResult;
import com.agropredict.application.usecase.crop.ListCropUseCase;
import com.agropredict.application.usecase.crop.FindCropUseCase;
import com.agropredict.application.usecase.diagnostic.FindDiagnosticUseCase;
import com.agropredict.application.usecase.report.GenerateReportUseCase;
import com.agropredict.application.usecase.report.StoreReportUseCase;
import com.agropredict.application.service.IReportService;
import com.agropredict.application.visitor.IOperationResultVisitor;
import com.agropredict.domain.component.report.ReportContext;
import com.agropredict.domain.component.report.ReportDetail;
import com.agropredict.domain.component.report.ReportIdentity;
import com.agropredict.domain.component.report.ReportStorage;
import com.agropredict.domain.entity.Crop;
import com.agropredict.domain.entity.Diagnostic;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class ReportViewModel implements IOperationResultVisitor {
    private final IRepositoryFactory factory;
    private final IReportView view;
    private final ExecutorService executor;
    private String userIdentifier;
    private String cropIdentifier;
    private String format;

    public ReportViewModel(IRepositoryFactory factory, IReportView view) {
        this.factory = factory;
        this.view = view;
        this.executor = Executors.newSingleThreadExecutor();
    }

    public void load(ListCropUseCase listCrops, String userIdentifier) {
        this.userIdentifier = userIdentifier;
        List<Crop> crops = listCrops.list(userIdentifier);
        view.populate(crops);
    }

    public void generate(String cropIdentifier, String format) {
        this.cropIdentifier = cropIdentifier;
        this.format = format;
        view.load();
        executor.execute(() -> export(cropIdentifier, format));
    }

    public void release() {
        executor.shutdown();
    }

    @Override
    public void visit(boolean completed, String filePath) {
        view.idle();
        if (completed) {
            persist(filePath);
            view.notify("Reporte generado exitosamente");
            view.offer(filePath);
        } else {
            view.notify("Error al generar el reporte");
        }
    }

    private void export(String cropIdentifier, String format) {
        FindCropUseCase cropLoader = new FindCropUseCase(factory.createCropRepository());
        FindDiagnosticUseCase diagnosticLoader = new FindDiagnosticUseCase(factory.createDiagnosticRepository());
        Crop crop = cropLoader.find(cropIdentifier);
        Diagnostic diagnostic = diagnosticLoader.find(cropIdentifier);
        if (crop == null || diagnostic == null) {
            view.notify("No se encontraron datos para exportar");
            return;
        }
        IReportService service = resolve(format);
        GenerateReportUseCase generateUseCase = new GenerateReportUseCase(service);
        OperationResult result = generateUseCase.generate(crop, diagnostic);
        result.accept(this);
    }

    private void persist(String filePath) {
        ReportIdentity identity = new ReportIdentity("rpt_" + System.currentTimeMillis(), format);
        ReportContext context = new ReportContext(cropIdentifier, cropIdentifier);
        ReportStorage storage = new ReportStorage(userIdentifier, filePath);
        ReportDetail detail = new ReportDetail(context, storage);
        ReportRequest request = new ReportRequest(identity, detail);
        StoreReportUseCase storeUseCase = new StoreReportUseCase(factory.createReportRepository());
        storeUseCase.store(request);
    }

    private IReportService resolve(String format) {
        return "csv".equals(format)
                ? factory.createCsvReportGenerator()
                : factory.createPdfReportGenerator();
    }
}
