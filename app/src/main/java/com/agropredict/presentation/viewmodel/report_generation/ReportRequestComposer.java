package com.agropredict.presentation.viewmodel.report_generation;

import com.agropredict.application.request.report_generation.Finding;
import com.agropredict.application.request.report_generation.ReportRequest;
import com.agropredict.domain.entity.Report;
import com.agropredict.domain.visitor.diagnostic.IDiagnosticPairVisitor;

public final class ReportRequestComposer implements IDiagnosticPairVisitor {
    private final Report report;
    private final ReportOutcome outcome;

    public ReportRequestComposer(Report report, ReportOutcome outcome) {
        this.report = report;
        this.outcome = outcome;
    }

    @Override
    public void match(String diagnosticIdentifier, String otherIdentifier) {
        ReportRequest request = new ReportRequest(report, new Finding(diagnosticIdentifier, otherIdentifier));
        outcome.route(request);
    }
}
