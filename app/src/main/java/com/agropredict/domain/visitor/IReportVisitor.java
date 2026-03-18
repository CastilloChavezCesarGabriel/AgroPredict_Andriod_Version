package com.agropredict.domain.visitor;

import com.agropredict.domain.value.report.ReportDetail;
import com.agropredict.domain.value.report.ReportIdentity;

public interface IReportVisitor {
    void visit(ReportIdentity identity, ReportDetail detail);
}
