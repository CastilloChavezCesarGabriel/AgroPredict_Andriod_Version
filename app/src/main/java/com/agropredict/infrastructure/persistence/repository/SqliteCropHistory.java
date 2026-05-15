package com.agropredict.infrastructure.persistence.repository;

import android.database.Cursor;
import com.agropredict.domain.identifier.IdentifierFactory;
import com.agropredict.infrastructure.persistence.database.Database;
import com.agropredict.infrastructure.persistence.database.SqliteRow;
import com.agropredict.infrastructure.persistence.database.SqliteRowFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class SqliteCropHistory {
    private static final String[] TRACKED = {"field_name", "soil_type_id", "phenological_stage_id"};

    private final Database database;
    private final SqliteRowFactory rowFactory;

    public SqliteCropHistory(Database database, SqliteRowFactory rowFactory) {
        this.database = Objects.requireNonNull(database, "crop history requires a database");
        this.rowFactory = Objects.requireNonNull(rowFactory, "crop history requires a row factory");
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
        for (String column : TRACKED) {
            String oldValue = previous.get(column);
            String newValue = current.get(column);
            if (matches(oldValue, newValue)) continue;
            SqliteRow row = rowFactory.open();
            row.record("id", IdentifierFactory.generate("crop_history"));
            row.record("crop_id", cropIdentifier);
            row.record("field_modified", column);
            row.record("old_value", oldValue);
            row.record("new_value", newValue);
            row.stamp("modified_at");
            row.flush("crop_history");
        }
    }

    private boolean matches(String oldValue, String newValue) {
        if (oldValue == null) return newValue == null;
        return oldValue.equals(newValue);
    }
}
