package com.agropredict.infrastructure.persistence;

import com.agropredict.domain.component.report.ReportContext;
import com.agropredict.domain.component.report.ReportDetail;
import com.agropredict.domain.component.report.ReportIdentity;
import com.agropredict.domain.component.report.ReportStorage;
import com.agropredict.domain.visitor.report.IReportContextVisitor;
import com.agropredict.domain.visitor.report.IReportDetailVisitor;
import com.agropredict.domain.visitor.report.IReportIdentityVisitor;
import com.agropredict.domain.visitor.report.IReportStorageVisitor;
import com.agropredict.domain.visitor.report.IReportVisitor;

public final class ReportRecorder implements IReportVisitor, IReportDetailVisitor,
        IReportIdentityVisitor, IReportContextVisitor, IReportStorageVisitor {

    private final IRecord record;

    public ReportRecorder(IRecord record) {
        this.record = record;
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
        record.record("id", identifier);
        record.record("format", format);
    }

    @Override
    public void visitContext(String diagnosticIdentifier, String cropIdentifier) {
        record.record("diagnostic_id", diagnosticIdentifier);
        record.record("crop_id", cropIdentifier);
    }

    @Override
    public void visitStorage(String userIdentifier, String filePath) {
        record.record("user_id", userIdentifier);
        record.record("file_path", filePath);
    }
}
