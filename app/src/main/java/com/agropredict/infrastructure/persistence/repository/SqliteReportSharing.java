package com.agropredict.infrastructure.persistence.repository;

import com.agropredict.domain.identifier.IdentifierFactory;
import com.agropredict.infrastructure.persistence.database.SqliteRow;
import com.agropredict.infrastructure.persistence.database.SqliteRowFactory;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

public final class SqliteReportSharing {
    private static final int DURATION_DAYS = 30;
    private final SqliteRowFactory rowFactory;

    public SqliteReportSharing(SqliteRowFactory rowFactory) {
        this.rowFactory = Objects.requireNonNull(rowFactory, "report sharing requires a row factory");
    }

    public void share(String reportIdentifier) {
        SqliteRow row = rowFactory.open();
        row.record("id", IdentifierFactory.generate("report_sharing"));
        row.record("report_id", reportIdentifier);
        row.record("qr_code", token());
        row.stamp("created_at");
        row.record("expiration", expiration());
        row.flush("report_sharing");
    }

    private String token() {
        byte[] bytes = new byte[16];
        new SecureRandom().nextBytes(bytes);
        StringBuilder builder = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) builder.append(String.format(Locale.US, "%02x", b));
        return builder.toString();
    }

    private String expiration() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.add(Calendar.DAY_OF_MONTH, DURATION_DAYS);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        return formatter.format(calendar.getTime());
    }
}
