package com.agropredict.application.repository;

import com.agropredict.application.report_generation.request.ReportRequest;
import com.agropredict.application.report_generation.request.Destination;

public interface IReportRepository {
    void store(ReportRequest request, Destination destination);
}
