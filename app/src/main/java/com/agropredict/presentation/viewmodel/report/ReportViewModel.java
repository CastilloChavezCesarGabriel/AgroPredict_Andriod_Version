package com.agropredict.presentation.viewmodel.report;

import com.agropredict.application.IRepositoryFactory;
import com.agropredict.application.usecase.crop.ListCropUseCase;
import com.agropredict.domain.entity.Crop;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class ReportViewModel {
    private final ReportExporter exporter;
    private final IReportView view;
    private final ExecutorService executor;

    public ReportViewModel(IRepositoryFactory factory, IReportView view) {
        this.exporter = new ReportExporter(factory, view);
        this.view = view;
        this.executor = Executors.newSingleThreadExecutor();
    }

    public void load(ListCropUseCase listCrops, String userIdentifier) {
        exporter.configure(userIdentifier);
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
