package com.agropredict.domain.component.report;

import com.agropredict.domain.visitor.report.IReportContextVisitor;

public final class ReportContext {
    private final String diagnosticIdentifier;
    private final String cropIdentifier;

    public ReportContext(String diagnosticIdentifier, String cropIdentifier) {
        this.diagnosticIdentifier = diagnosticIdentifier;
        this.cropIdentifier = cropIdentifier;
    }

    public void accept(IReportContextVisitor visitor) {
        visitor.visitContext(diagnosticIdentifier, cropIdentifier);
    }
}