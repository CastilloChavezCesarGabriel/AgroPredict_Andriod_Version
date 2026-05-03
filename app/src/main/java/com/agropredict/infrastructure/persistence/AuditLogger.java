package com.agropredict.infrastructure.persistence;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import com.agropredict.application.service.IAuditLogger;
import com.agropredict.domain.Identifier;
import com.agropredict.infrastructure.persistence.database.Clock;
import com.agropredict.infrastructure.persistence.database.Database;

public final class AuditLogger implements IAuditLogger {
    private final Database database;

    public AuditLogger(Database database) {
        this.database = database;
    }

    @Override
    public void log(String userIdentifier, String action) {
        SQLiteDatabase writable = database.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", Identifier.generate("log"));
        values.put("user_id", userIdentifier);
        values.put("log_action", action);
        values.put("description", action);
        values.put("created_at", Clock.read());
        writable.insert("log_entry", null, values);
    }
}