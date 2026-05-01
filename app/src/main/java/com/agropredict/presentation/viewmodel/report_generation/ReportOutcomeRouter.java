package com.agropredict.presentation.viewmodel.report_generation;

import com.agropredict.application.request.report_generation.ReportRequest;
import com.agropredict.application.visitor.IOperationResultVisitor;

public final class ReportOutcomeRouter implements IOperationResultVisitor {
    private final ReportRequest request;
    private final ReportExporter persister;

    public ReportOutcomeRouter(ReportRequest request, ReportExporter persister) {
        this.request = request;
        this.persister = persister;
    }

    @Override
    public void visit(boolean completed, String filePath) {
        if (completed) persister.persist(request, filePath);
        else persister.reject();
    }
}