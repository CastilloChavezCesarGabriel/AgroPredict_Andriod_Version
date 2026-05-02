package com.agropredict.infrastructure.persistence.repository;

import android.database.Cursor;
import com.agropredict.application.repository.IDiagnosticRepository;
import com.agropredict.domain.entity.Diagnostic;
import com.agropredict.domain.component.diagnostic.Prediction;
import com.agropredict.infrastructure.persistence.database.Database;
import com.agropredict.infrastructure.persistence.database.SqliteRow;
import com.agropredict.infrastructure.persistence.visitor.DiagnosticPersistenceVisitor;
import java.util.List;

public final class SqliteDiagnosticRepository extends SqliteRepository<Diagnostic> implements IDiagnosticRepository {
    private static final String SELECT_DIAGNOSTIC = "SELECT d.id, d.predicted_crop, "
            + "d.confidence, d.severity, d.recommendation_text, d.short_summary, "
            + "d.created_at, d.crop_id, d.image_id, d.user_id, "
            + "d.temperature, d.humidity, "
            + "c.crop_type, c.field_name, c.location, c.planting_date, "
            + "i.file_path "
            + "FROM diagnostic d "
            + "LEFT JOIN crop c ON d.crop_id = c.id "
            + "LEFT JOIN image i ON d.image_id = i.id ";

    public SqliteDiagnosticRepository(Database database) {
        super(database, "diagnostic");
    }

    @Override
    protected void persist(Diagnostic diagnostic, SqliteRow row) {
        diagnostic.accept(new DiagnosticPersistenceVisitor(row));
    }

    @Override
    protected Diagnostic restore(Cursor cursor) {
        Prediction prediction = new Prediction(
                cursor.getString(cursor.getColumnIndexOrThrow("predicted_crop")),
                cursor.getDouble(cursor.getColumnIndexOrThrow("confidence")));
        Diagnostic diagnostic = new Diagnostic(
                cursor.getString(cursor.getColumnIndexOrThrow("id")), prediction);
        String severity = cursor.getString(cursor.getColumnIndexOrThrow("severity"));
        if (severity != null) {
            diagnostic.conclude(
                    severity,
                    cursor.getString(cursor.getColumnIndexOrThrow("short_summary")),
                    cursor.getString(cursor.getColumnIndexOrThrow("recommendation_text")));
        }
        return diagnostic;
    }

    @Override
    public void delete(String diagnosticIdentifier) {
        database.getWritableDatabase().delete("diagnostic", "id = ?", new String[]{diagnosticIdentifier});
    }

    @Override
    public void clear(String cropIdentifier) {
        database.getWritableDatabase().delete("diagnostic", "crop_id = ?", new String[]{cropIdentifier});
    }

    @Override
    public List<Diagnostic> list(String userIdentifier) {
        return fetch(SELECT_DIAGNOSTIC
                + "WHERE d.user_id = ? ORDER BY d.created_at DESC", new String[]{userIdentifier});
    }

    @Override
    public List<Diagnostic> filter(String userIdentifier, String cropType) {
        return fetch(SELECT_DIAGNOSTIC
                + "WHERE d.user_id = ? AND d.predicted_crop = ? ORDER BY d.created_at DESC",
                new String[]{userIdentifier, cropType});
    }

    @Override
    public Diagnostic find(String diagnosticIdentifier) {
        return locate(SELECT_DIAGNOSTIC + "WHERE d.id = ?", diagnosticIdentifier);
    }

    @Override
    public Diagnostic resolve(String userIdentifier, String cropIdentifier) {
        List<Diagnostic> results = fetch(SELECT_DIAGNOSTIC
                + "WHERE d.user_id = ? AND d.crop_id = ? ORDER BY d.created_at DESC",
                new String[]{userIdentifier, cropIdentifier});
        return results.isEmpty() ? null : results.get(0);
    }
}