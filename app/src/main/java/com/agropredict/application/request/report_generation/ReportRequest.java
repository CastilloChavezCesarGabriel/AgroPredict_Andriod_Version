package com.agropredict.application.request.report_generation;

import com.agropredict.domain.report.Report;
import com.agropredict.domain.report.IReportContextConsumer;
import com.agropredict.domain.report.IReportIdentityConsumer;
import com.agropredict.domain.report.IReportStorageConsumer;
import java.util.Objects;

public final class ReportRequest {
    private final Report report;
    private final Finding finding;

    public ReportRequest(Report report, Finding finding) {
        this.report = Objects.requireNonNull(report, "report request requires a report");
        this.finding = Objects.requireNonNull(finding, "report request requires a finding");
    }

    public void describe(IReportIdentityConsumer consumer) {
        report.describe(consumer);
    }

    public void link(IReportContextConsumer consumer) {
        finding.link(consumer);
    }

    public void store(IReportStorageConsumer consumer, Destination destination) {
        destination.store(consumer);
    }

    public void identify(IReportLinkConsumer consumer) {
        report.identify(reportIdentifier -> finding.pair(consumer, reportIdentifier));
    }
}
