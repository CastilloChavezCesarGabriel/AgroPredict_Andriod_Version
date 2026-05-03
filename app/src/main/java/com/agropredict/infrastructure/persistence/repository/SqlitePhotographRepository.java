package com.agropredict.infrastructure.persistence.repository;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.agropredict.application.repository.IPhotographRepository;
import com.agropredict.application.repository.ISessionRepository;
import com.agropredict.domain.entity.Crop;
import com.agropredict.domain.entity.Photograph;
import com.agropredict.infrastructure.persistence.database.Clock;
import com.agropredict.infrastructure.persistence.database.Database;
import com.agropredict.infrastructure.persistence.database.SqliteRow;
import com.agropredict.infrastructure.persistence.visitor.PhotographPersistenceVisitor;

public final class SqlitePhotographRepository implements IPhotographRepository {
    private final Database database;
    private final ISessionRepository sessionRepository;

    public SqlitePhotographRepository(Database database, ISessionRepository sessionRepository) {
        this.database = database;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public void store(Photograph photograph, Crop crop) {
        SqliteRow row = new SqliteRow(database.getWritableDatabase());
        PhotographPersistenceVisitor visitor = new PhotographPersistenceVisitor(row, sessionRepository.recall());
        photograph.accept(visitor);
        crop.accept(visitor);
        row.record("created_at", Clock.read());
        row.flush("image");
    }

    @Override
    public Photograph find(String diagnosticIdentifier) {
        SQLiteDatabase database = this.database.getReadableDatabase();
        String query = "SELECT image.id, image.file_path FROM image "
                + "INNER JOIN diagnostic ON diagnostic.image_id = image.id "
                + "WHERE diagnostic.id = ? LIMIT 1";
        Cursor cursor = database.rawQuery(query, new String[]{diagnosticIdentifier});
        Photograph photograph = cursor.moveToFirst() ? rebuild(cursor) : null;
        cursor.close();
        return photograph;
    }

    private Photograph rebuild(Cursor cursor) {
        return new Photograph(
                cursor.getString(cursor.getColumnIndexOrThrow("id")),
                cursor.getString(cursor.getColumnIndexOrThrow("file_path")));
    }

    @Override
    public void clear(String cropIdentifier) {
        database.getWritableDatabase().delete("image", "crop_id = ?", new String[]{cropIdentifier});
    }
}
