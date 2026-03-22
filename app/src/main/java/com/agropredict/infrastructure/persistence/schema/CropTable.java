package com.agropredict.infrastructure.persistence.schema;

import android.database.sqlite.SQLiteDatabase;

public final class CropTable implements ITable {
    @Override
    public void create(SQLiteDatabase database) {
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS crop ("
            + "id TEXT PRIMARY KEY DEFAULT (lower(hex(randomblob(16)))), "
            + "user_id TEXT NOT NULL REFERENCES user(id) ON DELETE CASCADE, "
            + "soil_type_id TEXT REFERENCES soil_type(id) ON DELETE SET NULL, "
            + "phenological_stage_id TEXT REFERENCES phenological_stage(id) ON DELETE SET NULL, "
            + "field_name TEXT, "
            + "crop_type TEXT NOT NULL, "
            + "location TEXT, "
            + "area REAL, "
            + "planting_date TEXT, "
            + "is_active INTEGER DEFAULT 1 CHECK (is_active IN (0,1)), "
            + "created_at TEXT DEFAULT CURRENT_TIMESTAMP, "
            + "updated_at TEXT DEFAULT CURRENT_TIMESTAMP)");
        database.execSQL(
            "CREATE TRIGGER IF NOT EXISTS crop_updated "
            + "AFTER UPDATE ON crop FOR EACH ROW BEGIN "
            + "UPDATE crop SET updated_at = CURRENT_TIMESTAMP WHERE id = NEW.id; END");
        database.execSQL("CREATE INDEX IF NOT EXISTS index_crop_user ON crop(user_id)");
        database.execSQL("CREATE INDEX IF NOT EXISTS index_crop_type ON crop(crop_type)");
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS crop_history ("
            + "id TEXT PRIMARY KEY DEFAULT (lower(hex(randomblob(16)))), "
            + "crop_id TEXT NOT NULL REFERENCES crop(id) ON DELETE CASCADE, "
            + "field_modified TEXT, "
            + "old_value TEXT, "
            + "new_value TEXT, "
            + "modified_at TEXT DEFAULT CURRENT_TIMESTAMP)");
    }
}