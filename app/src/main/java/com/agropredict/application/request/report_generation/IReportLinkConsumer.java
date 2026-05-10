package com.agropredict.application.request.report_generation;

public interface IReportLinkConsumer {
    void pair(String reportIdentifier, String diagnosticIdentifier);
}
