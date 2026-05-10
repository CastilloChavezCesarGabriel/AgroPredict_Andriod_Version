package com.agropredict.application.repository;

import com.agropredict.application.request.report_generation.ReportRequest;
import com.agropredict.application.request.report_generation.Destination;

public interface IReportRepository {
    void store(ReportRequest request, Destination destination);
}
