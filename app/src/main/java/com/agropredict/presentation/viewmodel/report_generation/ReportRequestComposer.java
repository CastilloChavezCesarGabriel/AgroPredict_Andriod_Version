package com.agropredict.presentation.viewmodel.report_generation;

import com.agropredict.application.operation_result.OperationResult;
import com.agropredict.application.request.report_generation.Finding;
import com.agropredict.application.request.report_generation.ReportRequest;
import com.agropredict.domain.entity.Report;
import com.agropredict.domain.visitor.diagnostic.IDiagnosticPairVisitor;

public final class ReportRequestComposer implements IDiagnosticPairVisitor {
    private final Report report;
    private final OperationResult result;
    private final ReportExporter persister;

    public ReportRequestComposer(Report report, OperationResult result, ReportExporter persister) {
        this.report = report;
        this.result = result;
        this.persister = persister;
    }

    @Override
    public void match(String diagnosticIdentifier, String otherIdentifier) {
        ReportRequest request = new ReportRequest(report, new Finding(diagnosticIdentifier, otherIdentifier));
        result.accept(new ReportOutcomeRouter(request, persister));
    }
}