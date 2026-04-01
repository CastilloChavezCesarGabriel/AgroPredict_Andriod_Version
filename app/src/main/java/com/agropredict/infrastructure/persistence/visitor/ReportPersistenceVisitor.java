package com.agropredict.infrastructure.persistence.visitor;

import com.agropredict.domain.visitor.report.IReportVisitor;
import com.agropredict.infrastructure.persistence.database.IRow;

public final class ReportPersistenceVisitor implements IReportVisitor {
    private final IRow row;

    public ReportPersistenceVisitor(IRow row) {
        this.row = row;
    }

    @Override
    public void visitIdentity(String identifier, String format) {
        row.record("id", identifier);
        row.record("format", format);
    }

    @Override
    public void visitContext(String diagnosticIdentifier, String cropIdentifier) {
        row.record("diagnostic_id", diagnosticIdentifier);
        row.record("crop_id", cropIdentifier);
    }

    @Override
    public void visitStorage(String userIdentifier, String filePath) {
        row.record("user_id", userIdentifier);
        row.record("file_path", filePath);
    }
}