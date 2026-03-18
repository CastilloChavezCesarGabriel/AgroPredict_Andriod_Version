package com.agropredict.domain.value.report;

import com.agropredict.domain.visitor.IReportIdentityVisitor;

public final class ReportIdentity {
    private final String identifier;
    private final String format;

    public ReportIdentity(String identifier, String format) {
        this.identifier = identifier;
        this.format = format;
    }

    public void accept(IReportIdentityVisitor visitor) {
        visitor.visitIdentity(identifier, format);
    }
}