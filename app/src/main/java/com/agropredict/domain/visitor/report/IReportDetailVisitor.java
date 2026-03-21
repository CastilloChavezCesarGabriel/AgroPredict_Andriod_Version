package com.agropredict.domain.visitor.report;

import com.agropredict.domain.component.report.ReportContext;
import com.agropredict.domain.component.report.ReportStorage;

public interface IReportDetailVisitor {
    void visit(ReportContext context, ReportStorage storage);
}
