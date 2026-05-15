package com.agropredict.infrastructure.persistence.repository;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.agropredict.application.repository.IPhotographRepository;
import com.agropredict.application.repository.ISessionRepository;
import com.agropredict.domain.photograph.Photograph;
import com.agropredict.infrastructure.persistence.database.Database;
import com.agropredict.infrastructure.persistence.database.SqliteRow;
import com.agropredict.infrastructure.persistence.database.SqliteRowFactory;
import com.agropredict.infrastructure.persistence.visitor.PhotographPersistenceVisitor;
import java.util.Objects;

public final class SqlitePhotographRepository implements IPhotographRepository {
    private final Database database;
    private final ISessionRepository sessionRepository;
    private final SqliteRowFactory rowFactory;

    public SqlitePhotographRepository(Database database, ISessionRepository sessionRepository, SqliteRowFactory rowFactory) {
        this.database = Objects.requireNonNull(database, "photograph repository requires a database");
        this.sessionRepository = Objects.requireNonNull(sessionRepository, "photograph repository requires a session repository");
        this.rowFactory = Objects.requireNonNull(rowFactory, "photograph repository requires a row factory");
    }

    @Override
    public void store(Photograph photograph, String cropIdentifier) {
        SqliteRow row = rowFactory.open();
        PhotographPersistenceVisitor visitor = new PhotographPersistenceVisitor(row, sessionRepository.recall());
        photograph.expose(visitor);
        visitor.link(cropIdentifier);
        row.stamp("created_at");
        row.mark("is_active", 1);
        row.flush("image");
    }

    @Override
    public Photograph find(String diagnosticIdentifier) {
        SQLiteDatabase database = this.database.getReadableDatabase();
        String query = "SELECT image.id, image.file_path FROM image "
                + "INNER JOIN diagnostic ON diagnostic.image_id = image.id "
                + "WHERE diagnostic.id = ? AND image.is_active = 1 LIMIT 1";
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
}
