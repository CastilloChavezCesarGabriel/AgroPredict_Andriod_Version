package com.agropredict.infrastructure.persistence.repository;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.agropredict.application.repository.IDiagnosticRepository;
import com.agropredict.domain.entity.Diagnostic;
import com.agropredict.domain.component.diagnostic.Prediction;
import com.agropredict.infrastructure.persistence.database.Database;
import com.agropredict.infrastructure.persistence.database.SqliteRow;
import com.agropredict.infrastructure.persistence.visitor.DiagnosticPersistenceVisitor;
import java.util.ArrayList;
import java.util.List;

public final class SqliteDiagnosticRepository implements IDiagnosticRepository {
    private static final String SELECT_DIAGNOSTIC = "SELECT d.id, d.predicted_crop, "
            + "d.confidence, d.severity, d.recommendation_text, d.short_summary, "
            + "d.created_at, d.crop_id, d.image_id, d.user_id, "
            + "d.temperature, d.humidity, "
            + "c.crop_type, c.field_name, c.location, c.planting_date, "
            + "i.file_path "
            + "FROM diagnostic d "
            + "LEFT JOIN crop c ON d.crop_id = c.id "
            + "LEFT JOIN image i ON d.image_id = i.id ";
    private final Database database;

    public SqliteDiagnosticRepository(Database database) {
        this.database = database;
    }

    @Override
    public void store(Diagnostic diagnostic) {
        SqliteRow row = new SqliteRow(database.getWritableDatabase());
        DiagnosticPersistenceVisitor visitor = new DiagnosticPersistenceVisitor(row);
        diagnostic.accept(visitor);
        row.flush("diagnostic");
    }

    @Override
    public void delete(String diagnosticIdentifier) {
        database.getWritableDatabase().delete("diagnostic", "id = ?", new String[]{diagnosticIdentifier});
    }

    @Override
    public List<Diagnostic> list(String userIdentifier) {
        return fetch(SELECT_DIAGNOSTIC
                + "WHERE d.user_id = ? ORDER BY d.created_at DESC", new String[]{userIdentifier});
    }

    @Override
    public List<Diagnostic> filter(String userIdentifier, String cropIdentifier) {
        return fetch(SELECT_DIAGNOSTIC
                + "WHERE d.user_id = ? AND d.crop_id = ? ORDER BY d.created_at DESC",
                new String[]{userIdentifier, cropIdentifier});
    }

    @Override
    public Diagnostic find(String diagnosticIdentifier) {
        SQLiteDatabase db = database.getReadableDatabase();
        Cursor cursor = db.rawQuery(SELECT_DIAGNOSTIC + "WHERE d.id = ?", new String[]{diagnosticIdentifier});
        Diagnostic result = cursor.moveToFirst() ? restore(cursor) : null;
        cursor.close();
        return result;
    }

    @Override
    public Diagnostic resolve(String userIdentifier, String cropIdentifier) {
        List<Diagnostic> results = fetch(SELECT_DIAGNOSTIC
                + "WHERE d.user_id = ? AND d.crop_id = ? ORDER BY d.created_at DESC",
                new String[]{userIdentifier, cropIdentifier});
        return results.isEmpty() ? null : results.get(0);
    }

    private Diagnostic restore(Cursor cursor) {
        Prediction prediction = new Prediction(
                cursor.getString(cursor.getColumnIndexOrThrow("predicted_crop")), cursor.getDouble(cursor.getColumnIndexOrThrow("confidence")));
        Diagnostic diagnostic = new Diagnostic(cursor.getString(cursor.getColumnIndexOrThrow("id")), prediction);
        String severity = cursor.getString(cursor.getColumnIndexOrThrow("severity"));
        if (severity != null) {
            diagnostic.conclude(severity, cursor.getString(cursor.getColumnIndexOrThrow("short_summary")));
            diagnostic.recommend(cursor.getString(cursor.getColumnIndexOrThrow("recommendation_text")));
        }
        return diagnostic;
    }

    private List<Diagnostic> fetch(String query, String[] parameters) {
        SQLiteDatabase db = database.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, parameters);
        List<Diagnostic> results = new ArrayList<>();
        while (cursor.moveToNext()) {
            results.add(restore(cursor));
        }
        cursor.close();
        return results;
    }
}