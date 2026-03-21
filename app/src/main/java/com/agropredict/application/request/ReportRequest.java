package com.agropredict.application.request;

import com.agropredict.domain.component.report.ReportDetail;
import com.agropredict.domain.component.report.ReportIdentity;
import com.agropredict.domain.entity.Report;

public final class ReportRequest {
    private final ReportIdentity identity;
    private final ReportDetail detail;

    public ReportRequest(ReportIdentity identity, ReportDetail detail) {
        this.identity = identity;
        this.detail = detail;
    }

    public Report compile() {
        return Report.create(identity, detail);
    }
}
