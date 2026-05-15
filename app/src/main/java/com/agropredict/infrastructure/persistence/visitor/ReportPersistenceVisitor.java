package com.agropredict.infrastructure.persistence.visitor;

import com.agropredict.domain.report.IReportContextConsumer;
import com.agropredict.domain.report.IReportIdentityConsumer;
import com.agropredict.domain.report.IReportStorageConsumer;
import com.agropredict.infrastructure.persistence.database.SqliteRow;

public final class ReportPersistenceVisitor implements
        IReportIdentityConsumer, IReportContextConsumer, IReportStorageConsumer {
    private final SqliteRow row;

    public ReportPersistenceVisitor(SqliteRow row) {
        this.row = row;
    }

    @Override
    public void describe(String identifier, String format) {
        row.record("id", identifier);
        row.record("format", format);
    }

    @Override
    public void link(String diagnosticIdentifier, String cropIdentifier) {
        row.record("diagnostic_id", diagnosticIdentifier);
        row.record("crop_id", cropIdentifier);
    }

    @Override
    public void store(String userIdentifier, String filePath) {
        row.record("user_id", userIdentifier);
        row.record("file_path", filePath);
    }
}
