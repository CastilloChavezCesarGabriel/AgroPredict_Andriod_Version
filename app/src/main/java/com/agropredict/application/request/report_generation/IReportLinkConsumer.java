package com.agropredict.application.request.report_generation;

public interface IReportLinkConsumer {
    void accept(String reportIdentifier, String diagnosticIdentifier);
}
