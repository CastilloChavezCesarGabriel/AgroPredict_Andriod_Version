package com.agropredict.infrastructure.persistence.schema;

import android.database.sqlite.SQLiteDatabase;

public final class DiagnosticSummaryView implements ITable {
    @Override
    public void create(SQLiteDatabase database) {
        database.execSQL(
            "CREATE VIEW IF NOT EXISTS diagnostic_summary AS "
            + "SELECT diagnostic.id AS diagnostic_id, diagnostic.crop_id, "
            + "crop.crop_type, diagnostic.predicted_crop, diagnostic.confidence, "
            + "diagnostic.short_summary, diagnostic.created_at, "
            + "user.id AS user_id, user.username "
            + "FROM diagnostic "
            + "LEFT JOIN crop ON diagnostic.crop_id = crop.id "
            + "LEFT JOIN user ON diagnostic.user_id = user.id "
            + "ORDER BY diagnostic.created_at DESC");
    }
}