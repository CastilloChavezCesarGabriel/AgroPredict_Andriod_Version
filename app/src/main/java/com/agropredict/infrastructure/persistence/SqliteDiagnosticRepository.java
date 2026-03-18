package com.agropredict.infrastructure.persistence;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.agropredict.application.repository.IDiagnosticRepository;
import com.agropredict.domain.entity.Diagnostic;
import com.agropredict.domain.value.diagnostic.DiagnosticAssessment;
import com.agropredict.domain.value.diagnostic.DiagnosticConditions;
import com.agropredict.domain.value.diagnostic.DiagnosticContent;
import com.agropredict.domain.value.diagnostic.DiagnosticContext;
import com.agropredict.domain.value.diagnostic.DiagnosticData;
import com.agropredict.domain.value.diagnostic.DiagnosticEnvironment;
import com.agropredict.domain.value.diagnostic.DiagnosticOwnership;
import com.agropredict.domain.value.diagnostic.DiagnosticSummary;
import com.agropredict.domain.value.diagnostic.Prediction;
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

    private final DatabaseHelper databaseHelper;

    public SqliteDiagnosticRepository(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    @Override
    public void store(Diagnostic diagnostic) {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        ContentValues values = record(diagnostic);
        database.insert("diagnostic", null, values);
    }

    @Override
    public void delete(String diagnosticIdentifier) {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        deleteRelatedImage(database, diagnosticIdentifier);
        deleteRelatedCrop(database, diagnosticIdentifier);
        database.delete("diagnostic", "id = ?", new String[]{diagnosticIdentifier});
    }

    @Override
    public List<Diagnostic> list(String userIdentifier) {
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        String query = SELECT_DIAGNOSTIC
                + "WHERE d.user_id = ? ORDER BY d.created_at DESC";
        Cursor cursor = database.rawQuery(query, new String[]{userIdentifier});
        List<Diagnostic> diagnostics = extractDiagnostics(cursor);
        cursor.close();
        return diagnostics;
    }

    @Override
    public Diagnostic load(String diagnosticIdentifier) {
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        String query = SELECT_DIAGNOSTIC + "WHERE d.id = ?";
        Cursor cursor = database.rawQuery(query, new String[]{diagnosticIdentifier});
        Diagnostic diagnostic = extractSingle(cursor);
        cursor.close();
        return diagnostic;
    }

    private ContentValues record(Diagnostic diagnostic) {
        ContentValues values = new ContentValues();
        diagnostic.accept(new DiagnosticRecorder(values));
        return values;
    }

    private void deleteRelatedImage(SQLiteDatabase database, String diagnosticIdentifier) {
        database.execSQL(
                "DELETE FROM image WHERE id IN "
                        + "(SELECT image_id FROM diagnostic WHERE id = ?)",
                new Object[]{diagnosticIdentifier});
    }

    private void deleteRelatedCrop(SQLiteDatabase database, String diagnosticIdentifier) {
        database.execSQL(
                "DELETE FROM crop WHERE id IN "
                        + "(SELECT crop_id FROM diagnostic WHERE id = ?)",
                new Object[]{diagnosticIdentifier});
    }

    private List<Diagnostic> extractDiagnostics(Cursor cursor) {
        List<Diagnostic> diagnostics = new ArrayList<>();
        while (cursor.moveToNext()) {
            diagnostics.add(build(cursor));
        }
        return diagnostics;
    }

    private Diagnostic extractSingle(Cursor cursor) {
        if (!cursor.moveToFirst()) {
            return null;
        }
        return build(cursor);
    }

    private Diagnostic build(Cursor cursor) {
        Prediction prediction = new Prediction(
                cursor.getString(COLUMN_PREDICTED_CROP), cursor.getDouble(COLUMN_CONFIDENCE));
        DiagnosticConditions conditions = compose(cursor);
        DiagnosticAssessment assessment = assess(cursor);
        DiagnosticContent content = new DiagnosticContent(conditions, assessment);
        DiagnosticData data = new DiagnosticData(prediction, content);
        return Diagnostic.create(cursor.getString(COLUMN_IDENTIFIER), data);
    }

    private DiagnosticConditions compose(Cursor cursor) {
        DiagnosticContext context = new DiagnosticContext(
                cursor.getString(COLUMN_CROP_ID), cursor.getString(COLUMN_IMAGE_ID));
        DiagnosticEnvironment environment = new DiagnosticEnvironment(
                cursor.getDouble(COLUMN_TEMPERATURE), cursor.getDouble(COLUMN_HUMIDITY));
        return new DiagnosticConditions(context, environment);
    }

    private DiagnosticAssessment assess(Cursor cursor) {
        DiagnosticSummary summary = new DiagnosticSummary(
                cursor.getString(COLUMN_SEVERITY), cursor.getString(COLUMN_SHORT_SUMMARY));
        DiagnosticOwnership ownership = new DiagnosticOwnership(
                cursor.getString(COLUMN_USER_ID), cursor.getString(COLUMN_RECOMMENDATION_TEXT));
        return new DiagnosticAssessment(summary, ownership);
    }
}
