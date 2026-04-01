package com.agropredict.application.usecase.report;

import com.agropredict.application.operation_result.OperationResult;
import com.agropredict.application.service.IReportService;
import com.agropredict.domain.entity.Crop;
import com.agropredict.domain.entity.Diagnostic;

public final class GenerateReportUseCase {
    private final IReportService reportGenerator;

    public GenerateReportUseCase(IReportService reportGenerator) {
        this.reportGenerator = reportGenerator;
    }

    public OperationResult generate(Crop crop, Diagnostic diagnostic) {
        return reportGenerator.generate(crop, diagnostic);
    }
}