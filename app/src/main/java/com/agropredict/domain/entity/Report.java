package com.agropredict.domain.entity;

import com.agropredict.domain.value.report.ReportDetail;
import com.agropredict.domain.value.report.ReportIdentity;
import com.agropredict.domain.visitor.IReportVisitor;

public final class Report {
    private final ReportIdentity identity;
    private final ReportDetail detail;

    private Report(ReportIdentity identity, ReportDetail detail) {
        this.identity = identity;
        this.detail = detail;
    }

    public static Report create(ReportIdentity identity, ReportDetail detail) {
        return new Report(identity, detail);
    }

    public void accept(IReportVisitor visitor) {
        visitor.visit(identity, detail);
    }
}