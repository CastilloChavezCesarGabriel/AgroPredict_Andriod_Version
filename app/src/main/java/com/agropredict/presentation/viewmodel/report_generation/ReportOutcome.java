package com.agropredict.presentation.viewmodel.report_generation;

import com.agropredict.application.operation_result.IUseCaseResult;
import com.agropredict.application.report_generation.request.ReportRequest;
import com.agropredict.application.service.ReportFormat;
import java.util.Objects;

public final class ReportOutcome {
    private final IUseCaseResult result;
    private final ReportExporter persister;
    private final ReportFormat format;

    public ReportOutcome(IUseCaseResult result, ReportExporter persister, ReportFormat format) {
        this.result = Objects.requireNonNull(result, "report outcome requires a result");
        this.persister = Objects.requireNonNull(persister, "report outcome requires a persister");
        this.format = Objects.requireNonNull(format, "report outcome requires a format");
    }

    public void route(ReportRequest request) {
        result.accept(new ReportOutcomeRouter(request, persister, format));
    }
}
