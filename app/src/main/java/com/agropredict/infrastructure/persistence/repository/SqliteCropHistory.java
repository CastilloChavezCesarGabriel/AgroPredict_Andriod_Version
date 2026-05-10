package com.agropredict.infrastructure.persistence.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.agropredict.domain.identifier.IdentifierFactory;
import com.agropredict.infrastructure.persistence.database.Clock;
import com.agropredict.infrastructure.persistence.database.Database;
import java.util.HashMap;
import java.util.Map;

public final class SqliteCropHistory {
    private static final String[] TRACKED = {"field_name", "soil_type_id", "phenological_stage_id"};

    private final Database database;

    public SqliteCropHistory(Database database) {
        this.database = database;
    }

    public Map<String, String> snapshot(String cropIdentifier) {
        Map<String, String> previous = new HashMap<>();
        Cursor cursor = database.getReadableDatabase().rawQuery(
                "SELECT field_name, soil_type_id, phenological_stage_id FROM crop WHERE id = ?",
                new String[]{cropIdentifier});
        if (cursor.moveToFirst()) {
            for (String column : TRACKED) {
                previous.put(column, cursor.getString(cursor.getColumnIndexOrThrow(column)));
            }
        }
        cursor.close();
        return previous;
    }

    public void track(String cropIdentifier, Map<String, String> previous) {
        Map<String, String> current = snapshot(cropIdentifier);
        SQLiteDatabase writable = database.getWritableDatabase();
        String now = Clock.read();
        for (String column : TRACKED) {
            String oldValue = previous.get(column);
            String newValue = current.get(column);
            if (matches(oldValue, newValue)) continue;
            ContentValues row = new ContentValues();
            row.put("id", IdentifierFactory.generate("crop_history"));
            row.put("crop_id", cropIdentifier);
            row.put("field_modified", column);
            row.put("old_value", oldValue);
            row.put("new_value", newValue);
            row.put("modified_at", now);
            writable.insert("crop_history", null, row);
        }
    }

    private boolean matches(String oldValue, String newValue) {
        if (oldValue == null) return newValue == null;
        return oldValue.equals(newValue);
    }
}