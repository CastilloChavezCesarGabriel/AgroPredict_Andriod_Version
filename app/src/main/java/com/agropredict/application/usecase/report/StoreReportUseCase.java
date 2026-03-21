package com.agropredict.application.usecase.report;

import com.agropredict.application.repository.IReportRepository;
import com.agropredict.application.request.ReportRequest;

public final class StoreReportUseCase {
    private final IReportRepository reportRepository;

    public StoreReportUseCase(IReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public void store(ReportRequest request) {
        reportRepository.store(request.compile());
    }
}
