package com.agropredict.infrastructure.persistence;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.agropredict.application.repository.ICropImageRepository;
import com.agropredict.domain.entity.CropImage;

public final class SqliteCropImageRepository implements ICropImageRepository {
    private final DatabaseHelper databaseHelper;

    public SqliteCropImageRepository(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    @Override
    public void store(CropImage image) {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        ContentValues values = record(image);
        database.insert("image", null, values);
    }

    @Override
    public CropImage load(String imageIdentifier) {
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery(
                "SELECT id, file_path FROM image WHERE id = ?",
                new String[]{imageIdentifier});
        CropImage image = extractImage(cursor);
        cursor.close();
        return image;
    }

    private ContentValues record(CropImage image) {
        ContentValues values = new ContentValues();
        CropImageRecorder recorder = new CropImageRecorder(values);
        image.accept(recorder);
        return values;
    }

    private CropImage extractImage(Cursor cursor) {
        if (!cursor.moveToFirst()) {
            return null;
        }
        return CropImage.create(cursor.getString(0), cursor.getString(1));
    }
}