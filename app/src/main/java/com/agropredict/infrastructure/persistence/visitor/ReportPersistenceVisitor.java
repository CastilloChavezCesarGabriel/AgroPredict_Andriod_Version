package com.agropredict.infrastructure.persistence.visitor;

import com.agropredict.domain.component.report.ReportContext;
import com.agropredict.domain.component.report.ReportDetail;
import com.agropredict.domain.component.report.ReportIdentity;
import com.agropredict.domain.component.report.ReportStorage;
import com.agropredict.domain.visitor.report.IReportContextVisitor;
import com.agropredict.domain.visitor.report.IReportDetailVisitor;
import com.agropredict.domain.visitor.report.IReportIdentityVisitor;
import com.agropredict.domain.visitor.report.IReportStorageVisitor;
import com.agropredict.domain.visitor.report.IReportVisitor;
import com.agropredict.infrastructure.persistence.IRow;

public final class ReportPersistenceVisitor implements IReportVisitor, IReportDetailVisitor,
        IReportIdentityVisitor, IReportContextVisitor, IReportStorageVisitor {

    private final IRow row;

    public ReportPersistenceVisitor(IRow row) {
        this.row = row;
    }

    @Override
    public void visit(ReportIdentity identity, ReportDetail detail) {
        identity.accept(this);
        if (detail != null) detail.accept(this);
    }

    @Override
    public void visit(ReportContext context, ReportStorage storage) {
        if (context != null) context.accept(this);
        if (storage != null) storage.accept(this);
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
