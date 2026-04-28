package com.agropredict.application.factory;

import com.agropredict.application.repository.ICropRepository;
import com.agropredict.application.repository.IDiagnosticRepository;
import com.agropredict.application.repository.IReportRepository;
import com.agropredict.application.repository.ISessionRepository;
import com.agropredict.application.service.IReportService;

public interface IReportingFactory {
    ICropRepository createCropRepository();
    IDiagnosticRepository createDiagnosticRepository();
    IReportRepository createReportRepository();
    IReportService createReportService(String format);
    ISessionRepository createSessionRepository();
}