package com.agropredict.application.usecase.report;

import com.agropredict.application.operation_result.IUseCaseResult;
import com.agropredict.application.service.IReportService;
import com.agropredict.domain.crop.Crop;
import com.agropredict.domain.diagnostic.Diagnostic;
import java.util.Objects;

public final class GenerateReportUseCase {
    private final IReportService reportService;

    public GenerateReportUseCase(IReportService reportService) {
        this.reportService = Objects.requireNonNull(reportService, "generate report use case requires a report service");
    }

    public IUseCaseResult generate(Crop crop, Diagnostic diagnostic) {
        return reportService.generate(crop, diagnostic);
    }
}