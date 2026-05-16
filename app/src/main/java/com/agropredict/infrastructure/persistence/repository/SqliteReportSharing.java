package com.agropredict.infrastructure.persistence.repository;

import com.agropredict.domain.identifier.IdentifierFactory;
import com.agropredict.infrastructure.persistence.database.SqliteRow;
import com.agropredict.infrastructure.persistence.database.SqliteRowFactory;
import com.agropredict.infrastructure.persistence.report.QrCodeToken;
import com.agropredict.infrastructure.persistence.report.ReportExpiration;

import java.util.Objects;

public final class SqliteReportSharing {
    private final SqliteRowFactory rowFactory;
    private final QrCodeToken qrCode;
    private final ReportExpiration expiration;

    public SqliteReportSharing(SqliteRowFactory rowFactory) {
        this.rowFactory = Objects.requireNonNull(rowFactory, "report sharing requires a row factory");
        this.qrCode = new QrCodeToken();
        this.expiration = new ReportExpiration();
    }

    public void share(String reportIdentifier) {
        SqliteRow row = rowFactory.open();
        row.record("id", IdentifierFactory.generate("report_sharing"));
        row.record("report_id", reportIdentifier);
        row.record("qr_code", qrCode.generate());
        row.stamp("created_at");
        row.imprint("expiration", expiration.compute());
        row.flush("report_sharing");
    }
}