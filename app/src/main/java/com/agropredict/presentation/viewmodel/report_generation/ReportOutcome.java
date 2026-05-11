package com.agropredict.presentation.viewmodel.report_generation;

import com.agropredict.application.operation_result.IUseCaseResult;
import com.agropredict.application.report_generation.request.ReportRequest;

public final class ReportOutcome {
    private final IUseCaseResult result;
    private final ReportExporter persister;

    public ReportOutcome(IUseCaseResult result, ReportExporter persister) {
        this.result = result;
        this.persister = persister;
    }

    public void route(ReportRequest request) {
        result.accept(new ReportOutcomeRouter(request, persister));
    }
}
