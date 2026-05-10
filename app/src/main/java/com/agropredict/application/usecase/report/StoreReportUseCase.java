package com.agropredict.application.usecase.report;

import com.agropredict.application.repository.IReportRepository;
import com.agropredict.application.request.report_generation.ReportRequest;
import com.agropredict.application.request.report_generation.Destination;
import java.util.Objects;

public final class StoreReportUseCase {
    private final IReportRepository reportRepository;

    public StoreReportUseCase(IReportRepository reportRepository) {
        this.reportRepository = Objects.requireNonNull(reportRepository, "store report use case requires a report repository");
    }

    public void store(ReportRequest request, Destination destination) {
        reportRepository.store(request, destination);
    }
}