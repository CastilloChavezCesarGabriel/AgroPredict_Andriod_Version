package com.agropredict.presentation.viewmodel.report_generation;

import com.agropredict.application.operation_result.OperationResult;
import com.agropredict.application.request.report_generation.ReportRequest;

public final class ReportOutcome {
    private final OperationResult result;
    private final ReportExporter persister;

    public ReportOutcome(OperationResult result, ReportExporter persister) {
        this.result = result;
        this.persister = persister;
    }

    public void route(ReportRequest request) {
        result.accept(new ReportOutcomeRouter(request, persister));
    }
}
