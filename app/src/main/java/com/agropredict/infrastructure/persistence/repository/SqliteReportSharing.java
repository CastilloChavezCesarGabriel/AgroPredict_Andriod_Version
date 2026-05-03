package com.agropredict.infrastructure.persistence.repository;

import android.content.ContentValues;
import com.agropredict.domain.Identifier;
import com.agropredict.infrastructure.persistence.database.Database;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public final class SqliteReportSharing {
    private static final int DURATION_DAYS = 30;
    private final Database database;

    public SqliteReportSharing(Database database) {
        this.database = database;
    }

    public void share(String reportIdentifier, String generatedAt) {
        ContentValues values = new ContentValues();
        values.put("id", Identifier.generate("report_sharing"));
        values.put("report_id", reportIdentifier);
        values.put("qr_code", token());
        values.put("created_at", generatedAt);
        values.put("expiration", expiration());
        database.getWritableDatabase().insert("report_sharing", null, values);
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