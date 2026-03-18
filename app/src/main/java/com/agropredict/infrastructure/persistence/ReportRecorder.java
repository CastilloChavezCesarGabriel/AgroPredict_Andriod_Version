package com.agropredict.infrastructure.persistence;

import android.content.ContentValues;
import com.agropredict.domain.value.report.ReportContext;
import com.agropredict.domain.value.report.ReportDetail;
import com.agropredict.domain.value.report.ReportIdentity;
import com.agropredict.domain.value.report.ReportStorage;
import com.agropredict.domain.visitor.IReportContextVisitor;
import com.agropredict.domain.visitor.IReportDetailVisitor;
import com.agropredict.domain.visitor.IReportIdentityVisitor;
import com.agropredict.domain.visitor.IReportStorageVisitor;
import com.agropredict.domain.visitor.IReportVisitor;

public final class ReportRecorder implements IReportVisitor, IReportDetailVisitor,
        IReportIdentityVisitor, IReportContextVisitor, IReportStorageVisitor {

    private final ContentValues values;

    public ReportRecorder(ContentValues values) {
        this.values = values;
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
        values.put("id", identifier);
        values.put("format", format);
    }

    @Override
    public void visitReportContext(String diagnosticIdentifier, String cropIdentifier) {
        values.put("diagnostic_id", diagnosticIdentifier);
        values.put("crop_id", cropIdentifier);
    }

    @Override
    public void visitStorage(String userIdentifier, String filePath) {
        values.put("user_id", userIdentifier);
        values.put("file_path", filePath);
    }
}
