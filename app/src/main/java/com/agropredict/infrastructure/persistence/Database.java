package com.agropredict.infrastructure.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.agropredict.infrastructure.persistence.schema.Schema;
import com.agropredict.infrastructure.persistence.schema.SeedData;

public final class Database extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "agro_diagnostic.db";
    private static final int DATABASE_VERSION = 1;
    private final Schema schema;
    private final SeedData seedData;

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.schema = new Schema();
        this.seedData = new SeedData();
    }

    @Override
    public void onConfigure(SQLiteDatabase database) {
        database.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        schema.create(database);
        seedData.load(database);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {}
}