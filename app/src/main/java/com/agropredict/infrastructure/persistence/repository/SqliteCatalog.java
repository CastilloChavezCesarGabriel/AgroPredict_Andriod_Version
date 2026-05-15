package com.agropredict.infrastructure.persistence.repository;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.agropredict.application.repository.ICatalogRepository;
import com.agropredict.domain.guard.ArgumentPrecondition;
import com.agropredict.infrastructure.persistence.database.Database;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class SqliteCatalog implements ICatalogRepository {
    private final Database database;
    private final String tableName;

    public SqliteCatalog(Database database, String tableName) {
        this.database = Objects.requireNonNull(database, "sqlite catalog requires a database");
        this.tableName = ArgumentPrecondition.validate(tableName, "sqlite catalog table name");
    }

    @Override
    public List<String> list() {
        SQLiteDatabase database = this.database.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT name FROM " + tableName + " ORDER BY name", null);
        List<String> names = new ArrayList<>();
        while (cursor.moveToNext()) names.add(cursor.getString(0));
        cursor.close();
        return names;
    }

    @Override
    public String resolve(String name) {
        SQLiteDatabase database = this.database.getReadableDatabase();
        Cursor cursor = database.rawQuery(
                "SELECT id FROM " + tableName + " WHERE name = ? LIMIT 1",
                new String[]{name});
        String identifier = cursor.moveToFirst() ? cursor.getString(0) : null;
        cursor.close();
        return identifier;
    }
}