package com.agropredict.infrastructure.persistence.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.agropredict.infrastructure.persistence.schema.SqliteSchema;
import com.agropredict.infrastructure.persistence.schema.SeedLoader;

public final class Database extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 6;
    private final SqliteSchema schema;
    private final Context context;

    public Database(Context context, String name) {
        super(context, name, null, DATABASE_VERSION);
        this.context = context;
        this.schema = new SqliteSchema();
    }

    @Override
    public void onConfigure(SQLiteDatabase database) {
        database.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        schema.create(database);
        new SeedLoader(database, context.getAssets()).load();
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        schema.drop(database);
        onCreate(database);
    }
}