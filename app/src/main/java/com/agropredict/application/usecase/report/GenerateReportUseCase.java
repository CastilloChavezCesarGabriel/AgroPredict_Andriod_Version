package com.agropredict.application.usecase.report;

import com.agropredict.application.result.OperationResult;
import com.agropredict.application.service.IReportGeneratorService;
import java.util.Map;

public final class GenerateReportUseCase {
    private final IReportGeneratorService reportGenerator;

    public GenerateReportUseCase(IReportGeneratorService reportGenerator) {
        this.reportGenerator = reportGenerator;
    }

    public OperationResult generate(Map<String, Object> reportData) {
        return reportGenerator.generate(reportData);
    }
}