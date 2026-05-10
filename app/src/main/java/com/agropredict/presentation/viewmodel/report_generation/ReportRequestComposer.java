package com.agropredict.presentation.viewmodel.report_generation;

import com.agropredict.application.request.report_generation.Finding;
import com.agropredict.application.request.report_generation.ReportRequest;
import com.agropredict.domain.report.Report;
import com.agropredict.domain.diagnostic.visitor.IDiagnosticPairConsumer;

public final class ReportRequestComposer implements IDiagnosticPairConsumer {
    private final Report report;
    private final ReportOutcome outcome;

    public ReportRequestComposer(Report report, ReportOutcome outcome) {
        this.report = report;
        this.outcome = outcome;
    }

    @Override
    public void pair(String identifier, String otherIdentifier) {
        Finding finding = new Finding(identifier, otherIdentifier);
        ReportRequest request = new ReportRequest(report, finding);
        outcome.route(request);
    }
}
