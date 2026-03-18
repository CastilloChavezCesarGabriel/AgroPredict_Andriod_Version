package com.agropredict.infrastructure.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public final class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "agro_diagnostic.db";
    private static final int DATABASE_VERSION = 1;

    private final SchemaCreator schemaCreator;
    private final SeedDataLoader seedDataLoader;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.schemaCreator = new SchemaCreator();
        this.seedDataLoader = new SeedDataLoader();
    }

    @Override
    public void onConfigure(SQLiteDatabase database) {
        database.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        schemaCreator.create(database);
        seedDataLoader.load(database);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
    }
}
