package com.agropredict.presentation.viewmodel.report_generation;

import com.agropredict.application.request.report_generation.ReportRequest;

public interface IReportPersister {
    void persist(ReportRequest request, String filePath);
    void reject();
}