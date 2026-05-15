package com.agropredict.presentation.viewmodel.report_generation;

import com.agropredict.application.crop_management.usecase.FindCropUseCase;
import com.agropredict.application.diagnostic_history.ResolveDiagnosticUseCase;
import com.agropredict.application.report_generation.request.Destination;
import com.agropredict.application.report_generation.request.ReportRequest;
import com.agropredict.application.report_generation.usecase.StoreReportUseCase;
import com.agropredict.domain.crop.Crop;
import com.agropredict.domain.diagnostic.Diagnostic;
import java.util.Objects;

public final class ReportingData {
    private final FindCropUseCase findCrop;
    private final ResolveDiagnosticUseCase resolveDiagnostic;
    private final StoreReportUseCase storeReport;

    public ReportingData(FindCropUseCase findCrop, ResolveDiagnosticUseCase resolveDiagnostic, StoreReportUseCase storeReport) {
        this.findCrop = Objects.requireNonNull(findCrop, "reporting data requires a find crop use case");
        this.resolveDiagnostic = Objects.requireNonNull(resolveDiagnostic, "reporting data requires a resolve diagnostic use case");
        this.storeReport = Objects.requireNonNull(storeReport, "reporting data requires a store report use case");
    }

    public Crop find(String cropIdentifier) {
        return findCrop.find(cropIdentifier);
    }

    public Diagnostic resolve(String userIdentifier, String cropIdentifier) {
        return resolveDiagnostic.resolve(userIdentifier, cropIdentifier);
    }

    public void store(ReportRequest request, Destination destination) {
        storeReport.store(request, destination);
    }
}
