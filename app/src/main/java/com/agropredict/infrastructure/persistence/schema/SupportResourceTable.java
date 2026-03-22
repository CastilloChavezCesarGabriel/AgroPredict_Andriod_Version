package com.agropredict.infrastructure.persistence.schema;

import android.database.sqlite.SQLiteDatabase;

public final class SupportResourceTable implements ITable {
    @Override
    public void create(SQLiteDatabase database) {
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS support_resource ("
            + "id TEXT PRIMARY KEY DEFAULT (lower(hex(randomblob(16)))), "
            + "title TEXT NOT NULL, "
            + "resource_type TEXT NOT NULL CHECK (resource_type IN ('pdf', 'link')), "
            + "path_or_url TEXT NOT NULL, "
            + "description TEXT, "
            + "position INTEGER DEFAULT 0)");
    }
}