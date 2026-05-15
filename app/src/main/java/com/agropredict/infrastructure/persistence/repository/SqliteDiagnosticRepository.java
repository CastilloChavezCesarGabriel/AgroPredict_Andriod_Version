package com.agropredict.infrastructure.persistence.repository;

import android.database.Cursor;
import com.agropredict.application.repository.IDiagnosticRepository;
import com.agropredict.application.repository.IRecordEraser;
import com.agropredict.domain.diagnostic.Diagnostic;
import com.agropredict.domain.diagnostic.recommendation.Advice;
import com.agropredict.domain.diagnostic.recommendation.IAdvice;
import com.agropredict.domain.diagnostic.recommendation.ISummary;
import com.agropredict.domain.diagnostic.recommendation.NoAdvice;
import com.agropredict.domain.diagnostic.recommendation.NoSummary;
import com.agropredict.domain.diagnostic.classification.Prediction;
import com.agropredict.domain.diagnostic.recommendation.Recommendation;
import com.agropredict.domain.diagnostic.severity.ISeverity;
import com.agropredict.domain.diagnostic.recommendation.Summary;
import com.agropredict.infrastructure.persistence.database.Database;
import com.agropredict.infrastructure.persistence.database.SqliteRow;
import com.agropredict.infrastructure.persistence.database.SqliteRowFactory;
import com.agropredict.infrastructure.persistence.database.SqliteRowStore;
import com.agropredict.infrastructure.persistence.visitor.DiagnosticPersistenceVisitor;
import java.util.List;
import java.util.Objects;

public final class SqliteDiagnosticRepository implements IDiagnosticRepository, IRecordEraser {
    private static final String SELECT_DIAGNOSTIC = "SELECT d.id, d.predicted_crop, "
            + "d.confidence, d.severity, d.recommendation_text, d.short_summary, "
            + "d.created_at, d.crop_id, d.image_id, d.user_id, "
            + "d.temperature, d.humidity, "
            + "c.crop_type, c.field_name, c.location, c.planting_date, "
            + "i.file_path "
            + "FROM diagnostic d "
            + "LEFT JOIN crop c ON d.crop_id = c.id "
            + "LEFT JOIN image i ON d.image_id = i.id ";

    private final SqliteRowStore store;
    private final DiagnosticPersistenceContext context;
    private final SqliteRowFactory rowFactory;

    public SqliteDiagnosticRepository(Database database, DiagnosticPersistenceContext context, SqliteRowFactory rowFactory) {
        Objects.requireNonNull(database, "diagnostic repository requires a database");
        this.context = Objects.requireNonNull(context, "diagnostic repository requires a context");
        this.rowFactory = Objects.requireNonNull(rowFactory, "diagnostic repository requires a row factory");
        this.store = new SqliteRowStore(database);
    }

    @Override
    public void store(Diagnostic diagnostic) {
        SqliteRow row = rowFactory.open();
        new DiagnosticPersistenceVisitor(row, context.recall()).persist(diagnostic);
        row.stamp("created_at");
        row.mark("is_active", 1);
        row.flush("diagnostic");
    }

    @Override
    public void erase(String diagnosticIdentifier) {
        store.deactivate("diagnostic", diagnosticIdentifier);
    }

    @Override
    public List<Diagnostic> list(String userIdentifier) {
        return store.fetch(SELECT_DIAGNOSTIC
                + "WHERE d.user_id = ? AND d.is_active = 1 ORDER BY d.created_at DESC",
                new String[]{userIdentifier}, this::recover);
    }

    @Override
    public List<Diagnostic> filter(String userIdentifier, String cropType) {
        return store.fetch(SELECT_DIAGNOSTIC
                + "WHERE d.user_id = ? AND d.predicted_crop = ? AND d.is_active = 1 ORDER BY d.created_at DESC",
                new String[]{userIdentifier, cropType}, this::recover);
    }

    @Override
    public Diagnostic find(String diagnosticIdentifier) {
        return store.locate(SELECT_DIAGNOSTIC + "WHERE d.id = ? AND d.is_active = 1", diagnosticIdentifier, this::recover);
    }

    @Override
    public Diagnostic resolve(String userIdentifier, String cropIdentifier) {
        List<Diagnostic> results = store.fetch(SELECT_DIAGNOSTIC
                + "WHERE d.user_id = ? AND d.crop_id = ? AND d.is_active = 1 ORDER BY d.created_at DESC",
                new String[]{userIdentifier, cropIdentifier}, this::recover);
        return results.isEmpty() ? null : results.get(0);
    }

    private Diagnostic recover(Cursor cursor) {
        Prediction prediction = new Prediction(
                cursor.getString(cursor.getColumnIndexOrThrow("predicted_crop")),
                cursor.getDouble(cursor.getColumnIndexOrThrow("confidence")));
        Diagnostic diagnostic = Diagnostic.begin(
                cursor.getString(cursor.getColumnIndexOrThrow("id")), prediction, context.createPending());
        String severityValue = cursor.getString(cursor.getColumnIndexOrThrow("severity"));
        String summary = cursor.getString(cursor.getColumnIndexOrThrow("short_summary"));
        String advice = cursor.getString(cursor.getColumnIndexOrThrow("recommendation_text"));
        if (severityValue != null && (summary != null || advice != null)) {
            ISeverity severity = context.classify(severityValue);
            ISummary summaryPart = summary == null ? new NoSummary() : new Summary(summary);
            IAdvice advicePart = advice == null ? new NoAdvice() : new Advice(advice);
            return diagnostic.conclude(severity, new Recommendation(summaryPart, advicePart));
        }
        return diagnostic;
    }
}