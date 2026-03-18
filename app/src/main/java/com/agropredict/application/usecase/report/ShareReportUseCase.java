package com.agropredict.application.usecase.report;

import com.agropredict.application.repository.IReportRepository;
import com.agropredict.domain.entity.Report;

public final class ShareReportUseCase {
    private final IReportRepository reportRepository;

    public ShareReportUseCase(IReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public void record(Report report) {
        reportRepository.store(report);
    }
}