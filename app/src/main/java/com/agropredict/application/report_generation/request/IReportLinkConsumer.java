package com.agropredict.application.report_generation.request;

public interface IReportLinkConsumer {
    void pair(String reportIdentifier, String diagnosticIdentifier);
}
