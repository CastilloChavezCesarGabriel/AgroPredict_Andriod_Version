package com.agropredict.presentation.viewmodel.report_generation;

import com.agropredict.application.crop_management.usecase.ListCropUseCase;
import com.agropredict.application.service.IReportServiceCatalog;
import com.agropredict.application.service.ReportFormat;
import com.agropredict.domain.crop.Crop;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class ReportViewModel {
    private final ReportingData data;
    private final IReportServiceCatalog reportCatalog;
    private final IReportView view;
    private final ExecutorService executor;
    private ReportExporter exporter;

    public ReportViewModel(ReportingData data, IReportServiceCatalog reportCatalog, IReportView view) {
        this.data = Objects.requireNonNull(data, "report view model requires reporting data");
        this.reportCatalog = Objects.requireNonNull(reportCatalog, "report view model requires a report catalog");
        this.view = Objects.requireNonNull(view, "report view model requires a view");
        this.executor = Executors.newSingleThreadExecutor();
    }

    public void load(ListCropUseCase listCrops, String userIdentifier) {
        this.exporter = new ReportExporter(new ExportScope(data, reportCatalog, userIdentifier), view);
        List<Crop> crops = listCrops.list(userIdentifier);
        view.populate(crops);
    }

    public void generate(String cropIdentifier, ReportFormat format) {
        view.load();
        executor.execute(() -> exporter.export(cropIdentifier, format));
    }

    public void release() {
        executor.shutdown();
    }
}
