package com.agropredict.presentation.viewmodel.report_generation;

import com.agropredict.application.request.report_generation.ReportRequest;
import com.agropredict.application.visitor.IOperationResult;

public final class ReportOutcomeRouter implements IOperationResult {
    private final ReportRequest request;
    private final ReportExporter persister;

    public ReportOutcomeRouter(ReportRequest request, ReportExporter persister) {
        this.request = request;
        this.persister = persister;
    }

    @Override
    public void onSucceed(String filePath) {
        persister.persist(request, filePath);
    }

    @Override
    public void onFail() {
        persister.reject();
    }

    @Override
    public void onReject(String reason) {
        persister.reject();
    }
}