package com.agropredict.infrastructure.persistence.repository;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import com.agropredict.application.repository.IRecordEraser;
import com.agropredict.infrastructure.persistence.database.Database;
import java.util.Objects;

public final class CropCascadeEraser implements IRecordEraser {
    private static final String[] DEPENDENT_TABLES = {"diagnostic", "image", "report"};
    private static final String CROP_TABLE = "crop";
    private static final String IS_ACTIVE = "is_active";
    private static final String DEPENDENT_FOREIGN_KEY = "crop_id = ?";
    private static final String CROP_KEY = "id = ?";
    private final Database database;

    public CropCascadeEraser(Database database) {
        this.database = Objects.requireNonNull(database,
                "crop cascade eraser requires a database");
    }

    @Override
    public void erase(String cropIdentifier) {
        SQLiteDatabase writable = database.getWritableDatabase();
        ContentValues deactivation = new ContentValues();
        deactivation.put(IS_ACTIVE, 0);
        String[] arguments = new String[]{cropIdentifier};
        writable.beginTransaction();
        try {
            for (String table : DEPENDENT_TABLES) {
                writable.update(table, deactivation, DEPENDENT_FOREIGN_KEY, arguments);
            }
            writable.update(CROP_TABLE, deactivation, CROP_KEY, arguments);
            writable.setTransactionSuccessful();
        } finally {
            writable.endTransaction();
        }
    }
}