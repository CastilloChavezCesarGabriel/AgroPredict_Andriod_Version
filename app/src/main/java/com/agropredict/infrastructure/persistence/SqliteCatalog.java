package com.agropredict.infrastructure.persistence;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.agropredict.application.repository.ICatalogRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class SqliteCatalog implements ICatalogRepository {
    private static final List<String> ALLOWED_TABLES = Arrays.asList(
            "soil_type", "phenological_stage", "occupation");
    private final DatabaseHelper databaseHelper;
    private final String tableName;

    public SqliteCatalog(DatabaseHelper databaseHelper, String tableName) {
        if (!ALLOWED_TABLES.contains(tableName))
            throw new IllegalArgumentException("Invalid catalog table: " + tableName);
        this.databaseHelper = databaseHelper;
        this.tableName = tableName;
    }

    @Override
    public List<String> list() {
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT name FROM " + tableName + " ORDER BY name", null);
        List<String> names = new ArrayList<>();
        while (cursor.moveToNext()) names.add(cursor.getString(0));
        cursor.close();
        return names;
    }

    @Override
    public String resolve(String name) {
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery(
                "SELECT id FROM " + tableName + " WHERE name = ? LIMIT 1",
                new String[]{name});
        String identifier = cursor.moveToFirst() ? cursor.getString(0) : null;
        cursor.close();
        return identifier;
    }
}