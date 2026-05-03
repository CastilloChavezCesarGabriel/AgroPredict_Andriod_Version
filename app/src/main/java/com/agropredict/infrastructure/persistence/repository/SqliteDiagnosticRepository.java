package com.agropredict.infrastructure.persistence.repository;

import android.database.Cursor;
import com.agropredict.application.repository.IDiagnosticRepository;
import com.agropredict.application.repository.ISessionRepository;
import com.agropredict.domain.entity.Diagnostic;
import com.agropredict.domain.component.diagnostic.Prediction;
import com.agropredict.domain.component.diagnostic.Recommendation;
import com.agropredict.infrastructure.persistence.database.Clock;
import com.agropredict.infrastructure.persistence.database.Database;
import com.agropredict.infrastructure.persistence.database.SqliteRow;
import com.agropredict.infrastructure.persistence.database.SqliteRowStore;
import com.agropredict.infrastructure.persistence.visitor.DiagnosticPersistenceVisitor;
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
    private final SqliteRowStore store;
    private final ISessionRepository sessionRepository;

    public SqliteDiagnosticRepository(Database database, ISessionRepository sessionRepository) {
        this.database = database;
        this.store = new SqliteRowStore(database);
        this.sessionRepository = sessionRepository;
    }

    @Override
    public void store(Diagnostic diagnostic) {
        SqliteRow row = new SqliteRow(database.getWritableDatabase());
        diagnostic.accept(new DiagnosticPersistenceVisitor(row, sessionRepository.recall()));
        row.record("created_at", Clock.read());
        row.flush("diagnostic");
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
        return store.fetch(SELECT_DIAGNOSTIC
                + "WHERE d.user_id = ? ORDER BY d.created_at DESC",
                new String[]{userIdentifier}, this::recover);
    }

    @Override
    public List<Diagnostic> filter(String userIdentifier, String cropType) {
        return store.fetch(SELECT_DIAGNOSTIC
                + "WHERE d.user_id = ? AND d.predicted_crop = ? ORDER BY d.created_at DESC",
                new String[]{userIdentifier, cropType}, this::recover);
    }

    @Override
    public Diagnostic find(String diagnosticIdentifier) {
        return store.locate(SELECT_DIAGNOSTIC + "WHERE d.id = ?", diagnosticIdentifier, this::recover);
    }

    @Override
    public Diagnostic resolve(String userIdentifier, String cropIdentifier) {
        List<Diagnostic> results = store.fetch(SELECT_DIAGNOSTIC
                + "WHERE d.user_id = ? AND d.crop_id = ? ORDER BY d.created_at DESC",
                new String[]{userIdentifier, cropIdentifier}, this::recover);
        return results.isEmpty() ? null : results.get(0);
    }

    private Diagnostic recover(Cursor cursor) {
        Prediction prediction = new Prediction(
                cursor.getString(cursor.getColumnIndexOrThrow("predicted_crop")),
                cursor.getDouble(cursor.getColumnIndexOrThrow("confidence")));
        Diagnostic diagnostic = new Diagnostic(
                cursor.getString(cursor.getColumnIndexOrThrow("id")), prediction);
        String severity = cursor.getString(cursor.getColumnIndexOrThrow("severity"));
        if (severity != null) {
            diagnostic.conclude(severity, new Recommendation(
                    cursor.getString(cursor.getColumnIndexOrThrow("short_summary")),
                    cursor.getString(cursor.getColumnIndexOrThrow("recommendation_text"))));
        }
        return diagnostic;
    }
}
