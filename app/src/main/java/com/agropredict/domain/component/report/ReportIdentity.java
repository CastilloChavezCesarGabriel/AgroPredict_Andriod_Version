package com.agropredict.domain.component.report;

import com.agropredict.domain.visitor.report.IReportIdentityVisitor;

public final class ReportIdentity {
    private final String identifier;
    private final String format;

    public ReportIdentity(String identifier, String format) {
        this.identifier = identifier;
        this.format = format;
    }

    public boolean isExportable() {
        return "PDF".equalsIgnoreCase(format) || "CSV".equalsIgnoreCase(format);
    }

    public void accept(IReportIdentityVisitor visitor) {
        visitor.visitIdentity(identifier, format);
    }
}