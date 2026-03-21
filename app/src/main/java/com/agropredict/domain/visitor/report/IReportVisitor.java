package com.agropredict.domain.visitor.report;

import com.agropredict.domain.component.report.ReportDetail;
import com.agropredict.domain.component.report.ReportIdentity;

public interface IReportVisitor {
    void visit(ReportIdentity identity, ReportDetail detail);
}
