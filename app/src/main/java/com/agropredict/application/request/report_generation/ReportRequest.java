package com.agropredict.application.request.report_generation;

import com.agropredict.domain.entity.Report;
import com.agropredict.domain.visitor.report.IReportVisitor;

public final class ReportRequest {
    private final Report report;
    private final Finding finding;

    public ReportRequest(Report report, Finding finding) {
        this.report = report;
        this.finding = finding;
    }

    public void accept(IReportVisitor visitor, Destination destination) {
        report.accept(visitor);
        finding.accept(visitor);
        destination.accept(visitor);
    }
}