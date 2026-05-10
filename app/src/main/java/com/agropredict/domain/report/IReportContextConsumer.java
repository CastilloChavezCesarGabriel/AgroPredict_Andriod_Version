package com.agropredict.domain.report;

public interface IReportContextConsumer {
    void link(String diagnosticIdentifier, String cropIdentifier);
}