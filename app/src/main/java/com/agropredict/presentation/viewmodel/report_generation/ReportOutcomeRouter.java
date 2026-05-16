package com.agropredict.presentation.viewmodel.report_generation;

import com.agropredict.application.report_generation.request.ReportRequest;
import com.agropredict.application.operation_result.IOperationResult;
import com.agropredict.application.service.ReportFormat;
import java.util.Objects;

public final class ReportOutcomeRouter implements IOperationResult {
    private final ReportRequest request;
    private final ReportExporter persister;
    private final ReportFormat format;

    public ReportOutcomeRouter(ReportRequest request, ReportExporter persister, ReportFormat format) {
        this.request = Objects.requireNonNull(request, "report outcome router requires a request");
        this.persister = Objects.requireNonNull(persister, "report outcome router requires a persister");
        this.format = Objects.requireNonNull(format, "report outcome router requires a format");
    }

    @Override
    public void onSucceed(String filePath) {
        persister.persist(request, filePath, format);
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
