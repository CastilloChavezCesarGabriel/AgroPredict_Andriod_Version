package com.agropredict.infrastructure.persistence;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import com.agropredict.application.service.IAuditLogger;

public final class AuditLogger implements IAuditLogger {
    private final Database database;

    public AuditLogger(Database database) {
        this.database = database;
    }

    @Override
    public void log(String userIdentifier, String action) {
        SQLiteDatabase writable = database.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", userIdentifier);
        values.put("log_action", action);
        writable.insert("log_entry", null, values);
    }
}