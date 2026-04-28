package com.agropredict.presentation.viewmodel.report_generation;

import com.agropredict.application.factory.IReportingFactory;
import com.agropredict.application.usecase.crop.ListCropUseCase;
import com.agropredict.domain.entity.Crop;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class ReportViewModel {
    private final IReportingFactory factory;
    private final IReportView view;
    private final ExecutorService executor;
    private ReportExporter exporter;

    public ReportViewModel(IReportingFactory factory, IReportView view) {
        this.factory = factory;
        this.view = view;
        this.executor = Executors.newSingleThreadExecutor();
    }

    public void load(ListCropUseCase listCrops, String userIdentifier) {
        this.exporter = new ReportExporter(factory, view, userIdentifier);
        List<Crop> crops = listCrops.list(userIdentifier);
        view.populate(crops);
    }

    public void generate(String cropIdentifier, String format) {
        view.load();
        executor.execute(() -> exporter.export(cropIdentifier, format));
    }

    public void release() {
        executor.shutdown();
    }
}