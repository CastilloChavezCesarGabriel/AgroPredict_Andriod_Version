package com.agropredict.infrastructure.persistence.repository;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.agropredict.application.repository.IDiagnosticRepository;
import com.agropredict.domain.entity.Diagnostic;
import com.agropredict.domain.component.diagnostic.DiagnosticAssessment;
import com.agropredict.domain.component.diagnostic.DiagnosticConditions;
import com.agropredict.domain.component.diagnostic.DiagnosticContent;
import com.agropredict.domain.component.diagnostic.DiagnosticContext;
import com.agropredict.domain.component.diagnostic.DiagnosticData;
import com.agropredict.domain.component.diagnostic.DiagnosticEnvironment;
import com.agropredict.domain.component.diagnostic.DiagnosticOwnership;
import com.agropredict.domain.component.diagnostic.DiagnosticSummary;
import com.agropredict.domain.component.diagnostic.Prediction;
import com.agropredict.infrastructure.persistence.Database;
import com.agropredict.infrastructure.persistence.SqliteRow;
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
    private static final int COLUMN_IDENTIFIER = 0;
    private static final int COLUMN_PREDICTED_CROP = 1;
    private static final int COLUMN_CONFIDENCE = 2;
    private static final int COLUMN_SEVERITY = 3;
    private static final int COLUMN_RECOMMENDATION_TEXT = 4;
    private static final int COLUMN_SHORT_SUMMARY = 5;
    private static final int COLUMN_CROP_ID = 7;
    private static final int COLUMN_IMAGE_ID = 8;
    private static final int COLUMN_USER_ID = 9;
    private static final int COLUMN_TEMPERATURE = 10;
    private static final int COLUMN_HUMIDITY = 11;

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
                cursor.getString(COLUMN_PREDICTED_CROP), cursor.getDouble(COLUMN_CONFIDENCE));
        DiagnosticContext context = new DiagnosticContext(
                cursor.getString(COLUMN_CROP_ID), cursor.getString(COLUMN_IMAGE_ID));
        DiagnosticEnvironment environment = new DiagnosticEnvironment(
                cursor.getDouble(COLUMN_TEMPERATURE), cursor.getDouble(COLUMN_HUMIDITY));
        DiagnosticConditions conditions = new DiagnosticConditions(context, environment);
        DiagnosticSummary summary = new DiagnosticSummary(
                cursor.getString(COLUMN_SEVERITY), cursor.getString(COLUMN_SHORT_SUMMARY));
        DiagnosticOwnership ownership = new DiagnosticOwnership(
                cursor.getString(COLUMN_USER_ID), cursor.getString(COLUMN_RECOMMENDATION_TEXT));
        DiagnosticAssessment assessment = new DiagnosticAssessment(summary, ownership);
        DiagnosticContent content = new DiagnosticContent(conditions, assessment);
        DiagnosticData data = new DiagnosticData(prediction, content);
        return Diagnostic.create(cursor.getString(COLUMN_IDENTIFIER), data);
    }

    @Override
    public void delete(String diagnosticIdentifier) {
        SQLiteDatabase database = this.database.getWritableDatabase();
        database.execSQL("DELETE FROM image WHERE id IN (SELECT image_id FROM diagnostic WHERE id = ?)",
                new Object[]{diagnosticIdentifier});
        database.execSQL("DELETE FROM crop WHERE id IN (SELECT crop_id FROM diagnostic WHERE id = ?)",
                new Object[]{diagnosticIdentifier});
        database.delete("diagnostic", "id = ?", new String[]{diagnosticIdentifier});
    }

    @Override
    public List<Diagnostic> list(String userIdentifier) {
        SQLiteDatabase database = this.database.getReadableDatabase();
        String query = SELECT_DIAGNOSTIC
                + "WHERE d.user_id = ? ORDER BY d.created_at DESC";
        Cursor cursor = database.rawQuery(query, new String[]{userIdentifier});
        List<Diagnostic> diagnostics = read(cursor);
        cursor.close();
        return diagnostics;
    }

    @Override
    public Diagnostic find(String diagnosticIdentifier) {
        SQLiteDatabase database = this.database.getReadableDatabase();
        String query = SELECT_DIAGNOSTIC + "WHERE d.id = ?";
        Cursor cursor = database.rawQuery(query, new String[]{diagnosticIdentifier});
        Diagnostic diagnostic = cursor.moveToFirst() ? restore(cursor) : null;
        cursor.close();
        return diagnostic;
    }
}