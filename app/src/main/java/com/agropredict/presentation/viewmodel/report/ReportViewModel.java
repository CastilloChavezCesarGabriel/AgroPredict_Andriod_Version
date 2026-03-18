package com.agropredict.presentation.viewmodel.report;

import android.app.Activity;
import com.agropredict.application.IRepositoryFactory;
import com.agropredict.application.result.OperationResult;
import com.agropredict.application.usecase.crop.ListCropUseCase;
import com.agropredict.application.usecase.report.GenerateReportUseCase;
import com.agropredict.application.usecase.report.ShareReportUseCase;
import com.agropredict.application.service.IReportGeneratorService;
import com.agropredict.domain.entity.Crop;
import com.agropredict.domain.entity.Report;
import com.agropredict.domain.value.report.ReportDetail;
import com.agropredict.domain.value.report.ReportIdentity;
import com.agropredict.domain.value.report.ReportContext;
import com.agropredict.presentation.mapping.CropMapping;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class ReportViewModel {

    private final IRepositoryFactory factory;
    private final ListCropUseCase listCropUseCase;
    private final ExecutorService executor;
    private IReportView view;

    public ReportViewModel(IRepositoryFactory factory, ListCropUseCase listCropUseCase) {
        this.factory = factory;
        this.listCropUseCase = listCropUseCase;
        this.executor = Executors.newSingleThreadExecutor();
    }

    public void bind(IReportView view) {
        this.view = view;
    }

    public void load(String userIdentifier) {
        List<Crop> crops = listCropUseCase.list(userIdentifier);
        if (view != null) {
            view.populateCrops(map(crops));
        }
    }

    public void generate(Map<String, String> reportOptions, Activity activity) {
        if (view != null) view.showLoading();
        executor.execute(() -> generateInBackground(reportOptions, activity));
    }

    public void release() {
        executor.shutdown();
    }

    private void generateInBackground(Map<String, String> reportOptions, Activity activity) {
        GenerateReportUseCase generateUseCase = resolve(reportOptions.get("format"));
        Map<String, Object> reportData = new HashMap<>(reportOptions);
        OperationResult result = generateUseCase.generate(reportData);
        store(reportData);
        activity.runOnUiThread(() -> present(result));
    }

    private GenerateReportUseCase resolve(String format) {
        IReportGeneratorService service = "csv".equals(format)
                ? factory.createCsvReportGenerator()
                : factory.createPdfReportGenerator();
        return new GenerateReportUseCase(service);
    }

    private void store(Map<String, Object> reportData) {
        ShareReportUseCase shareUseCase = new ShareReportUseCase(factory.createReportRepository());
        String identifier = "report_" + System.currentTimeMillis();
        String format = String.valueOf(reportData.getOrDefault("format", "pdf"));
        String cropIdentifier = String.valueOf(reportData.get("crop_identifier"));
        ReportIdentity identity = new ReportIdentity(identifier, format);
        ReportContext context = new ReportContext(null, cropIdentifier);
        ReportDetail detail = new ReportDetail(context, null);
        Report report = Report.create(identity, detail);
        shareUseCase.record(report);
    }

    private void present(OperationResult result) {
        if (view != null) {
            view.hideLoading();
            result.accept(new ReportResultStrategy(view));
        }
    }

    private List<Map<String, String>> map(List<Crop> crops) {
        CropMapping mapping = new CropMapping();
        List<Map<String, String>> mapped = new ArrayList<>();
        for (Crop crop : crops) {
            mapped.add(mapping.map(crop));
        }
        return mapped;
    }
}
