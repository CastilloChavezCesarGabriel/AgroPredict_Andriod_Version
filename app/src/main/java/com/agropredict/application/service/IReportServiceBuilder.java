package com.agropredict.application.service;

public interface IReportServiceBuilder {
    IReportService build(ReportFormat format, String directoryPath);
}