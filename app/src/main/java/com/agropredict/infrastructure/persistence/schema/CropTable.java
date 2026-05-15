package com.agropredict.infrastructure.persistence.schema;

import android.database.sqlite.SQLiteDatabase;

public final class CropTable implements ITable {
    @Override
    public void create(SQLiteDatabase database) {
        define(database);
        index(database);
    }

    private void define(SQLiteDatabase database) {
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS crop ("
            + "id TEXT PRIMARY KEY, "
            + "user_id TEXT NOT NULL REFERENCES user(id) ON DELETE CASCADE, "
            + "soil_type_id TEXT REFERENCES soil_type(id) ON DELETE SET NULL, "
            + "phenological_stage_id TEXT REFERENCES phenological_stage(id) ON DELETE SET NULL, "
            + "field_name TEXT, "
            + "crop_type TEXT NOT NULL, "
            + "location TEXT NOT NULL, "
            + "area REAL NOT NULL, "
            + "planting_date TEXT NOT NULL, "
            + "is_active INTEGER NOT NULL, "
            + "created_at TEXT NOT NULL, "
            + "updated_at TEXT NOT NULL)");
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS crop_history ("
            + "id TEXT PRIMARY KEY, "
            + "crop_id TEXT NOT NULL REFERENCES crop(id) ON DELETE CASCADE, "
            + "field_modified TEXT, "
            + "old_value TEXT, "
            + "new_value TEXT, "
            + "modified_at TEXT NOT NULL)");
    }

    private void index(SQLiteDatabase database) {
        database.execSQL("CREATE INDEX IF NOT EXISTS index_crop_user ON crop(user_id)");
        database.execSQL("CREATE INDEX IF NOT EXISTS index_crop_type ON crop(crop_type)");
    }
}
