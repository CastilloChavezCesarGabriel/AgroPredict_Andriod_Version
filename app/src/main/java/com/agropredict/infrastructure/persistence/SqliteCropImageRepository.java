package com.agropredict.infrastructure.persistence;

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
        Recorder recorder = record(image);
        recorder.flush("image");
    }

    @Override
    public CropImage find(String imageIdentifier) {
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery(
                "SELECT id, file_path FROM image WHERE id = ?",
                new String[]{imageIdentifier});
        CropImage image = extractImage(cursor);
        cursor.close();
        return image;
    }

    private Recorder record(CropImage image) {
        Recorder recorder = new Recorder(databaseHelper.getWritableDatabase());
        image.accept(new CropImageRecorder(recorder));
        return recorder;
    }

    private CropImage extractImage(Cursor cursor) {
        if (!cursor.moveToFirst()) {
            return null;
        }
        return CropImage.create(cursor.getString(0), cursor.getString(1));
    }
}