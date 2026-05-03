package com.agropredict.infrastructure.persistence.schema;

import android.database.sqlite.SQLiteDatabase;

public final class ImageTable implements ITable {
    @Override
    public void create(SQLiteDatabase database) {
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS image ("
            + "id TEXT PRIMARY KEY, "
            + "crop_id TEXT REFERENCES crop(id) ON DELETE SET NULL, "
            + "user_id TEXT REFERENCES user(id) ON DELETE SET NULL, "
            + "file_path TEXT NOT NULL, "
            + "format TEXT CHECK (format IN ('jpg','png')), "
            + "size_in_megabytes REAL, "
            + "captured_at TEXT, "
            + "created_at TEXT NOT NULL)");
        database.execSQL("CREATE INDEX IF NOT EXISTS index_image_crop ON image(crop_id)");
        database.execSQL("CREATE INDEX IF NOT EXISTS index_image_user ON image(user_id)");
    }
}