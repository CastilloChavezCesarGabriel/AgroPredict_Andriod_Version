package com.agropredict.presentation.viewmodel.report_generation;

import com.agropredict.application.crop_management.usecase.ListCropUseCase;
import com.agropredict.application.service.ReportFormat;
import com.agropredict.domain.crop.Crop;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class ReportViewModel {
    private final ReportingSource source;
    private final ExportPresentation presentation;
    private final IReportView view;
    private final ExecutorService executor;
    private ReportExporter exporter;

    public ReportViewModel(ReportingSource source, ExportPresentation presentation, IReportView view) {
        this.source = Objects.requireNonNull(source, "report view model requires a reporting source");
        this.presentation = Objects.requireNonNull(presentation, "report view model requires an export presentation");
        this.view = Objects.requireNonNull(view, "report view model requires a view");
        this.executor = Executors.newSingleThreadExecutor();
    }

    public void load(ListCropUseCase listCrops, String userIdentifier) {
        this.exporter = new ReportExporter(new ExportScope(source, userIdentifier), view, presentation);
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
