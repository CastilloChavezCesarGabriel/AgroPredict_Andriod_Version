package com.agropredict.infrastructure.persistence.repository;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.agropredict.application.repository.ICropImageRepository;
import com.agropredict.domain.entity.CropImage;
import com.agropredict.infrastructure.persistence.Database;
import com.agropredict.infrastructure.persistence.SqliteRow;
import com.agropredict.infrastructure.persistence.visitor.CropImagePersistenceVisitor;

public final class SqliteCropImageRepository extends SqliteRepository<CropImage> implements ICropImageRepository {
    public SqliteCropImageRepository(Database database) {
        super(database, "image");
    }

    @Override
    protected void persist(CropImage image, SqliteRow row) {
        image.accept(new CropImagePersistenceVisitor(row));
    }

    @Override
    public CropImage find(String imageIdentifier) {
        SQLiteDatabase database = this.database.getReadableDatabase();
        Cursor cursor = database.rawQuery(
                "SELECT id, file_path FROM image WHERE id = ?",
                new String[]{imageIdentifier});
        CropImage image = cursor.moveToFirst() ? CropImage.create(cursor.getString(0), cursor.getString(1)) : null;
        cursor.close();
        return image;
    }
}