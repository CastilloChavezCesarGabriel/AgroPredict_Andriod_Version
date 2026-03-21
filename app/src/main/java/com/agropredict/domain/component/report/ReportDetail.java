package com.agropredict.domain.component.report;

import com.agropredict.domain.visitor.report.IReportDetailVisitor;

public final class ReportDetail {
    private final ReportContext context;
    private final ReportStorage storage;

    public ReportDetail(ReportContext context, ReportStorage storage) {
        this.context = context;
        this.storage = storage;
    }

    public void accept(IReportDetailVisitor visitor) {
        visitor.visit(context, storage);
    }
}