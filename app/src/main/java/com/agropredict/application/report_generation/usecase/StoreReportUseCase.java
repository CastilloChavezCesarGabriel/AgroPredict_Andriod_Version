package com.agropredict.application.report_generation.usecase;

import com.agropredict.application.repository.IReportRepository;
import com.agropredict.application.report_generation.request.ReportRequest;
import com.agropredict.application.report_generation.request.Destination;
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