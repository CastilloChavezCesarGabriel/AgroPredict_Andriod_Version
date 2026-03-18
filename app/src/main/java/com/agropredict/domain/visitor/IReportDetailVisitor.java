package com.agropredict.domain.visitor;

import com.agropredict.domain.value.report.ReportContext;
import com.agropredict.domain.value.report.ReportStorage;

public interface IReportDetailVisitor {

    void visit(ReportContext context, ReportStorage storage);
}
