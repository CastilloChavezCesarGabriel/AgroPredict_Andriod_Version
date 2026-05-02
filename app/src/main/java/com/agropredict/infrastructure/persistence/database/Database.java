package com.agropredict.infrastructure.persistence.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.agropredict.infrastructure.persistence.schema.Schema;
import com.agropredict.infrastructure.persistence.schema.SeedLoader;

public final class Database extends SQLiteOpenHelper {
    private static final String TAG = "Database";
    private static final String DATABASE_NAME = "agro_diagnostic.db";
    private static final int DATABASE_VERSION = 2;
    private final Schema schema;
    private final SeedLoader seedLoader;

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.schema = new Schema();
        this.seedLoader = new SeedLoader();
    }

    @Override
    public void onConfigure(SQLiteDatabase database) {
        database.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        schema.create(database);
        seedLoader.load(database);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            try {
                database.execSQL("ALTER TABLE log_entry ADD COLUMN log_action TEXT");
            } catch (SQLiteException exception) {
                Log.w(TAG, "log_entry.log_action column already present", exception);
            }
        }
    }
}